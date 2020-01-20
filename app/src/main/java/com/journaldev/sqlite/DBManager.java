package com.journaldev.sqlite;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class DBManager {

    private DatabaseHelper dbHelper;
    private DbTables dbtables;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbtables = new DbTables(context);
        database = dbtables.getWritableDatabase();
        return this;
    }

    public void close() {
        dbtables.close();
    }

public long insertVal(String tbl,ContentValues flds){

    return database.insert(tbl, null, flds);
}
    public void insert(String name, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, name);
        contentValue.put(DatabaseHelper.DESC, desc);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);

    }

    public Cursor fetch(String tbl,String[] columns,String whr) {
        Cursor cursor = database.query(tbl, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }
public int getCount(String qry){
    Cursor cursor=database.rawQuery(qry,null);
    cursor.close();
    return cursor.getCount();
}
public long getOrInsert(String tbl, String whr, String key, Map<String,String> flds){

    Cursor cursor = null;
    cursor = database.rawQuery("SELECT MAX("+key+") AS cnt FROM "+tbl +" WHERE "+whr, null);
    cursor.moveToFirst();
    long count = cursor.getInt(0);
    cursor.close();
    if(count>0){
        return count;

    }
    else {
        ContentValues contentValue = new ContentValues();
       for(Map.Entry<String,String>entry:flds.entrySet()){
           contentValue.put(entry.getKey(), entry.getValue());

       }
        return insertVal(tbl,contentValue);

    }
}
public String dupBarcode(String pdtid,String[] bcodes){

    Cursor cursor = null;
    String whqry="(";
    for(String code:bcodes){
        whqry+=" bc_code LIKE '"+code+"' OR";

    }
    whqry=whqry.substring(0,whqry.length()-2);
    whqry+=")";
    if(pdtid!=null){
        whqry+=" AND pdt_id="+pdtid;
    }
    cursor = database.rawQuery("SELECT bc_code FROM TBL_MASTER_PRODUCTBARCODE WHERE "+whqry, null);
    cursor.moveToFirst();

    if(cursor.getCount()>0){
        String exist = cursor.getString(0);
        cursor.close();
        return exist +" Barcodes exists in the database";

    }
    else {

        return "";

    }


}
public void insertBcodes(long pdt, String bcodes){
        String[] codes=bcodes.split(",");
    ContentValues contentValue = new ContentValues();

    for (int i=0;i<codes.length;i++) {
        contentValue.put("pdt_id", pdt);
        contentValue.put("bc_code", codes[i]);
        database.replace(DbTables.TBL_BCODE,null,contentValue);
    }
}
public JSONArray  cursorToJson(Cursor cursor){
    JSONArray resultSet = new JSONArray();
    JSONObject returnObj = new JSONObject();

    cursor.moveToFirst();
    while (cursor.isAfterLast() == false) {

        int totalColumn = cursor.getColumnCount();
        JSONObject rowObject = new JSONObject();

        for (int i = 0; i < totalColumn; i++) {
            if (cursor.getColumnName(i) != null) {

                try {

                    if (cursor.getString(i) != null) {
                        rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                    } else {
                        rowObject.put(cursor.getColumnName(i), "");
                    }
                } catch (Exception e) {
                }
            }

        }

        resultSet.put(rowObject);
        cursor.moveToNext();
    }
    return resultSet;
}

}
