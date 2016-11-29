package svecw.walletlog;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MonthlyTotal extends AppCompatActivity {
    InternalDB internalDB = new InternalDB(this);
    HashMap<Integer, Integer> addonslist = new HashMap<Integer, Integer>();
    ArrayList<Integer> cid = new ArrayList<Integer>();
    HashMap<Integer,ArrayList<String>> listDataChild = new HashMap<>();
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    PaidBillsAdapter paidBillsAdapter;
    ListView listView;
    TextView total;
    TextView oneTimeExpensesText;
    TextView paidBillsText;
    TextView monthName;
    ImageView previous,next;
    String selectedDate,currentDate,status;
    int day,month,year;
    Calendar cal = Calendar.getInstance();
    String[] monthNames = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_total);
        previous = (ImageView) findViewById(R.id.previous);
        next = (ImageView) findViewById(R.id.next);
        total = (TextView) findViewById(R.id.total);
        oneTimeExpensesText = (TextView)findViewById(R.id.oneTimeExpensesText);
        paidBillsText = (TextView)findViewById(R.id.paidBillsText);
        monthName = (TextView)findViewById(R.id.month);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedDate = bundle.getString("selectedDate");
            status = bundle.getString("status");
            day = bundle.getInt("day");
            month = bundle.getInt("month");
            year= bundle.getInt("year");
        } else {
            selectedDate = null;
            status = null;
            day = 0;
            month = 0;
            year = 0;
        }
        int totalAmount = internalDB.getTotalAmount(selectedDate,"total") + internalDB.getPaidBillsTotal(selectedDate, "total");
        if (totalAmount > 0)
            total.setText("TOTAL    "+totalAmount);
        else {
            total.setText("NO EXPENSES");
        }

        setTitle(""+year);
        monthName.setText(""+monthNames[month]);

        if(status.equals("clicked")) {
            cal.set(year,month,day);
        }

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH,-1);
                currentDate = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                Intent intent = new Intent(getApplicationContext(), MonthlyTotal.class);
                intent.putExtra("selectedDate", currentDate);
                intent.putExtra("status","clicked");
                intent.putExtra("day",cal.get(Calendar.DAY_OF_MONTH));
                intent.putExtra("month",cal.get(Calendar.MONTH));
                intent.putExtra("year",cal.get(Calendar.YEAR));
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH,1);
                currentDate = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                Intent intent = new Intent(getApplicationContext(), MonthlyTotal.class);
                intent.putExtra("selectedDate", currentDate);
                intent.putExtra("status","clicked");
                intent.putExtra("day",cal.get(Calendar.DAY_OF_MONTH));
                intent.putExtra("month",cal.get(Calendar.MONTH));
                intent.putExtra("year",cal.get(Calendar.YEAR));
                startActivity(intent);
            }
        });

        addonslist = internalDB.getOneTimeExpensesOfMonth(selectedDate);
        for (HashMap.Entry<Integer, Integer> entry : addonslist.entrySet()) {
            cid.add(entry.getKey());
        }

        if (!cid.isEmpty())
            oneTimeExpensesText.setText("ONE TIME EXPENSES");

        expandableListView = (ExpandableListView)findViewById(R.id.expanadaleListView);
        for(int i= 0; i < cid.size();i++) {
            listDataChild.put(cid.get(i),internalDB.getMoreDetailsMonth(cid.get(i),selectedDate));
        }
        expandableListAdapter = new ExpandableListAdapter(this,cid,listDataChild,"Total",selectedDate);
        expandableListView.setAdapter(expandableListAdapter);
        listView = (ListView)findViewById(R.id.paidBills);
        ArrayList<String> paidBillsDates = new ArrayList<>();
        ArrayList<String> paidBillsNames = new ArrayList<>();
        ArrayList<Integer> paidBillsAmount = new ArrayList<>();
        Cursor cursorDaily = internalDB.getPaidBills("daily",selectedDate,"Total");
        if(cursorDaily.moveToFirst()){
            do{
                paidBillsNames.add(cursorDaily.getString(0));
                paidBillsAmount.add(cursorDaily.getInt(2));
                paidBillsDates.add(cursorDaily.getString(3));
            }while (cursorDaily.moveToNext());
        }
        Cursor cursorMonthly = internalDB.getPaidBills("monthly",selectedDate,"Total");
        if(cursorMonthly.moveToFirst()){
            do{
                paidBillsNames.add(cursorMonthly.getString(0));
                paidBillsAmount.add(cursorMonthly.getInt(2));
                paidBillsDates.add(cursorMonthly.getString(3));
            }while (cursorMonthly.moveToNext());
        }

        if (!paidBillsNames.isEmpty())
            paidBillsText.setText("BILLS PAID");

        paidBillsAdapter = new PaidBillsAdapter(this);
        listView.setAdapter(paidBillsAdapter);
        paidBillsAdapter.getPaidBills(paidBillsDates ,paidBillsNames, paidBillsAmount, "Total");
        paidBillsAdapter.notifyDataSetChanged();
    }
}

