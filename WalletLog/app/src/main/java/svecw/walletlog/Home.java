package svecw.walletlog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Home extends AppCompatActivity {
    CalendarView calendarView;
    Button totalAmount, totalBtn;
    TextView bills;
    ListView payableBills;
    InternalDB internalDB;
    ArrayList<String> dailyName = new ArrayList<>();
    ArrayList<Integer> dailyQty = new ArrayList<>();
    ArrayList<Integer> dailyPrice = new ArrayList<>();
    ArrayList<Integer> dateOfPay =  new ArrayList<>();
    ArrayList<String> monthlyName = new ArrayList<>();
    ArrayList<Integer> monthlyPrice = new ArrayList<>();
    ArrayList<Integer> monthlyDateOfPay = new ArrayList<>();
    ImageView paidDateIcon, billsImage;
    ArrayList<Integer> allMonthsTotals = new ArrayList<>();
    ArrayList<Integer> paidBillsTotals = new ArrayList<>();
    ArrayList<String> paidBillsNamesDaily = new ArrayList<>();
    ArrayList<String> paidBillsnamesMonthly = new ArrayList<>();
    Cursor paidBillsDailyCursor,paidBillsMonthlyCursor;
    String selectedCategoryName, currentDate;
    int i, j, day , month, year;
    MoreDetailsAdapter moreDetailsAdapter;
    static final int DATE_DIALOG_ID = 0;

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Calendar cal = Calendar.getInstance();
        i = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        j = (cal.getActualMaximum((Calendar.DAY_OF_MONTH))) - 1;

        currentDate = cal.get(cal.DAY_OF_MONTH) + "/" + (cal.get(cal.MONTH) + 1) + "/" + cal.get(cal.YEAR);

        internalDB = new InternalDB(this);

        if(internalDB.isExistscategory()) {
            View v = new View(this);
            internalDB.insertCategories(v.getContext());
        }
        bills = (TextView)findViewById(R.id.bills);
        calendarView = (CalendarView) findViewById(R.id.calendar);
        totalAmount = (Button) findViewById(R.id.totalBtn);
        totalBtn = (Button) findViewById(R.id.totalBtn);
        paidDateIcon = (ImageView) findViewById(R.id.paidDateIcon);
        billsImage = (ImageView) findViewById(R.id.billsImage);

        calendarView.setSelectedWeekBackgroundColor(0);
        Cursor dailyCursor = internalDB.getDailyList();
        paidBillsDailyCursor = internalDB.getPaidBills("daily",currentDate,"Total");
        if(paidBillsDailyCursor.moveToFirst()){
            do{
                paidBillsNamesDaily.add(paidBillsDailyCursor.getString(0));
            }while(paidBillsDailyCursor.moveToNext());
        }
        if(dailyCursor.moveToFirst()) {
            do {
                int daysBetween;
                int currentDate = cal.get(cal.DAY_OF_MONTH);
                if (cal.get(cal.DAY_OF_MONTH) <= dailyCursor.getInt(4))
                    daysBetween = dailyCursor.getInt(4) - cal.get(cal.DAY_OF_MONTH);
                else if (dailyCursor.getInt(4) < currentDate && (dailyCursor.getInt(4) == 1 || dailyCursor.getInt(4) == 2) && ((currentDate == i) || (currentDate == j))&& !(paidBillsNamesDaily.contains(dailyCursor.getString(1)))) {
                    dailyName.add(dailyCursor.getString(1));
                    dailyQty.add(dailyCursor.getInt(3));
                    dailyPrice.add(dailyCursor.getInt(2));
                    dateOfPay.add(dailyCursor.getInt(4));
                    daysBetween = 4;
                }
                else
                    daysBetween = 4;

                if(daysBetween <= 3 && !(paidBillsNamesDaily.contains(dailyCursor.getString(1)))) {
                    dailyName.add(dailyCursor.getString(1));
                    dailyQty.add(dailyCursor.getInt(3));
                    dailyPrice.add(dailyCursor.getInt(2));
                    dateOfPay.add(dailyCursor.getInt(4));
                }
            }while (dailyCursor.moveToNext());
        }
        Cursor monthlyCursor = internalDB.getMonthlyDetails();
        paidBillsMonthlyCursor = internalDB.getPaidBills("monthly",currentDate,"Total");
        if(paidBillsMonthlyCursor.moveToFirst()){
            do{
                paidBillsnamesMonthly.add(paidBillsMonthlyCursor.getString(0));
            }while (paidBillsMonthlyCursor.moveToNext());
        }
        if(monthlyCursor.moveToFirst()) {
            do {
                int daysBetween;
                int currentDate = cal.get(cal.DAY_OF_MONTH);
                if (cal.get(cal.DAY_OF_MONTH) <= monthlyCursor.getInt(3))
                    daysBetween = monthlyCursor.getInt(3) - cal.get(cal.DAY_OF_MONTH);
                else if (monthlyCursor.getInt(3) < currentDate && (monthlyCursor.getInt(3) == 1 || monthlyCursor.getInt(3) == 2) && ((currentDate == i) || (currentDate == j)) && !(paidBillsnamesMonthly.contains(monthlyCursor.getString(1)))) {
                    monthlyName.add(monthlyCursor.getString(1));
                    monthlyPrice.add(monthlyCursor.getInt(2));
                    monthlyDateOfPay.add(monthlyCursor.getInt(3));
                    daysBetween = 4;
                }
                else
                    daysBetween = 4;


                if(daysBetween <= 3 && !(paidBillsnamesMonthly.contains(monthlyCursor.getString(1))) ) {
                    monthlyName.add(monthlyCursor.getString(1));
                    monthlyPrice.add(monthlyCursor.getInt(2));
                    monthlyDateOfPay.add(monthlyCursor.getInt(3));
                }
            } while (monthlyCursor.moveToNext());
        }
        ArrayList<Integer> unchecks = new ArrayList<>();
        for(int i = 0;i < dailyName.size();i++){
            int month = cal.get(cal.MONTH) ;
            if(dateOfPay.get(i) > 15) {
                unchecks.add(internalDB.noOfUnchecks(month + 1, dailyName.get(i)));
            }
            else if (dateOfPay.get(i) <= 15){
                if(month == 0){
                    unchecks.add(internalDB.noOfUnchecks(12,dailyName.get(i)));
                }
                else {
                    unchecks.add(internalDB.noOfUnchecks(month,dailyName.get(i)));
                }
            }
        }
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.add(cal.MONTH, -1);
        int prevdays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < dailyName.size(); i++) {
            if(dateOfPay.get(i) > 15) {
                monthlyName.add(dailyName.get(i));
                monthlyPrice.add((days - unchecks.get(i))*dailyPrice.get(i)*dailyQty.get(i));
                monthlyDateOfPay.add(dateOfPay.get(i));
            }
            else {
                monthlyName.add(dailyName.get(i));
                monthlyPrice.add((prevdays - unchecks.get(i))*dailyPrice.get(i)*dailyQty.get(i));
                monthlyDateOfPay.add(dateOfPay.get(i));
            }
        }

        Integer expenditure = internalDB.getTotalAmount(currentDate,"total") + internalDB.getPaidBillsTotal(currentDate, "total");

        if (!(dailyName.isEmpty()) || !(monthlyName.isEmpty())){
            bills.setText("PAYABLE BILLS");
            billsImage.setEnabled(true);
        }

        payableBills = (ListView)findViewById(R.id.payableBills);
        moreDetailsAdapter = new MoreDetailsAdapter(Home.this);
        payableBills.setAdapter(moreDetailsAdapter);
        moreDetailsAdapter.getListAddons(monthlyName, monthlyPrice, monthlyDateOfPay,"total");
        moreDetailsAdapter.notifyDataSetChanged();
        totalAmount.setText("EXPENDITURE" + "    "+ expenditure);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                Intent intent = new Intent(getApplicationContext(), Expenses.class);
                intent.putExtra("selectedDate", date);
                intent.putExtra("day", dayOfMonth);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                intent.putExtra("status", "clicked");
                startActivity(intent);
            }
        });
        totalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(getApplicationContext(), MonthlyTotal.class);
                String currentDate = cal.get(cal.DAY_OF_MONTH) + "/" + (cal.get(cal.MONTH) + 1) % 10 + "/" + cal.get(cal.YEAR);
                intent.putExtra("selectedDate", currentDate);
                intent.putExtra("status", "NotClicked");
                intent.putExtra("day", cal.get(Calendar.DAY_OF_MONTH));
                intent.putExtra("month", cal.get(Calendar.MONTH));
                intent.putExtra("year", cal.get(Calendar.YEAR));
                startActivity(intent);
            }
        });

        billsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Bills.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_Category:
                showDialog();
                break;
            case R.id.action_Graph:
                Intent intent2 = new Intent(this, Graph.class);
                Calendar calendar = Calendar.getInstance();
                allMonthsTotals = internalDB.getAllMonthsTotal(calendar.get(calendar.YEAR));
                paidBillsTotals = internalDB.getGraphPaidBills(calendar.get(calendar.YEAR));
                ArrayList<Integer> totals = new ArrayList<>();
                for(int i = 0;i < allMonthsTotals.size();i++){
                    totals.add(paidBillsTotals.get(i) + allMonthsTotals.get(i));
                }
                intent2.putExtra("allMonthsTotals", totals);
                intent2.putExtra("expense", "TOTAL EXPENSES");
                this.startActivity(intent2);
                break;
            case R.id.action_Settings:
                Intent settingsIntent = new Intent(getApplicationContext(), EditExpenses.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_About:
                Intent intent1 = new Intent(this, About.class);
                this.startActivity(intent1);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    public void showDialog() {
        ArrayList<String> categoryNames = new ArrayList<String>();
        Map<Integer,String> id = new HashMap<>();
        id = internalDB.getCategoryIds();
        id  = new TreeMap<>(id);
        for(HashMap.Entry<Integer,String> entry:id.entrySet()){
            categoryNames.add(entry.getValue());
        }
        categoryNames.remove("Create own category");
        final CharSequence[] names = categoryNames.toArray(new String[categoryNames.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Select Category: ");
        dialogBuilder.setItems(names, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selectedCategoryName = names[item].toString();
                Calendar calendar1 = Calendar.getInstance();
                Integer categoryId;
                Intent intent = new Intent(getApplicationContext(), Graph.class);
                categoryId = internalDB.getIdByName(selectedCategoryName);
                allMonthsTotals = internalDB.getCategoryTotal(categoryId, (calendar1.get(calendar1.YEAR)));
                intent.putExtra("allMonthsTotals", allMonthsTotals);
                intent.putExtra("expense", selectedCategoryName);
                startActivity(intent);
            }
        });
        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
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
        Cursor cursorDaily = internalDB.getDailyDetails(category);
        if(cursorDaily.getCount() > 0){
            if(cursorDaily.moveToFirst()){
                internalDB.insertPaidBills(category, "daily", amount, date);
                startActivity(getIntent());
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
    public void show() {
        List<String> expenses = new ArrayList<String>();
        expenses.add("        Today's expense");
        expenses.add("        Daily expense");
        expenses.add("        Monthly expense");
        final CharSequence[] Expenses = expenses.toArray(new String[expenses.size()]);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add Expense");
        alert.setItems(Expenses, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedExpense = Expenses[item].toString();
                if (selectedExpense.equals("        Today's expense")) {
                    Calendar cal = Calendar.getInstance();
                    Intent intent = new Intent(getApplicationContext(), OneTimeExpenses.class);
                    if (cal.get(cal.MONTH) < 9)
                        currentDate = cal.get(cal.DAY_OF_MONTH) + "/" + (cal.get(cal.MONTH) + 1) + "/" + cal.get(cal.YEAR);
                    else
                        currentDate = cal.get(cal.DAY_OF_MONTH) + "/" + cal.get(cal.MONTH) + "/" + cal.get(cal.YEAR);
                    intent.putExtra("dateOfPurchase", currentDate);
                    intent.putExtra("action","ADD");
                    startActivity(intent);
                } else if (selectedExpense.equals("        Daily expense")) {
                    Intent intent = new Intent(getApplicationContext(), AddExpenses.class);
                    intent.putExtra("expenseType","DAILY");
                    intent.putExtra("type", "ADD");
                    startActivity(intent);
                } else if (selectedExpense.equals("        Monthly expense")) {
                    Intent intent = new Intent(getApplicationContext(), AddExpenses.class);
                    intent.putExtra("expenseType","MONTHLY");
                    intent.putExtra("type", "ADD");
                    startActivity(intent);
                }
            }

        });
        AlertDialog alertDialogObject = alert.create();
        alertDialogObject.show();
    }
}