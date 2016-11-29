package svecw.walletlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
/**
 * Created by ramyateja.karicharla on 26-01-2016.
 */
public class EditExpensesAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<String> expenses;
    public EditExpensesAdapter(Context context) {
        expenses = new ArrayList<>();
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return expenses.size();
    }
    @Override
    public Object getItem(int position) {
        return expenses.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout l;

        if (convertView == null)
            l = (LinearLayout) inflater.inflate(R.layout.edit_expenses_list,parent,false);
        else
            l = (LinearLayout) convertView;
        TextView cate = (TextView) l.findViewById(R.id.expense);

        cate.setText(expenses.get(position));
        return l;
    }
    public void getDetails(ArrayList<String> Activities){
        this.expenses = Activities;
    }
}
