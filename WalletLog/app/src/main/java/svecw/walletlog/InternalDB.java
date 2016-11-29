package svecw.walletlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class  InternalDB extends SQLiteOpenHelper {

    public InternalDB(Context context) {
        super(context, "WALLETLOG_DB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String category = "CREATE TABLE category(categoryId INTEGER PRIMARY KEY,category TEXT,image blob)";
        db.execSQL(category);

        String oneTimeExpenses = "CREATE TABLE oneTimeExpenses(Id INTEGER,amount INTEGER, description TEXT, dateOfPurchase TEXT, FOREIGN KEY (Id) REFERENCES categorytb(categoryId))";
        db.execSQL(oneTimeExpenses);

        String dailyRecurring = "CREATE TABLE dailyRecurring (dailyId INTEGER PRIMARY KEY, name TEXT, defaultQty INTEGER, defaultPrice INTEGER, dateOfPay INTEGER)";
        db.execSQL(dailyRecurring);

        String monthlyRecurring = "CREATE TABLE monthlyRecurring (monthlyId INTEGER PRIMARY KEY, name TEXT, amount INTEGER, dateOfPay INTEGER)";
        db.execSQL(monthlyRecurring);

        String dailyChanges = "CREATE TABLE dailyChanges (date TEXT,name TEXT)";
        db.execSQL(dailyChanges);

        String paidBills = "CREATE TABLE paidBills(name TEXT, type TEXT, amount INTEGER, paidDate TEXT)";
        db.execSQL(paidBills);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String category = "DROP TABLE IF EXISTS category";
        db.execSQL(category);
        onCreate(db);
        String oneTimeExpenses = "DROP TABLE IF EXISTS oneTimeExpenses";
        db.execSQL(oneTimeExpenses);
        onCreate(db);
        String dailyRecurring = "DROP TABLE IF EXISTS dailyRecurring";
        db.execSQL(dailyRecurring);
        onCreate(db);
        String monthlyRecurring = "DROP TABLE IF EXISTS monthlyRecurring";
        db.execSQL(monthlyRecurring);
        onCreate(db);
        String dailyChanges = "DROP TABLE IF EXISTS dailyChanges";
        db.execSQL(dailyChanges);
        onCreate(db);
        String paidBills = "DROP TABLE IF EXISTS paidBills";
        db.execSQL(paidBills);
        onCreate(db);
    }

    public void insertDailyValues(String name, int defaultQty, int defaultPrice, int dateOfPay) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("defaultQty", defaultQty);
        contentValues.put("defaultPrice", defaultPrice);
        contentValues.put("dateOfPay", dateOfPay);
        sqLiteDatabase.insert("dailyRecurring", null, contentValues);
    }

    public void insertMonthlyValues(String name, int amount, int dateOfPay) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("amount", amount);
        contentValues.put("dateOfPay", dateOfPay);
        sqLiteDatabase.insert("monthlyRecurring", null, contentValues);

    }


    public void insertCategories(Context context) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("category", "Travelling");
        ByteArrayOutputStream travellingBAOS=new ByteArrayOutputStream();
        Bitmap travellingImage = BitmapFactory.decodeResource(context.getResources(), R.mipmap.trav);
        travellingImage.compress(Bitmap.CompressFormat.PNG, 5, travellingBAOS);
        contentValues.put("image", travellingBAOS.toByteArray());
        sqLiteDatabase.insert("category", null, contentValues);

        contentValues.put("category", "Shopping");
        ByteArrayOutputStream shoppingBAOS=new ByteArrayOutputStream();
        Bitmap shoppingImage = BitmapFactory.decodeResource(context.getResources(), R.mipmap.shop);
        shoppingImage.compress(Bitmap.CompressFormat.PNG, 5, shoppingBAOS);
        contentValues.put("image", shoppingBAOS.toByteArray());
        sqLiteDatabase.insert("category", null, contentValues);

        contentValues.put("category", "Grocery");
        ByteArrayOutputStream groceryBAOS =new ByteArrayOutputStream();
        Bitmap groceryImage = BitmapFactory.decodeResource(context.getResources(), R.mipmap.groc);
        groceryImage.compress(Bitmap.CompressFormat.PNG,5, groceryBAOS);
        contentValues.put("image", groceryBAOS.toByteArray());
        sqLiteDatabase.insert("category", null, contentValues);

        contentValues.put("category", "Food");
        ByteArrayOutputStream foodBAOS=new ByteArrayOutputStream();
        Bitmap foodImage = BitmapFactory.decodeResource(context.getResources(), R.mipmap.food);
        foodImage.compress(Bitmap.CompressFormat.PNG, 5, foodBAOS);
        contentValues.put("image", foodBAOS.toByteArray());
        sqLiteDatabase.insert("category", null, contentValues);

        contentValues.put("category", "Other expenses");
        ByteArrayOutputStream oterBAOS=new ByteArrayOutputStream();
        Bitmap otherImage = BitmapFactory.decodeResource(context.getResources(), R.mipmap.oth);
        otherImage.compress(Bitmap.CompressFormat.PNG, 5, oterBAOS);
        contentValues.put("image", oterBAOS.toByteArray());
        sqLiteDatabase.insert("category", null, contentValues);

       contentValues.put("category", "Create own category");
        ByteArrayOutputStream ownBAOS=new ByteArrayOutputStream();
        Bitmap ownImage = BitmapFactory.decodeResource(context.getResources(), R.mipmap.own);
        ownImage.compress(Bitmap.CompressFormat.PNG, 5, ownBAOS);
        contentValues.put("image", ownBAOS.toByteArray());
        sqLiteDatabase.insert("category", null, contentValues);

    }

      public void insertData(String date,String name) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("date", date);
        contentValues.put("name", name);

        sqLiteDatabase.insert("dailyChanges", null, contentValues);
        sqLiteDatabase.close();
    }

    public boolean isExistscategory() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String readQuery = "SELECT * FROM category";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        if(cursor.getCount() > 0) {
            return false;
        }
        return true;
    }

    public HashMap<Integer,String> getCategoryIds() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        HashMap<Integer,String> ids = new HashMap<Integer,String>();
        String readQuery = "SELECT * FROM category";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        if(cursor.moveToFirst()) {
            do {
                ids.put(cursor.getInt(0), cursor.getString(1));
            }while(cursor.moveToNext());
        }
        return  ids;
    }

    public Cursor getNames(String name) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String readQuery = "SELECT * FROM category where category = \'"+name+"\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        return cursor;
    }

    public Cursor getDailyList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String readQuery = "SELECT * FROM dailyRecurring";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        return cursor;
    }

    public ArrayList<String> getDailyChanges(String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> changedList =  new ArrayList<String>();
        String readQuery = "SELECT * FROM dailyChanges where date=\'"+date+"\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        if(cursor.moveToFirst()) {
            do {
                changedList.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        return changedList;
    }

    public int noOfUnchecks(int month,String name){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int unchecks=0;
        String readQuery = "Select count(name) from dailyChanges where date like \'%/"+month+"/%\' and name=\'"+name+"\' group by name";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);

        if(cursor.moveToFirst()) {
            unchecks=cursor.getInt(0);
        }
        return unchecks;
    }

    public void updateOneTimeExpenses(int amount,String description, String dateOfPurchase,int rowId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =  new ContentValues();
        contentValues.put("amount", amount);
        contentValues.put("description", description);
        contentValues.put("dateOfPurchase", dateOfPurchase);
        sqLiteDatabase.update("oneTimeExpenses", contentValues, "rowid=" + rowId, null);
    }

    public void insertOneTimeExpenses(int c_id,int amount,String description, String dateOfPurchase) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("Id", c_id);
        contentValues.put("amount", amount);
        contentValues.put("description", description);
        contentValues.put("dateOfPurchase", dateOfPurchase);

        sqLiteDatabase.insert("oneTimeExpenses", null, contentValues);
        sqLiteDatabase.close();
    }

    public  HashMap<Integer,Integer> getOneTimeExpensesOfDate(String selectedDate) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        HashMap<Integer,Integer> addOns = new HashMap<Integer,Integer>();
        String readQuery = "SELECT Id,SUM(amount) FROM oneTimeExpenses where dateOfPurchase = \'" + selectedDate + "\' GROUP BY (Id)";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);

        if(cursor.moveToFirst()) {
            do {
                addOns.put(cursor.getInt(0),cursor.getInt(1));
            } while (cursor.moveToNext());
        }
        return addOns;
    }

    public Cursor getMonthlyDetails() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String readQuery = "SELECT * FROM monthlyRecurring";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);

        return cursor;
    }
    public void insertUserCategory(String newCategory,Context context) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();

        contentValues.put("category", newCategory);
        Bitmap ownImage = BitmapFactory.decodeResource(context.getResources(), R.mipmap.own);
        ownImage.compress(Bitmap.CompressFormat.PNG, 5, baos);
        contentValues.put("image",baos.toByteArray());
        sqLiteDatabase.insert("category", null, contentValues);

    }

    public ArrayList<String> getCategoryNames(ArrayList<Integer> cids) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < cids.size(); i++) {
            String readQuery = "SELECT category FROM category where categoryId = "+cids.get(i)+"";
            Cursor cursor =  sqLiteDatabase.rawQuery(readQuery, null);
            if(cursor.moveToFirst()) {
                do {
                    names.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }
        return  names;
    }

    public int getDailyAmount(String name){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int amount = 0;
        String readQuery = "SELECT defaultQty,defaultPrice FROM dailyRecurring where name = \'"+name+"\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        if(cursor.moveToFirst()) {
            amount = cursor.getInt(0) * cursor.getInt(1);
        }
        return amount;
    }

    public ArrayList<Bitmap> getCategoryImages(ArrayList<Integer> cids) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Bitmap> images = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            String readQuery = "SELECT image FROM category where categoryId = "+cids.get(i)+"";
            Cursor cursor =  sqLiteDatabase.rawQuery(readQuery, null);
            if(cursor.moveToFirst()) {
                do {
                    images.add(BitmapFactory.decodeByteArray(cursor.getBlob(0), 0, cursor.getBlob(0).length));
                } while (cursor.moveToNext());
            }
        }
        return  images;
    }

    public int getTotalAmount(String date,String type) {
        String[] divide = date.split("/");
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int total = 0;
        String readQuery =  null;
        if(type.equals("total")) {
            readQuery= "SELECT SUM(amount) FROM oneTimeExpenses where dateOfPurchase LIKE " + "\'%/" + divide[1] + "/" + divide[2] + "\'";
        }
        else if (type.equals("daily")){
            readQuery = "SELECT SUM(amount) FROM oneTimeExpenses where dateOfPurchase = \'"+date+"\'";
        }
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);
        cursor.moveToFirst();
        total = cursor.getInt(0);
        return total;
    }

    public  HashMap<Integer,Integer> getOneTimeExpensesOfMonth(String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        HashMap<Integer,Integer> oneTimeExpenses = new HashMap<Integer,Integer>();
        String[] divide = date.split("/");
        String readQuery = "SELECT Id,SUM(amount) FROM oneTimeExpenses where dateOfPurchase LIKE " + "\'%/" + divide[1] + "/" + divide[2] + "\' GROUP BY (Id)";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);

        if(cursor.moveToFirst()) {
            do {
                oneTimeExpenses.put(cursor.getInt(0),cursor.getInt(1));
            } while (cursor.moveToNext());
        }
        return oneTimeExpenses;
    }


    public Integer getIdByName(String name) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Integer cid=0;
        String readQuery = "SELECT * FROM category where category = \'" + name + "\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);
        cursor.moveToFirst();
        cid = cursor.getInt(0);
        return cid;
    }

    public Cursor getImageName(Integer cid) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String readQuery = "SELECT image, category FROM category where categoryId ="+cid+"";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);
        return  cursor;
    }

    public int getTotalAmountMonth(Integer cid,String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int total = 0;
        String[] divide = date.split("/");
        String readQuery = "SELECT SUM(amount) FROM oneTimeExpenses where dateOfPurchase LIKE " + "\'%/" + divide[1] + "/" + divide[2] + "\' AND Id = "+cid+"";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);
        if(cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        return  total;
    }

    public int getTotalAmountDate(Integer cid,String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int total = 0;
        String readQuery = "SELECT SUM(amount) FROM oneTimeExpenses where dateOfPurchase = \'"+date+"\' AND Id = "+cid+"";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);
        if(cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        return  total;
    }

    public ArrayList<String> getMoreDetailsMonth(Integer cid,String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] divide = date.split("/");
        ArrayList<String> dates = new ArrayList<>();
        String readQuery = "SELECT DISTINCT dateOfPurchase FROM oneTimeExpenses where Id = "+cid+" AND dateOfPurchase LIKE " + "\'%/" + divide[1] + "/" + divide[2] + "\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);

        if(cursor.moveToFirst()) {
            do {
                dates.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return dates;
    }

    public ArrayList<String> getMoreDetailsDate(Integer cid,String date){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> desc = new ArrayList<>();
        String readQuery = "SELECT description FROM oneTimeExpenses where Id = "+cid+" AND dateOfPurchase =\'"+date+"\'";
        Log.v("queryy",readQuery);
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);
        if(cursor.moveToFirst()) {
            do {
                desc.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return desc;
    }

    public int getAmountsOfMonth(Integer cid,String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int amount = 0;
        String readQuery = "SELECT sum(amount) FROM oneTimeExpenses where Id ="+cid+" AND dateOfPurchase =\'"+date+"\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);
        if(cursor.moveToFirst()) {
            amount = cursor.getInt(0);
        }
        return amount;
    }

    public int getAmountsOfDate(Integer cid,String description) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int amount = 0;
        String readQuery = "SELECT amount FROM oneTimeExpenses where Id ="+cid+" AND description =\'"+description+"\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);
        if(cursor.moveToFirst()) {
            amount = cursor.getInt(0);
        }
        return amount;
    }

    public void removeChanges(String date,String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String readQuery = "DELETE FROM dailyChanges where date=\'"+date+"\' AND name=\'"+name+"\'";
        sqLiteDatabase.execSQL(readQuery);
    }

    public ArrayList<Integer> getAllMonthsTotal(int year) {
        int i;
        ArrayList<Integer> totals = new ArrayList<>();
        for (i = 1; i <= 12; i++) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            String readQuery = "SELECT SUM(amount) FROM oneTimeExpenses where dateOfPurchase LIKE " + "\'%/"+i+"/"+year+"\'";
            Cursor cursor = sqLiteDatabase.rawQuery(readQuery,null);
            if (cursor.moveToFirst()){
                do {
                  totals.add(cursor.getInt(0));
                }while (cursor.moveToNext());
            }
        }
        return  totals;
    }

    public void deleteOneTimeExpenses(int id, String description, int amount, String dateOfPurchase) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String readQuery = "DELETE FROM oneTimeExpenses where rowid in (SELECT MIN(rowid) FROM oneTimeExpenses where Id = " + id + " and description = \'"+description+"\' and amount = " + amount + " and dateOfPurchase = '" + dateOfPurchase + "')";
        sqLiteDatabase.execSQL(readQuery);
    }


    public void deleteRecurringExpenses(String name,String type){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String readQuery = null;
        if(type.equals("monthly")) {
            readQuery = "DELETE FROM monthlyRecurring where name = \'" + name + "\'";
        }
        else if(type.equals("daily")){
            readQuery = "DELETE FROM dailyRecurring where name = \'" +name+ "\'";
        }
        sqLiteDatabase.execSQL(readQuery);
    }

    public void deleteBill(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String readQuery = "DELETE FROM paidBills where rowid ="+id ;
        sqLiteDatabase.execSQL(readQuery);
    }


    public ArrayList<Integer> getCategoryTotal(Integer categoryId, int year) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ArrayList<Integer> totals = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String readQuery = "SELECT SUM(amount) FROM oneTimeExpenses where Id = "+categoryId+" and dateOfPurchase LIKE " + "\'%/" + i + "/" + year + "\'";
            Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    totals.add(cursor.getInt(0));
                }while (cursor.moveToNext());
            }

        }
        return totals;
    }

    public Cursor getDailyDetails(String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String readQuery = "SELECT * FROM dailyRecurring WHERE name = '" + name + "\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);

        return cursor;
    }

    public void insertPaidBills(String name,String type,int amount,String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("type",type);
        contentValues.put("amount",amount);
        contentValues.put("paidDate",date);
        sqLiteDatabase.insert("paidBills", null, contentValues);
        sqLiteDatabase.close();
    }

    public Cursor getPaidBills(String type,String date,String value){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] divide = date.split("/");
        String readQuery = null;
        if(value.equals("Total")) {
            readQuery = "SELECT * FROM paidBills where type = \'"+type+"\' AND paidDate LIKE \'%/"+divide[1]+"/"+divide[2]+"\'";
        }
        else if(value.equals("date")) {
            readQuery = "SELECT * FROM paidBills where type = \'"+type+"\' AND paidDate LIKE \'"+date+"\'";

        }
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        return cursor;
    }

    public void updateDailyValues(String name, int defaultQty, int defaultPrice, int dateOfPay,int rowId) {
        SQLiteDatabase  sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =  new ContentValues();
        contentValues.put("name",name);
        contentValues.put("dateOfPay",dateOfPay);
        contentValues.put("defaultQty",defaultQty);
        contentValues.put("defaultPrice",defaultPrice);
        sqLiteDatabase.update("dailyRecurring",contentValues,"rowid="+rowId,null);
    }

    public void updateMonthlyValues(String name, int amount, int dateOfPay,int rowId) {
        SQLiteDatabase  sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =  new ContentValues();
        contentValues.put("name",name);
        contentValues.put("dateOfPay",dateOfPay);
        contentValues.put("amount", amount);
        sqLiteDatabase.update("monthlyRecurring",contentValues,"rowid="+rowId,null);
    }

    public Cursor getMonthlyDetails(String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String readQuery = "SELECT * FROM monthlyRecurring WHERE name = \'" + name + "\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);

        return cursor;
    }


    public int getRowIdRecurring(String name,String type) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int rowID = 0;
        String readQuery =null;
        if(type.equals("monthly")){
           readQuery = "SELECT rowid FROM monthlyRecurring WHERE name = \'"+name+"\'";
        }
        else if(type.equals("daily")){
            readQuery = "SELECT rowid FROM dailyRecurring WHERE name = \'"+name+"\'";
        }

        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }

        return  rowID;
    }

    public int getRowIdBills(String name,String billAmount, String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int rowID = 0;
        String readQuery = null;
        readQuery = "SELECT rowid FROM paidBills WHERE name = \'"+name+"\' AND amount = \'"+billAmount+"\' AND paidDate = \'"+date+"\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }
        return  rowID;
    }

    public int getRowIdOneTimeExpenses(int c_id,int amount,String description,String dateOfPurchase) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int rowID = 0;
        String readQuery = "SELECT rowid FROM oneTimeExpenses WHERE Id = "+c_id+" AND amount = "+amount+" AND description = \'"+description+"\' AND dateOfPurchase = \'"+dateOfPurchase+"\'";
        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        if(cursor.moveToFirst()){
            rowID = cursor.getInt(0);
        }
        return  rowID;
    }

    public int getPaidBillsTotal(String date,String type) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int amount = 0;
        String[] divide = date.split("/");
        String readQuery = null;
        if(type.equals("date")) {
            readQuery = "SELECT SUM(amount) FROM paidBills WHERE paidDate = \'"+date+"\'";
        }
        else if(type.equals("total")){
            readQuery = "SELECT SUM(amount) FROM paidBills WHERE paidDate LIKE \'%/"+divide[1]+"/"+divide[2]+"\'";
        }        Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
        if(cursor.moveToFirst()) {
            amount = cursor.getInt(0);
        }
        return amount;
    }
    public ArrayList<Integer> getGraphPaidBills(int year) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ArrayList<Integer> totals = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String readQuery = "SELECT SUM(amount) FROM paidBills where paidDate LIKE " + "\'%/" + i + "/" + year + "\'";
            Cursor cursor = sqLiteDatabase.rawQuery(readQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    totals.add(cursor.getInt(0));
                }while (cursor.moveToNext());
            }
        }
        return totals;
    }

}