package com.example.parthdoshi.bmi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by parthdoshi on 14/07/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db;

    DatabaseHandler(Context context) {
        super(context, "ClientDB", null, 1);
        this.context = context;
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table client(datetime string, bmi float)";
        db.execSQL(sql);
    }

    public void insert(String datetime, float bmi)
    {
        ContentValues cv = new ContentValues();
        cv.put("datetime", datetime);
        cv.put("bmi", bmi);
        long rid = db.insert("client", null, cv);
        if (rid < 0)
            Toast.makeText(context, "Insert Failed", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Data Inserted", Toast.LENGTH_SHORT).show();
    }

    public String view()
    {
        Cursor cursor = db.query("client",null,null,null,null,null,null);
        StringBuffer sb = new StringBuffer();
        cursor.moveToFirst();
        if(cursor.getCount() > 0)
        {
            do
            {
                sb.append("At " + cursor.getString(0) + "\nYour BMI was " + cursor.getString(1) + "\n\n");
            }while (cursor.moveToNext());
        }
        return sb.toString();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
