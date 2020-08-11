package ir.thisisjag.jagshome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    static private DBHelper dbHelper;

    private Context context;

    private static SQLiteDatabase database;

    DBManager(Context c) {
        context = c;
    }

    DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public static void close() {
        dbHelper.close();
    }

    static long insert(int j,int idd, int sid,int sts, String name1) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.J, j);
        contentValue.put(DBHelper.IDd, idd);
        contentValue.put(DBHelper.SUBID, sid);
        contentValue.put(DBHelper.STATUS, sts);
        contentValue.put(DBHelper.NAME, name1);
        return database.insert(DBHelper.TABLE_NAME, null, contentValue);
    }
    static long getId(String name){
        Cursor TuplePointer = dbHelper.getReadableDatabase().rawQuery("SELECT _id FROM Devices WHERE name1 = "+name+" ",null);
        TuplePointer.moveToFirst();
        long Course = TuplePointer.getPosition();
        return Course;
    }
    Cursor fetch() {
        String[] columns = new String[] { DBHelper._ID,DBHelper.IDd,DBHelper.J, DBHelper.SUBID,DBHelper.STATUS, DBHelper.NAME };
        Cursor cursor = database.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }



    public int update(long _id,int j,  int idd, int sid,int sts, String name1) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.IDd, idd);
        contentValues.put(DBHelper.J, j);
        contentValues.put(DBHelper.SUBID, sid);
        contentValues.put(DBHelper.STATUS, sts);
        contentValues.put(DBHelper.NAME, name1);
        int i = database.update(DBHelper.TABLE_NAME, contentValues, DBHelper._ID + " = " + _id, null);
        return i;
    }

    void delete(int _id) {

        database.delete(DBHelper.TABLE_NAME, null, null);

    }

}