package com.mbkm.project1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //Penentuan nama dan versi database
    public DBHelper(Context context) {
        super(context, "task.db", null, 1);
    }

    //Perintah membuat tabel task dan kolom-kolom untuk atribut task
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE task_table(id INTEGER PRIMARY KEY, judul TEXT, task TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS task_table");
    }

    //Memasukkan data task ke dalam SQLite database
    public boolean masukkanTask(SetterGetterData sgd) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("judul", sgd.getJudul());
        cv.put("task", sgd.getTask());
        return db.insert("task_table", null, cv) > 0;
    }

    //Mendapatkan seluruh data task dari Database
    public Cursor dapatkanSemuaTask() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "task_table", null);
    }

    //Memperbaharui task
    public boolean perbaharuiTask(SetterGetterData sgd, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("judul", sgd.getJudul());
        cv.put("task", sgd.getTask());
        return db.update("task_table", cv, "id" + "=" + id,
                null) > 0;
    }

    //Metode menghapus sebuah task
    public void hapusTask(int id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("task_table", "id" + "=" + id, null);
    }

    //Menghapus seluruh data task
    public void hapusSemuaTask() {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("task_table", null, null);
    }

}