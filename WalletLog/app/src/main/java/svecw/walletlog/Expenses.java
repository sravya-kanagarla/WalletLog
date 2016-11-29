package svecw.walletlog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Expenses extends AppCompatActivity {

    InternalDB internalDB = new InternalDB(this);
    ExpensesAdapter dailyAdapter;
    Calendar cal = Calendar.getInstance();
    String date, status;
    Button save;
    ListView dailyView;
    TextView dailyExpenses;
    TextView oneTimeExpenses;
    TextView totalView;
    ExpandableListView oneTimeExpandable;
    ExpandableListAdapter expandableListAdapter;
    HashMap<Integer, Integer> addonslist = new HashMap<Integer, Integer>();
    ArrayList<String> dailyActivities = new ArrayList<String>();
    ArrayList<Integer> cid = new ArrayList<Integer>();
    HashMap<Integer,ArrayList<String>> listDataChild = new HashMap<>();
    ArrayList<Integer> amts = new ArrayList<Integer>();
    ArrayList<String> changeList = new ArrayList<String>();
    ArrayList<String> unselectedList = new ArrayList<String>();
    ArrayList<String> reselectedList = new ArrayList<String>();
    ImageView previous,next;
    int day,month,year;
    int amount = 0;
    Cursor cursor;
    TextView blink;
    TextView paidBillsText;
    ListView listView;
    PaidBillsAdapter paidBillsAdapter;

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        previous = (ImageView) findViewById(R.id.previousBtn);
        next = (ImageView) findViewById(R.id.nextBtn);
        ActionBar toolbar = getSupportActionBar();
        save = (Button) findViewById(R.id.saveBtn);
        dailyExpenses = (TextView) findViewById(R.id.daily_expenses);
        oneTimeExpenses = (TextView) findViewById(R.id.one_time_expenses);
        totalView = (TextView)findViewById(R.id.totalAmountView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        blink = (TextView) findViewById(R.id.blink);
        paidBillsText = (TextView)findViewById(R.id.paidBillsText);

        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        blink.startAnimation(animation);

        final String selectedDate;
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

        toolbar.setTitle(selectedDate);

        changeList = internalDB.getDailyChanges(selectedDate);
        dailyAdapter = new ExpensesAdapter(Expenses.this);
        dailyView = (ListView) findViewById(R.id.dailyActivity);
        dailyView.setAdapter(dailyAdapter);
        cursor = internalDB.getDailyList();
        if (cursor.moveToFirst()) {
            do {
                dailyActivities.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (!(dailyActivities.isEmpty())) {
            save.setEnabled(true);
            dailyExpenses.setText("DAILY EXPENSES");
            dailyAdapter.getDailyList(dailyActivities, changeList);
        }
        dailyAdapter.notifyDataSetChanged();
        addonslist = internalDB.getOneTimeExpensesOfDate(selectedDate);
        if (!(addonslist.isEmpty())) {
            oneTimeExpenses.setText("ONE TIME EXPENSES");
            blink.setText("Long press to delete");
        }
        for (HashMap.Entry<Integer, Integer> entry : addonslist.entrySet()) {
            cid.add(entry.getKey());
            amts.add(entry.getValue());
        }
        oneTimeExpandable = (ExpandableListView)findViewById(R.id.oneTimeExpenses);
        for(int i =0;i < cid.size();i++) {
            listDataChild.put(cid.get(i), internalDB.getMoreDetailsDate(cid.get(i),selectedDate));
        }
        expandableListAdapter = new ExpandableListAdapter(this,cid,listDataChild,"Date",selectedDate);
        oneTimeExpandable.setAdapter(expandableListAdapter);
        oneTimeExpandable.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long packedPosition = oneTimeExpandable.getExpandableListPosition(position);
                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
                if(itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    showDelete(groupPosition,childPosition,selectedDate);
                }
                return false;
            }
        });

        amount = internalDB.getTotalAmount(selectedDate,"daily");
        listView = (ListView)findViewById(R.id.paidBills);
        ArrayList<String> paidBillsDates = new ArrayList<>();
        ArrayList<String> paidBillsNames = new ArrayList<>();
        ArrayList<Integer> paidBillsAmount = new ArrayList<>();
        Cursor paidBillsCursorDialy = internalDB.getPaidBills("daily",selectedDate,"date");
        if(paidBillsCursorDialy.moveToFirst()){
            do{
                paidBillsNames.add(paidBillsCursorDialy.getString(0));
                paidBillsAmount.add(paidBillsCursorDialy.getInt(2));
            }while (paidBillsCursorDialy.moveToNext());
        }
        Cursor paidBillscursorMonthly = internalDB.getPaidBills("monthly",selectedDate,"date");
        if(paidBillscursorMonthly.moveToFirst()){
            do{
                paidBillsNames.add(paidBillscursorMonthly.getString(0));
                paidBillsAmount.add(paidBillscursorMonthly.getInt(2));
            }while (paidBillscursorMonthly.moveToNext());
        }

        if (!(paidBillsAmount.isEmpty()))
            paidBillsText.setText("PAID BILLS");

        paidBillsAdapter = new PaidBillsAdapter(this);
        listView.setAdapter(paidBillsAdapter);
        paidBillsAdapter.getPaidBills(paidBillsDates ,paidBillsNames, paidBillsAmount, "date");
        paidBillsAdapter.notifyDataSetChanged();
        int paidAmount = internalDB.getPaidBillsTotal(selectedDate, "date");
        if((amount+paidAmount) > 0) {
            totalView.setText("TOTAL:     " + (amount+paidAmount));
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView expense = (TextView)findViewById(R.id.name);
                TextView billAmount = (TextView)findViewById(R.id.amount);
                deleteBill(expense.getText().toString(), billAmount.getText().toString(), selectedDate);
                return true;
            }
        });

        if(status.equals("clicked")) {
            cal.set(year,month,day);
        }

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DAY_OF_MONTH,-1);
                date = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                Intent intent = new Intent(getApplicationContext(), Expenses.class);
                intent.putExtra("selectedDate", date);
                intent.putExtra("status","clicked");
                intent.putExtra("day", cal.get(Calendar.DAY_OF_MONTH));
                intent.putExtra("month",cal.get(Calendar.MONTH));
                intent.putExtra("year",cal.get(Calendar.YEAR));
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DAY_OF_MONTH,+1);
                date = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                Intent intent = new Intent(getApplicationContext(), Expenses.class);
                intent.putExtra("selectedDate", date);
                intent.putExtra("status","clicked");
                intent.putExtra("day", cal.get(Calendar.DAY_OF_MONTH));
                intent.putExtra("month",cal.get(Calendar.MONTH));
                intent.putExtra("year",cal.get(Calendar.YEAR));
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedDate;
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    selectedDate = bundle.getString("selectedDate");
                } else {
                    selectedDate = null;
                }
                Intent intent = new Intent(getApplicationContext(), OneTimeExpenses.class);
                intent.putExtra("dateOfPurchase", selectedDate);
                intent.putExtra("action","ADD");
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlert();

                for (int i = 0; i < unselectedList.size(); i++) {
                    internalDB.insertData(selectedDate, unselectedList.get(i));
                }
                for (int i = 0; i < reselectedList.size(); i++) {
                    internalDB.removeChanges(selectedDate, reselectedList.get(i));
                }
            }
        });
    }
    public void selectedItem(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if(!checked) {
            unselectedList.add((String) ((CheckBox) view).getText());
        }
        else if(checked) {
            reselectedList.add((String) ((CheckBox) view).getText());
        }
    }
    public void showAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Do you want to save changes");
        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(getIntent());
            }
        });
        alert.show();
    }
    public boolean showDelete(final int groupPosition,final int childPosition,final String selectedDate) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Are You sure want to delete");
        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String desc = (String) expandableListAdapter.getChild(groupPosition, childPosition);
                internalDB.deleteOneTimeExpenses(cid.get(groupPosition), desc, internalDB.getAmountsOfDate(cid.get(groupPosition), desc), selectedDate);
                Intent intent = new Intent(getApplicationContext(), Expenses.class);
                intent.putExtra("selectedDate", selectedDate);
                intent.putExtra("status", "clicked");
                String[] divide = selectedDate.split("/");
                intent.putExtra("selectedDate", selectedDate);
                intent.putExtra("day", divide[0]);
                intent.putExtra("month", divide[1]);
                intent.putExtra("year", divide[2]);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
        return false;
    }

    public void deleteBill(final String name, final String billAmount, final String date) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setIcon(R.drawable.ic_action_warning);
        alert.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int rowId = internalDB.getRowIdBills(name, billAmount, date);
                internalDB.deleteBill(rowId);
                finish();
                startActivity(getIntent());
            }
        });
        alert.show();
    }
}