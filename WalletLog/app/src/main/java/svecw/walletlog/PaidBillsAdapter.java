package svecw.walletlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class PaidBillsAdapter extends BaseAdapter {

    ArrayList<String> dates;
    ArrayList<String> names;
    ArrayList<Integer> amounts;
    LayoutInflater inflater;
    String value;
    public PaidBillsAdapter(Context context){
        dates = new ArrayList<>();
        names = new ArrayList<>();
        amounts = new ArrayList<>();
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout l;

        if (convertView == null)
            l = (LinearLayout) inflater.inflate(R.layout.paid_bills,parent,false);
        else
            l = (LinearLayout) convertView;

        TextView date = (TextView)l.findViewById(R.id.date);
        TextView name = (TextView)l.findViewById(R.id.name);
        TextView amount = (TextView)l.findViewById(R.id.amount);
        if(value.equals("Total")) {
            date.setText(dates.get(position));
            name.setText(names.get(position));
            amount.setText(""+amounts.get(position));
        }
        else if(value.equals("date")) {
            name.setText(names.get(position));
            amount.setText(""+amounts.get(position));
        }
        return l;
    }

    public void getPaidBills(ArrayList<String> dates,ArrayList<String> names,ArrayList<Integer> amounts,String value) {
        this.dates = dates;
        this.names = names;
        this.amounts = amounts;
        this.value = value;
    }
}
