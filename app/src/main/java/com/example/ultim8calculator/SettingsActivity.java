package com.example.ultim8calculator;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    EditText[] primaryButtonsColor = new EditText[3], secondaryButtonsColor = new EditText[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        primaryButtonsColor[0] = findViewById(R.id.etxtnum_primary1);
        primaryButtonsColor[1] = findViewById(R.id.etxtnum_primary2);
        primaryButtonsColor[2] = findViewById(R.id.etxtnum_primary3);
        secondaryButtonsColor[0] = findViewById(R.id.etxtnum_secondary1);
        secondaryButtonsColor[1] = findViewById(R.id.etxtnum_secondary2);
        secondaryButtonsColor[2] = findViewById(R.id.etxtnum_secondary3);
    }

    public void switchToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("primaryButtonsColor", makeColorForButtons(primaryButtonsColor));
        intent.putExtra("secondaryButtonsColor", makeColorForButtons(secondaryButtonsColor));
        startActivity(intent);
    }

    private String makeColorForButtons(EditText[] textFields) {
        StringBuilder color = new StringBuilder();
        int nullCount = 0;
        for (int i = 0; i < 3; i++) {
            if (!textFields[i].getText().toString().isEmpty()) {
                if (Integer.parseInt(textFields[i].getText().toString()) > 255) {
                    color.append(String.format("%02x", 255));
                }
                else {
                    color.append(String.format("%02x", Integer.parseInt(textFields[i].getText().toString())));
                }
            }
            else {
                nullCount++;
                color.append("00");
            }
        }
        if (nullCount == 3) {
            return "null";
        }
        else {
            return "#" + color;
        }
    }
}
