package com.vip.textmemo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.vip.textmemo.model.Memo;
import com.vip.textmemo.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEMO_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, "
                + Constants.KEY_HEAD + " TEXT, "
                + Constants.KEY_TEXT + " TEXT, "
                + Constants.KEY_DATE + " LONG" + ")";

        db.execSQL(CREATE_MEMO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        // creating a table again
        onCreate(db);
    }

    /*
        CRUD Operations begins here
     */

    public void addMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_HEAD, memo.getHeading());
        values.put(Constants.KEY_TEXT, memo.getText());
        values.put(Constants.KEY_DATE, java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);
        Log.d("handlvery", "addMemo: itemadded");
        db.close();

    }

    public Memo getMemo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                Constants.KEY_ID,
                Constants.KEY_HEAD,
                Constants.KEY_TEXT,
                Constants.KEY_DATE},
                Constants.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        Memo memo = new Memo();
        if(cursor != null) {
            memo.setIndex(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
            memo.setHeading(cursor.getString(cursor.getColumnIndex(Constants.KEY_HEAD)));
            memo.setText(cursor.getString(cursor.getColumnIndex(Constants.KEY_TEXT)));

            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))).
                    getTime());

            memo.setFinalDate(formattedDate);
        }
        return memo;
    }

    public List<Memo> getAllMemo() {
        List<Memo> memoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                Constants.KEY_ID,
                Constants.KEY_HEAD,
                Constants.KEY_TEXT,
                Constants.KEY_DATE},
                null, null, null, null,
                Constants.KEY_DATE + " DESC");

        if(cursor.moveToFirst()) {
            do {
                Memo memo = new Memo();
                memo.setIndex(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
                memo.setHeading(cursor.getString(cursor.getColumnIndex(Constants.KEY_HEAD)));
                memo.setText(cursor.getString(cursor.getColumnIndex(Constants.KEY_TEXT)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE)))
                    .getTime());

                memo.setFinalDate(formattedDate);

                // adding to memoList
                memoList.add(memo);
            } while(cursor.moveToNext());
        }
        return memoList;
    }

    public int updateMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_HEAD, memo.getHeading());
        values.put(Constants.KEY_TEXT, memo.getText());
        values.put(Constants.KEY_DATE, java.lang.System.currentTimeMillis());

        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?",
                new String[]{String.valueOf(memo.getIndex())});
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + Constants.TABLE_NAME);
        db.close();
    }

    public void deleteMemo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    public int getCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String SELECT_ALL = "SELECT * FROM " + Constants.TABLE_NAME;
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        return cursor.getCount();
    }
}
