package svecw.walletlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class ExpensesAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<String> dailyActivity;
    ArrayList<String> changedList;
    InternalDB internalDB;

    public ExpensesAdapter(Context context) {
        dailyActivity = new ArrayList<>();
        changedList = new ArrayList<>();
        internalDB = new InternalDB(context);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return dailyActivity.size();
    }

    @Override
    public Object getItem(int position) {
        return dailyActivity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout l;

        if (convertView == null)
            l = (LinearLayout) inflater.inflate(R.layout.daily_activities_view,parent,false);
        else
            l = (LinearLayout) convertView;
        CheckBox checkBox = (CheckBox) l.findViewById(R.id.check);
        TextView dailyAmount = (TextView) l.findViewById(R.id.dailyAmount);
        if(changedList.contains(dailyActivity.get(position))) {
            checkBox.setChecked(false);
            checkBox.setText(dailyActivity.get(position));
            dailyAmount.setText("Rs. "+0);
        }

        else {
            checkBox.setChecked(true);
            checkBox.setText(dailyActivity.get(position));
            dailyAmount.setText("Rs. "+internalDB.getDailyAmount(dailyActivity.get(position)));
        }

        return l;
    }
    public void getDailyList(ArrayList<String> dailyActivities,ArrayList<String> changedList) {
        this.dailyActivity = dailyActivities;
        this.changedList = changedList;
    }
}