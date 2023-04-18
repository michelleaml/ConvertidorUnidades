package com.example.convertidorunidades.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.convertidorunidades.Activities.Result;
import com.example.convertidorunidades.Database.DatabaseHelper;
import com.example.convertidorunidades.R;

import java.text.DecimalFormat;

public class Converter extends AppCompatActivity {

    private TextView resultTextView;
    private RadioGroup distanceRadioGroup;

    private RadioGroup convertRadioGroup;

    private EditText inputUser;

    public DatabaseHelper dbHelper;

    Button buttonNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_convertir);
        resultTextView = findViewById(R.id.resultTextView);
        distanceRadioGroup = findViewById(R.id.distanceRadioGroup);
        convertRadioGroup = findViewById(R.id.convertRadioGroup);
        inputUser = findViewById(R.id.inputUser);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Button convertButton = findViewById(R.id.convertButton);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedDistanceRadioButtonId = distanceRadioGroup.getCheckedRadioButtonId();
                String result = "";

                switch (checkedDistanceRadioButtonId) {
                    case R.id.metersRadioButton:
                        result = "Metros";
                        break;
                    case R.id.inchesRadioButton:
                        result = "Pulgadas";
                        break;
                    case R.id.centimetersRadioButton:
                        result = "Centímetros";
                        break;
                    case R.id.feetRadioButton:
                        result = "Pies";
                        break;
                }
                int checkedconvertRadioButtonId = convertRadioGroup.getCheckedRadioButtonId();
                String Convresult = "";
                String userInput = inputUser.getText().toString();
                if (!userInput.matches("-?\\d+(\\.\\d+)?")) {
                    inputUser.setError("Debe ingresar un número válido");
                    return;
                }
                double v1 = Double.parseDouble(userInput);
                double v2 = 0;

                switch (checkedconvertRadioButtonId) {
                    case R.id.metersRadioButton2:
                        Convresult = "Metros";
                        if (result.equals("Pulgadas")) {
                            v2 = v1 * 0.0254;
                        } else if (result.equals("Centímetros")) {
                            v2 = v1 / 100;
                        } else if (result.equals("Pies")) {
                            v2 = v1 * 0.3048;
                        }
                        break;
                    case R.id.inchesRadioButton2:
                        Convresult = "Pulgadas";
                        if (result.equals("Metros")) {
                            v2 = v1 / 0.0254;
                        } else if (result.equals("Centímetros")) {
                            v2 = v1 / 2.54;
                        } else if (result.equals("Pies")) {
                            v2 = v1 * 12;
                        }
                        break;
                    case R.id.centimetersRadioButton2:e:
                    Convresult = "Centímetros";
                        if (result.equals("Metros")) {
                            v2 = v1 * 100;
                        } else if (result.equals("Pulgadas")) {
                            v2 = v1 * 2.54;
                        } else if (result.equals("Pies")) {
                            v2 = v1 * 30.48;
                        }
                        break;
                    case R.id.feetRadioButton2:
                        Convresult = "Pies";
                        if (result.equals("Metros")) {
                            v2 = v1 / 0.3048;
                        } else if (result.equals("Pulgadas")) {
                            v2 = v1 / 12;
                        } else if (result.equals("Centímetros")) {
                            v2 = v1 / 30.48;
                        }
                        break;
                }
                DecimalFormat df = new DecimalFormat("#.00"); // Decimals limited to 2
                dbHelper.addConversion(result, v1, Convresult, Double.parseDouble(df.format(v2)));
                resultTextView.setText(String.format("%s %s  %.2f %s", inputUser.getText().toString(), result, v2, Convresult));
            }
        });
        buttonNext = findViewById(R.id.next);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Converter.this, Result.class);
                startActivity(intent);
            }
        });
    }
}