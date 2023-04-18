package com.example.convertidorunidades.activities.;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.convertidorunidades.R;

public class Convertir extends AppCompatActivity {

    private TextView resultTextView;
    private RadioGroup distanceRadioGroup;

    private RadioGroup convertRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        distanceRadioGroup = findViewById(R.id.distanceRadioGroup);
        convertRadioGroup = findViewById(R.id.convertRadioGroup);

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

                resultTextView.setText(result);
            }
        });
    }
}
