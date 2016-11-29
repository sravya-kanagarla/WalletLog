package svecw.walletlog; /**
 * Created by ramyateja.karicharla on 03-02-2016.
 */
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private HashMap<Integer, ArrayList<String>> moreDetails;
    private ArrayList<Integer> categories;
    InternalDB internalDB;
    String value;
    String currentDate = null;
    int DetailAmount = 0;
    public ExpandableListAdapter(Activity context, ArrayList<Integer> category, HashMap<Integer, ArrayList<String>> moreDetails,String value,String date) {
        this.context = context;
        this.moreDetails = moreDetails;
        this.categories = category;
        this.value = value;
        this.currentDate = date;
        internalDB = new InternalDB(context);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return moreDetails.get(categories.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String more = (String) getChild(groupPosition, childPosition);
        final LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_list_view_moredetails, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.more);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        //Log.v("aaaa"+more.toString(), "");
        item.setText(more.toString());
        if(value.equals("Total")) {
            DetailAmount = internalDB.getAmountsOfMonth(categories.get(groupPosition), more);
        }
        else if(value.equals("Date")) {
            DetailAmount = internalDB.getAmountsOfDate(categories.get(groupPosition), more);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.editIcon);
            imageView.setImageResource(R.drawable.ic_action_edit_daily_expense);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int rowId = internalDB.getRowIdOneTimeExpenses(categories.get(groupPosition), DetailAmount, more, currentDate);

                    Intent intent = new Intent(context, OneTimeExpenses.class);
                    intent.putExtra("rowID",rowId);
                    intent.putExtra("categoyId",categories.get(groupPosition));
                    intent.putExtra("dateOfPurchase",currentDate);
                    intent.putExtra("amount", DetailAmount);
                    intent.putExtra("description",more);
                    intent.putExtra("action","UPDATE");
                    context.startActivity(intent);
                }
            });
        }
        amount.setText(""+DetailAmount);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return moreDetails.get(categories.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    public int getGroupCount() {
        return categories.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
        int categoryId = (int) getGroup(groupPosition);
        String catergoryName = null;
        Bitmap categoryImage = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.bg_img);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.activity_list_view,null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        TextView category = (TextView) convertView.findViewById(R.id.label);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);

        Cursor cursor = internalDB.getImageName(categoryId);
        if(cursor.moveToFirst()) {
            categoryImage = (BitmapFactory.decodeByteArray(cursor.getBlob(0), 0, cursor.getBlob(0).length));
            catergoryName = cursor.getString(1);
        }
        imageView.setImageBitmap(categoryImage);
        category.setText(catergoryName);
        int totalAmount = 0;
        if(value.equals("Total")) {
            totalAmount = internalDB.getTotalAmountMonth(categoryId, currentDate);
        }
        else if(value.equals("Date")) {
            totalAmount = internalDB.getTotalAmountDate(categoryId,currentDate);

        }
        amount.setText(""+totalAmount);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}