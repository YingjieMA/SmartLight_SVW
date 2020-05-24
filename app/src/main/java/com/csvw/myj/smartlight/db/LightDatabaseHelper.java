package com.csvw.myj.smartlight.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.csvw.myj.smartlight.utils.Constants;

/**
 * @ClassName: LightDatabaseHelper
 * @Description:
 * @Author: MYJ
 * @CreateDate: 2020/5/24 14:30
 */
public class LightDatabaseHelper extends SQLiteOpenHelper {
    public LightDatabaseHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSql = "CREATE TABLE "+
                Constants.TABLE_NAME +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(50) ,address INTEGER, type VARCHAR(10),"
                + "state BOOLEAN,online BOOLEAN,diagnose BOOLEAN,"
                + "rValue INTEGER,"+ "gValue INTEGER," + "bValue INTEGER," + "intenseValue INTEGER" +")";
        db.execSQL(createSql);

    }

    private void createLightAttr(SQLiteDatabase db) {

        db.execSQL("INSERT INTO "+Constants.TABLE_NAME+"('name','address','type','state','online','diagnose','rValue','gValue','bValue','intenseValue') VALUES('IP Decor Lamp','rgb','1','1','1','255','255','255','100');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
