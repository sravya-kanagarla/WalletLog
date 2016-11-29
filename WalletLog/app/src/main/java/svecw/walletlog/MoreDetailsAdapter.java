package svecw.walletlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MoreDetailsAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<String> categories;
    ArrayList<Integer> amounts;
    ArrayList<Integer> dateOfPays;
    Context context;
    String name,value;
    Integer amount;
    static final int DATE_DIALOG_ID = 0;

    public MoreDetailsAdapter(Context context) {
        categories= new ArrayList<String>();
        amounts = new ArrayList<Integer>();
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(calendar.MONTH) +1;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.payable_bills, null, true);
            viewHolder.name = (TextView) convertView.findViewById(R.id.more);
            viewHolder.dateOfPay = (TextView) convertView.findViewById(R.id.paidDate);
            viewHolder.amount = (EditText) convertView.findViewById(R.id.amount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.paidDateIcon);
        viewHolder.imageView.setImageResource(R.drawable.ic_action_calendar_day);

        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(600);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        viewHolder.imageView.startAnimation(animation);
        viewHolder.name.setText(categories.get(position));
        viewHolder.amount.setText(""+amounts.get(position));

        if (value.equals("total")) {
            String word = "th";
            if ((dateOfPays.get(position) % 10 == 1) && dateOfPays.get(position) != 11)
                word = "st";
            else if ((dateOfPays.get(position) % 10 == 2) && dateOfPays.get(position) != 12)
                word = "nd";
            else if ((dateOfPays.get(position) % 10 == 3) && dateOfPays.get(position) != 13)
                word = "rd";
            viewHolder.dateOfPay.setText(dateOfPays.get(position) +""+ word);
        }
        viewHolder.ref = position;

        viewHolder.amount
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        if (!hasFocus) {
                            String enteredPrice = ((EditText) v).getText().toString();
                            amounts.set(position, Integer.parseInt(enteredPrice));
                        }
                    }
                });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) MoreDetailsAdapter.this.context).showDialog(DATE_DIALOG_ID);
                name = categories.get(position);
                amount = Integer.parseInt(viewHolder.amount.getText().toString());

            }
        });


       return convertView;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView dateOfPay;
        TextView name;
        EditText amount;
        int ref;
    }

    public HashMap<String,Integer> getName() {
        HashMap<String,Integer> values = new HashMap<>();
        values.put(name,amount);
        return values;
    }

    public void getListAddons(ArrayList<String> categories,ArrayList<Integer> amounts, ArrayList<Integer> dateOfPays,String value) {
        this.categories = categories;
        this.amounts = amounts;
        this.dateOfPays = dateOfPays;
        this.value = value;
    }

}
