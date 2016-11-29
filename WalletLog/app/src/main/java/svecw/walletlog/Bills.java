package svecw.walletlog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Bills extends AppCompatActivity {

    MoreDetailsAdapter moreDetailsAdapter;
    ListView listView;
    TextView text;
    InternalDB internalDB;
    TextView billsText;
    Calendar cal;
    Home home;
    int year,month,day;
    static final int DATE_DIALOG_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        listView = (ListView) findViewById(R.id.billsList);
        billsText = (TextView) findViewById(R.id.billsText);
        text = (TextView) findViewById(R.id.text);
        moreDetailsAdapter = new MoreDetailsAdapter(Bills.this);
        internalDB = new InternalDB(this);
        cal = Calendar.getInstance();
        home = new Home();

        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> amounts = new ArrayList<>();
        ArrayList<Integer> doP = null;
        Cursor cursor1 = internalDB.getDailyList();
        if(cursor1.moveToFirst()){
            do{
                names.add(cursor1.getString(1));
                int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                amounts.add(cursor1.getInt(2) * cursor1.getInt(3) * (days-internalDB.noOfUnchecks(cal.get(cal.MONTH)+1,cursor1.getString(1))));
            }while (cursor1.moveToNext());
        }
        Cursor cursor2 = internalDB.getMonthlyDetails();
        if(cursor2.moveToFirst()){
            do{
                names.add(cursor2.getString(1));
                amounts.add(cursor2.getInt(2));
            }while (cursor2.moveToNext());
        }
        listView.setAdapter(moreDetailsAdapter);
        moreDetailsAdapter.getListAddons(names, amounts, doP, "bills");
        moreDetailsAdapter.notifyDataSetChanged();

        if (!(names.isEmpty())) {
            billsText.setText("BILLS");
            text.setText("Note paid date on clicking the calendar");
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DATE_DIALOG_ID) {
            Calendar c = Calendar.getInstance();
            return new DatePickerDialog(this, datePickerListener,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            insertPaidBills();
        }
    };
    public void insertPaidBills() {
        String date = day+"/"+(month+1)+"/"+year;
        String category = null;
        Integer amount = 0;
        HashMap<String,Integer> values = moreDetailsAdapter.getName();
        for(HashMap.Entry<String,Integer> entry:values.entrySet()){
            category = entry.getKey();
            amount = entry.getValue();
        }
        cal.set(year, month, day);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Cursor cursorDaily = internalDB.getDailyDetails(category);
        if(cursorDaily.getCount() > 0){
            if(cursorDaily.moveToFirst()){
                amount = (days - internalDB.noOfUnchecks((month+1),category)) * cursorDaily.getInt(2) * cursorDaily.getInt(3);
                internalDB.insertPaidBills(category, "daily", amount, date);
            }
        }
        else {
            Cursor cursorMonthly = internalDB.getMonthlyDetails(category);
            if (cursorMonthly.getCount() > 0) {
                if (cursorMonthly.moveToFirst()) {
                    internalDB.insertPaidBills(category, "monthly", amount, date);
                    startActivity(getIntent());
                }
            }
        }
        Intent intent = new Intent(getApplicationContext(), Expenses.class);
        intent.putExtra("selectedDate", date);
        intent.putExtra("status","clicked");
        intent.putExtra("day", day);
        intent.putExtra("month", month);
        intent.putExtra("year",year);
        startActivity(intent);
    }
}
