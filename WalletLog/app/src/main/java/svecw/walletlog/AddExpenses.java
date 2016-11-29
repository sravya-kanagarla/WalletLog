package svecw.walletlog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.NumberPicker;

public class AddExpenses extends AppCompatActivity  {
    String daily;
    Integer dateOfPay1;
    String pricePerQuantity;
    String quantity;
    String cat;
    String type;
    String monthly;
    Integer monthlyAmount;
    String expenseType;
    RelativeLayout container;
    int x;
    InternalDB internalDB;
    Button addbtn;
    int numPickValue;
    ImageView numPickBtn;
    TextView dateOfPay;
    EditText name;
    EditText pricePerQty;
    EditText dailyQty;
    int rowID;
    NumberPicker numberPicker;
    TextView dateOfPayVal;

    @Override
    public void onBackPressed() {

        if (type.equals("UPDATE"))
            showAlert("Changes are not saved");
        else {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);
        android.support.v7.app.ActionBar toolbar = getSupportActionBar();
        String buttonValue;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            buttonValue = bundle.getString("buttonValue");
            expenseType = bundle.getString("expenseType");
            daily = bundle.getString("daily");
            monthly = bundle.getString("monthly");
            type = bundle.getString("type");
            quantity = bundle.getString("quantity");
            pricePerQuantity = bundle.getString("pricePerQuantity");
            dateOfPay1 = bundle.getInt("dateOfPay");
            monthlyAmount = bundle.getInt("monthlyAmount");
            rowID = bundle.getInt("rowId");
        } else {
            buttonValue = "ADD";
            daily = null;
            monthly = null;
            cat = null;
            pricePerQuantity = null;
            quantity = null;
            dateOfPay1 = 0;
            rowID = -1;
            type = null;
        }
        internalDB = new InternalDB(this);
        container = (RelativeLayout) findViewById(R.id.categoryLayout);
        addbtn = (Button) findViewById(R.id.addBtn);
        numPickBtn = (ImageView) findViewById(R.id.numPickBtn);
        dateOfPay = (TextView) findViewById(R.id.dateOfPay);
        name = (EditText) findViewById(R.id.name);
        dateOfPayVal = (TextView)findViewById(R.id.dateOfPayVal);
        addbtn.setText(buttonValue);

        if(addbtn.getText().equals("UPDATE")) {
            toolbar.setTitle("Edit");
            x = 0;
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout layoutView = (RelativeLayout) findViewById(R.id.categoryLayout);
            if(daily == null) {
                name.setText(monthly);
                if (layoutView.isEnabled()) {
                    container.removeAllViews();
                }
                layoutView = (RelativeLayout) layoutInflater.inflate(R.layout.add_expense_monthly, null);
                x++;
                container.addView(layoutView);
                EditText Amount = (EditText) findViewById(R.id.monthlyAmount);
                Amount.setText("" + monthlyAmount);
                dateOfPayVal.setText(dateOfPay1.toString());
            }else {
                name.setText(daily);
                if (layoutView.isEnabled()) {
                    container.removeAllViews();
                }
                layoutView = (RelativeLayout) layoutInflater.inflate(R.layout.add_expense_daily, null);
                x++;
                container.addView(layoutView);
                pricePerQty = (EditText) findViewById(R.id.pricePerQty);
                dailyQty = (EditText) findViewById(R.id.dailyQty);
                dateOfPayVal.setText(dateOfPay1.toString());
                pricePerQty.setText("" + pricePerQuantity);
                dailyQty.setText("" + quantity);
            }
        }
        else {
            if (expenseType.equals("DAILY")) {
                addbtn.setText("ADD");
                x = 0;
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout layoutView = (RelativeLayout) findViewById(R.id.categoryLayout);

                if (layoutView.isEnabled()) {
                    container.removeAllViews();
                }
                layoutView = (RelativeLayout) layoutInflater.inflate(R.layout.add_expense_daily, null);
                x++;
                container.addView(layoutView);
            }
            else if (expenseType.equals("MONTHLY")) {
                addbtn.setText("ADD");
                x = 0;
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout layoutView = (RelativeLayout) findViewById(R.id.categoryLayout);

                if (layoutView.isEnabled()) {
                    container.removeAllViews();
                }
                layoutView = (RelativeLayout) layoutInflater.inflate(R.layout.add_expense_monthly, null);
                x++;
                container.addView(layoutView);
            }
        }

        numPickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker();
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addbtn.getText().equals("ADD")) {
                    if (name.getText().toString().equals(null) || name.getText().toString().equals("")) {
                        showAlert("Name cannot be empty");
                    } else if (name.getText().toString().contains("'")) {
                        showAlert("Name cannot contain    '    symbol");
                    } else if (expenseType.equals("DAILY")) {
                        EditText pricePerQty = (EditText) findViewById(R.id.pricePerQty);
                        EditText dailyQty = (EditText) findViewById(R.id.dailyQty);
                        Cursor cursor = internalDB.getDailyDetails(name.getText().toString());
                        if (cursor.getCount() > 0) {
                            showAlert("Name already Exists");
                        } else {
                            try {
                                int price = Integer.parseInt(pricePerQty.getText().toString());
                                int dailyQnty = Integer.parseInt(dailyQty.getText().toString());
                                numPickValue = numberPicker.getValue();
                                internalDB.insertDailyValues(name.getText().toString(), dailyQnty, price, numPickValue);
                                Intent intent = new Intent(getApplicationContext(), EditExpenses.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                showAlert("Enter valid inputs");
                            }
                        }
                    } else if (expenseType.equals("MONTHLY")) {
                        EditText Amount = (EditText) findViewById(R.id.monthlyAmount);
                        Cursor cursor = internalDB.getMonthlyDetails(name.getText().toString());
                        if (cursor.getCount() > 0) {
                            showAlert("Name already Exists");
                        } else {
                            try {
                                int monthlyAmount = Integer.parseInt(Amount.getText().toString());
                                numPickValue = numberPicker.getValue();
                                internalDB.insertMonthlyValues(name.getText().toString(), monthlyAmount, numPickValue);
                                Intent intent = new Intent(getApplicationContext(), EditExpenses.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                showAlert("Enter valid inputs");
                            }
                        }
                    }
                } else {
                    if (name.getText().toString().equals(null) || name.getText().toString().equals("")) {
                        showAlert("Name Cannot be empty");
                    } else if (name.getText().toString().contains("'")) {
                        showAlert("Name cannot contain    '    symbol");
                    }
                    else {
                        if (type.equals("DAILY")) {
                            try{
                                EditText pricePerQty = (EditText) findViewById(R.id.pricePerQty);
                                EditText dailyQty = (EditText) findViewById(R.id.dailyQty);
                                int price = Integer.parseInt(pricePerQty.getText().toString());
                                int dailyQnty = Integer.parseInt(dailyQty.getText().toString());
                                internalDB.updateDailyValues(name.getText().toString(), dailyQnty, price, Integer.parseInt(dateOfPayVal.getText().toString()), rowID);
                                Intent intent = new Intent(getApplicationContext(), EditExpenses.class);
                                startActivity(intent);
                            } catch (Exception e){
                                showAlert("Enter valid inputs");
                            }
                        }else {
                            EditText Amount = (EditText) findViewById(R.id.monthlyAmount);
                            try {
                                int monthlyAmount = Integer.parseInt(Amount.getText().toString());
                                internalDB.updateMonthlyValues(name.getText().toString(), monthlyAmount, Integer.parseInt(dateOfPayVal.getText().toString()), rowID);
                                Intent intent = new Intent(getApplicationContext(), EditExpenses.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                showAlert("Enter valid inputs");
                            }
                        }
                    }
                }
            }
        });
    }

    public void showNumberPicker() {
        final Dialog dialog = new Dialog(AddExpenses.this);
        dialog.setTitle("Select Date: ");
        dialog.setContentView(R.layout.activity_number_picker);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(28);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateOfPayVal.setText(String.valueOf(numberPicker.getValue()));
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void showAlert(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(getIntent());
            }
        });
        alert.show();
    }
}
