package ir.thisisjag.jagshome;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    static DBHelper instance;

    // Table columns
    static final String _ID = "_id";
    static final String IDd = "DeviceID";
    static final String SUBID = "SubID";
    static final String STATUS = "RelayStatus";
    static final String NAME = "RelayName";
    static final String TABLE_NAME = "DEVICES_TABLE";


    public DBHelper(@Nullable Context context) {
        super(context, "devices.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        instance = this;
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + IDd + " INTEGER," + SUBID + " INTEGER," + STATUS + " INTEGER, " + NAME + " TEXT)";

        db.execSQL(createTableStatement);
    }
    public static DBHelper getInstance() {
        return instance;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert(Relays relays) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME,relays.getName());
        cv.put(IDd,relays.getId());
        cv.put(SUBID,relays.getSubId());
        cv.put(STATUS,relays.isState());
        db.insert(TABLE_NAME,null,cv);
        return true;
    }
    public void update(long _id,int idd,  int subid, int sts,String name1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.IDd, idd);
        contentValues.put(DBHelper.SUBID, subid);
        contentValues.put(DBHelper.STATUS, sts);
        contentValues.put(DBHelper.NAME, name1);
        int i = db.update(DBHelper.TABLE_NAME, contentValues, DBHelper._ID + " = " + _id, null);
        return;
    }
    public Relays getAll(){
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        Relays relays = null;
        if(cursor.moveToFirst()){
            do{
                relays = new Relays(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getString(4),cursor.getInt(3));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return relays;
    }
//    public static Relays find(int id, int subid){
//        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + IDd + "= " + id + " AND " + "= " + subid ;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(queryString, null);
//        cursor.moveToFirst();
//        Relays relays = new Relays(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getString(4),cursor.getInt(3));
//        return relays;
//    }
    public boolean delOne(String relays){
        String queryString = "DELETE FROM " + TABLE_NAME + " WHERE " + NAME + "='"+ relays +"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }else{
            cursor.close();
            db.close();
            return false;
        }

        }
    public long getNum(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        long Course = 0;
        if(cursor.moveToFirst())
        Course = cursor.getCount();
        cursor.close();
        db.close();
        return Course;
    }

}
