package com.example.convertidorunidades.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "conversions.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    private static final String TABLE_NAME = "conversions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FROM_UNIT = "from_unit";
    private static final String COLUMN_FROM_VALUE = "from_value";
    private static final String COLUMN_TO_UNIT = "to_unit";
    private static final String COLUMN_TO_VALUE = "to_value";

    private static final String COLUMN_TO_RESULT = "to_result";
    // SQL statement for creating the table
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FROM_UNIT + " TEXT, " +
                    COLUMN_FROM_VALUE + " REAL, " +
                    COLUMN_TO_UNIT + " TEXT, " +
                    COLUMN_TO_VALUE + " REAL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement if needed
    }

    public void addConversion(String fromUnit, double fromValue, String toUnit, double toValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FROM_UNIT, fromUnit);
        values.put(COLUMN_FROM_VALUE, fromValue);
        values.put(COLUMN_TO_UNIT, toUnit);
        values.put(COLUMN_TO_VALUE, toValue);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
}