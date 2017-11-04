package se.ju.group8.depot;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author max
 * @date 10/30/17.
 */


class DatabaseHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase db = null;

    static SQLiteDatabase getInstance(Context aContext) {
        if (db == null) {
            db = new DatabaseHelper(aContext.getApplicationContext()).getWritableDatabase();
        }
        Log.d("test", "getInstance");
        return db;

    }

    private DatabaseHelper(Context aContext) {
        super(aContext, "entries.db", null, 1);
        Log.d("test","constructor");
    }

    static void init(){
//        db.execSQL("CREATE TABLE IF NOT EXISTS humans (_id INTEGER PRIMARY KEY, name TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS entriesInventory (_id INTEGER PRIMARY KEY, name TEXT NOT NULL, barcode TEXT, dateBought DATE)");
        db.execSQL("CREATE TABLE IF NOT EXISTS entriesWanted (_id INTEGER PRIMARY KEY, name TEXT NOT NULL, barcode TEXT, dateBought DATE)");
        db.execSQL("CREATE TABLE IF NOT EXISTS entriesShopping (_id INTEGER PRIMARY KEY, name TEXT NOT NULL, barcode TEXT, dateBought DATE)");
    }

    static void insert(String query, String[] arguments){
        db.execSQL(
                query,
                arguments
        );
    }

    static Cursor getData(String query){
        return db.rawQuery(
                query,
                new String[]{ }
        );
    }

    public void onCreate(SQLiteDatabase db) {Log.d("test","onCreate");}

    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
    }
}