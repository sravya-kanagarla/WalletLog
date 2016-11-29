package svecw.walletlog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OneTimeExpenses extends AppCompatActivity {
    Button addBtn;
    Spinner category;
    EditText amount;
    EditText description;
    Map<Integer,String> id = new HashMap<>();
    InternalDB internalDB = new InternalDB(this);
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<Integer> cid = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Bitmap> images = new ArrayList<>();
    String dateOfPurchase;
    TextView label;
    String month;
    EditText name;
    String action;
    int rowIDAddons = 0;
    int categoryId = 0;

    public void onBackPressed() {
        if (action.equals("UPDATE"))
            showAlert("Changes are not saved");
        else {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_expenses);
        addBtn = (Button)findViewById(R.id.addBtn);
        category = (Spinner) findViewById(R.id.category);
        amount = (EditText) findViewById(R.id.AddOnsAmount);
        description = (EditText) findViewById(R.id.description);
        label = (TextView) findViewById(R.id.label);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            action = bundle.getString("action");
            if(action.equals("ADD")) {
                dateOfPurchase = bundle.getString("dateOfPurchase");
                month = bundle.getString("month");
                addBtn.setText("ADD");
            }
            else if(action.equals("UPDATE")) {
                addBtn.setText("UPDATE");
                categoryId = bundle.getInt("categoyId");
                rowIDAddons = bundle.getInt("rowID");
                amount.setText("" + bundle.getInt("amount"));
                description.setText(bundle.getString("description"));
                dateOfPurchase = bundle.getString("dateOfPurchase");
            }
        }
        else  {
            action = null;
        }

        id = internalDB.getCategoryIds();
        id  = new TreeMap<>(id);

        for(HashMap.Entry<Integer,String> entry:id.entrySet()){
            cid.add(entry.getKey());
            categories.add(entry.getValue());
        }

        names = internalDB.getCategoryNames(cid);
        images = internalDB.getCategoryImages(cid);


        if (action.equals("ADD")) {
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categories);
            category.setAdapter(adapter);

            category.setAdapter(new MySpinner(this, R.id.category, names));

            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (category.getSelectedItem().toString().equals("Create own category")) {
                        String name = categoryAlert();
                        int spinnerPosition = adapter.getPosition(name);
                        category.setSelection(spinnerPosition);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            for (HashMap.Entry<Integer, String> entry : id.entrySet()) {
                if (category.getSelectedItem().toString().equals(entry.getValue())) {
                    categoryId = entry.getKey();
                }
            }
        }

        if(action.equals("ADD")) {
            addBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (category.getSelectedItem().toString().equals("Create own category")) {
                        showMessage("Please select valid category");
                    }
                    else if(description.getText().toString().contains("'")){
                       showMessage("Description cannot contain    '    symbol");

                    }else if(description.getText().toString().equals(null) || description.getText().toString().equals("")){
                        showMessage("Description cannot be Empty");
                    }
                    else{
                        for (HashMap.Entry<Integer, String> entry : id.entrySet()) {
                            if (category.getSelectedItem().toString().equals(entry.getValue())) {
                                try {
                                    internalDB.insertOneTimeExpenses(entry.getKey(), Integer.parseInt(amount.getText().toString()), description.getText().toString(), dateOfPurchase);
                                    Intent intent = new Intent(getApplicationContext(), Expenses.class);
                                    intent.putExtra("selectedDate", dateOfPurchase);
                                    intent.putExtra("status","clicked");
                                    String date[] = dateOfPurchase.split("/");
                                    intent.putExtra("day", Integer.parseInt(date[0]));
                                    intent.putExtra("month", Integer.parseInt(date[1]));
                                    intent.putExtra("year",Integer.parseInt(date[2]));
                                    startActivity(intent);
                                } catch (Exception np) {
                                    showMessage("Enter valid inputs");
                                }
                            }
                        }
                    }
                }
            });
        }
        else {
            category.setEnabled(false);
            android.support.v7.app.ActionBar toolbar = getSupportActionBar();
            toolbar.setTitle("EDIT");
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        internalDB.updateOneTimeExpenses(Integer.parseInt(amount.getText().toString()), description.getText().toString(), dateOfPurchase, rowIDAddons);
                        Intent intent = new Intent(getApplicationContext(), Expenses.class);
                        intent.putExtra("selectedDate", dateOfPurchase);
                        intent.putExtra("status","clicked");
                        String date[] = dateOfPurchase.split("/");
                        intent.putExtra("day", Integer.parseInt(date[0]));
                        intent.putExtra("month",Integer.parseInt(date[1]));
                        intent.putExtra("year",Integer.parseInt(date[2]));
                        startActivity(intent);
                    }
                    catch (Exception np) {
                        showMessage("Enter valid inputs");

                    }
                }
            });
        }
    }

    public void showMessage(String message) {
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
    public void showAlert(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), Expenses.class);
                intent.putExtra("status", "clicked");
                String[] divide = dateOfPurchase.split("/");
                intent.putExtra("selectedDate", dateOfPurchase);
                intent.putExtra("day", divide[0]);
                intent.putExtra("month", divide[1]);
                intent.putExtra("year", divide[2]);
                startActivity(intent);
            }
        });
        alert.show();
    }
    public String categoryAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add your own category...");
        name = new EditText(this);
        final View v = new View(this);
        alert.setView(name);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor cursor = internalDB.getNames(name.getText().toString());
                if(cursor.getCount() > 0) {
                    showMessage("Name already exists");
                }
                else {
                    internalDB.insertUserCategory(name.getText().toString(), v.getContext());
                    finish();
                    startActivity(getIntent());
                }

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
        return name.getText().toString();
    }
    public class MySpinner extends ArrayAdapter<String> {
        LayoutInflater inflater;

        public MySpinner(Context context, int textViewId, ArrayList<String> objects) {
            super(context, textViewId, objects);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.activity_list_view, parent, false);
            TextView label = (TextView) row.findViewById(R.id.label);
            ImageView icon = (ImageView) row.findViewById(R.id.image);
            label.setText(names.get(position));
            icon.setImageBitmap(images.get(position));
            return row;
        }
    }
}
