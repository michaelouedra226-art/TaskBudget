package com.example.taskbudget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB = "taskbudget.db";
    private static final int V = 1;

    public DBHelper(Context c){ super(c, DB, null, V); }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, due TEXT, done INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE transactions (id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, type TEXT, description TEXT, date TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n){
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(db);
    }

    // Tasks
    public long addTask(String title, String due){ ContentValues cv=new ContentValues(); cv.put("title",title); cv.put("due",due); return getWritableDatabase().insert("tasks",null,cv); }
    public List<String[]> getTasks(){ List<String[]> l=new ArrayList<>(); Cursor c=getReadableDatabase().rawQuery("SELECT id,title,due,done FROM tasks ORDER BY id DESC",null); while(c.moveToNext()) l.add(new String[]{String.valueOf(c.getLong(0)),c.getString(1),c.getString(2),String.valueOf(c.getInt(3))}); c.close(); return l; }
    public void toggleTask(long id, boolean done){ ContentValues cv=new ContentValues(); cv.put("done",done?1:0); getWritableDatabase().update("tasks",cv,"id=?",new String[]{String.valueOf(id)}); }
    public void deleteTask(long id){ getWritableDatabase().delete("tasks","id=?",new String[]{String.valueOf(id)}); }

    // Transactions
    public long addTx(double amount, String type, String desc, String date){ ContentValues cv=new ContentValues(); cv.put("amount",amount); cv.put("type",type); cv.put("description",desc); cv.put("date",date); return getWritableDatabase().insert("transactions",null,cv); }
    public List<String[]> getTx(){ List<String[]> l=new ArrayList<>(); Cursor c=getReadableDatabase().rawQuery("SELECT id,amount,type,description,date FROM transactions ORDER BY id DESC",null); while(c.moveToNext()) l.add(new String[]{String.valueOf(c.getLong(0)),String.valueOf(c.getDouble(1)),c.getString(2),c.getString(3),c.getString(4)}); c.close(); return l; }
    public double balance(){ double b=0; Cursor c=getReadableDatabase().rawQuery("SELECT amount,type FROM transactions",null); while(c.moveToNext()){ double a=c.getDouble(0); b += c.getString(1).equals("income")? a : -a; } c.close(); return b; }
}
