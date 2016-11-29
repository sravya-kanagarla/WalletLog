package svecw.walletlog;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class EditExpenses extends AppCompatActivity {
    InternalDB internalDB = new InternalDB(this);
    ListView dailyList;
    ListView monthlyList;
    ArrayList<String> dailyActivities = new ArrayList<String>();
    ArrayList<String> monthlyActivities = new ArrayList<String>();
    TextView daily;
    TextView monthly;
    Cursor cursor;
    EditExpensesAdapter editExpensesAdapter;
    ImageView editDailyExpense;
    TextView blink;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expenses);
        daily = (TextView)findViewById(R.id.dailyExpense);
        monthly = (TextView)findViewById(R.id.monthlyExpense);
        dailyList = (ListView)findViewById(R.id.daily);
        monthlyList = (ListView)findViewById(R.id.monthly);
        editDailyExpense = (ImageView)findViewById(R.id.editExpense);
        blink = (TextView)findViewById(R.id.blink);

        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        blink.startAnimation(animation);

        cursor = internalDB.getDailyList();
        if (cursor.moveToFirst()) {
            do {
                dailyActivities.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor = internalDB.getMonthlyDetails();

        if (cursor.moveToFirst()) {
            do {
                monthlyActivities.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (!(dailyActivities.isEmpty()))
            daily.setText("DAILY EXPENSES");
                if (!(monthlyActivities.isEmpty()))
            monthly.setText("MONTHLY EXPENSES");
        if (!(dailyActivities.isEmpty()) || !(monthlyActivities.isEmpty()))
            blink.setText("Long press to delete");

        dailyList = (ListView)findViewById(R.id.daily);
        editExpensesAdapter = new EditExpensesAdapter(EditExpenses.this);
        dailyList.setAdapter(editExpensesAdapter);
        editExpensesAdapter.getDetails(dailyActivities);
        editExpensesAdapter.notifyDataSetChanged();

        monthlyList = (ListView)findViewById(R.id.monthly);
        editExpensesAdapter = new EditExpensesAdapter(EditExpenses.this);
        monthlyList.setAdapter(editExpensesAdapter);
        editExpensesAdapter.getDetails(monthlyActivities);
        editExpensesAdapter.notifyDataSetChanged();

        dailyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name;
                Integer dateOfPay;
                Integer pricePerQuantity;
                Integer quantity;
                registerForContextMenu(dailyList);
                dailyList.showContextMenu();
                name = (TextView) view.findViewById(R.id.expense);
                Cursor cursor = internalDB.getDailyDetails(name.getText().toString());
                Intent intent = new Intent(getApplicationContext(), AddExpenses.class);

                if (cursor.moveToFirst()) {
                    do {
                        quantity = cursor.getInt(2);
                        pricePerQuantity = cursor.getInt(3);
                        dateOfPay = cursor.getInt(4);
                        intent.putExtra("quantity", quantity.toString());
                        intent.putExtra("pricePerQuantity", pricePerQuantity.toString());
                        intent.putExtra("dateOfPay", dateOfPay);
                        intent.putExtra("type", "DAILY");
                        Log.v("settings"+quantity,""+pricePerQuantity);

                    } while (cursor.moveToNext());
                }

                intent.putExtra("buttonValue", "UPDATE");
                intent.putExtra("rowId", internalDB.getRowIdRecurring(name.getText().toString(),"daily"));
                intent.putExtra("daily", name.getText().toString());

                startActivity(intent);
            }
        });

        monthlyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name;
                Integer monthlyDateOfPay;
                Integer monthlyAmount;

                Intent intent = new Intent(getApplicationContext(), AddExpenses.class);

                registerForContextMenu(monthlyList);
                monthlyList.showContextMenu();
                name = (TextView) view.findViewById(R.id.expense);
                Cursor cursor = internalDB.getMonthlyDetails(name.getText().toString());
                if (cursor.moveToFirst()) {
                    do {

                        monthlyDateOfPay = cursor.getInt(3);
                        monthlyAmount = cursor.getInt(2);
                        intent.putExtra("monthlyAmount", monthlyAmount);
                        intent.putExtra("dateOfPay", monthlyDateOfPay);

                    } while (cursor.moveToNext());
                }

                intent.putExtra("buttonValue", "UPDATE");
                intent.putExtra("rowId", internalDB.getRowIdRecurring(name.getText().toString(),"monthly"));
                intent.putExtra("monthly", name.getText().toString());
                intent.putExtra("type", "MONTHLY");

                startActivity(intent);
            }
        });


        monthlyList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name;
                name = (TextView) view.findViewById(R.id.expense);
                showDelete(name.getText().toString(), "monthly");
                return true;
            }
        });
        dailyList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView name;
                name = (TextView) view.findViewById(R.id.expense);
                showDelete(name.getText().toString(), "daily");
                return true;
            }
        });

    }
    public void showDelete(final String name, final String type) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete the item " + name);
        alert.setIcon(R.drawable.ic_action_warning);
        alert.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(type.equals("daily")) {
                    internalDB.deleteRecurringExpenses(name,"daily");
                }
                else {
                    internalDB.deleteRecurringExpenses(name,"monthly");
                }
                finish();
                startActivity(getIntent());
            }
        });
        alert.show();
    }
}
