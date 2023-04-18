package com.example.convertidorunidades.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.convertidorunidades.Activities.MainActivity;
import com.example.convertidorunidades.Database.DatabaseHelper;
import com.example.convertidorunidades.R;

public class Result extends AppCompatActivity {

    private TextView mConversionTextView;

    Button buttonTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mConversionTextView = findViewById(R.id.conversion_text_view);

        // Create an instance of the database helper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Get a readable database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query the database for the last 5 conversions
        Cursor cursor = db.query("conversions", null, null, null, null, null, "id DESC", "5");

        // Loop through the cursor and build a string with the conversions
        StringBuilder sb = new StringBuilder();
        while (cursor.moveToNext()) {
            String fromUnit = cursor.getString(cursor.getColumnIndexOrThrow("from_unit"));
            double fromValue = cursor.getDouble(cursor.getColumnIndexOrThrow("from_value"));
            String toUnit = cursor.getString(cursor.getColumnIndexOrThrow("to_unit"));
            double toValue = cursor.getDouble(cursor.getColumnIndexOrThrow("to_value"));
            sb.append(fromValue).append(" ").append(fromUnit).append(" = ").append(toValue).append(" ").append(toUnit).append("\n");
        }

        // Display the conversions in the text view
        mConversionTextView.setText(sb.toString());

        // Close the cursor and database
        cursor.close();
        db.close();

        buttonTryAgain = findViewById(R.id.tryAgain);

        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Result.this, Converter.class);
                startActivity(intent);
            }
        });
    }
}