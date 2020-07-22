package com.example.cflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InputForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_form);

        Intent intent = getIntent();
        if (intent.hasExtra("requestCode")) {
            if (intent.getIntExtra("requestCode",0)==2) {
                EditText amountBox = findViewById(R.id.amount_input);
                EditText descriptionBox = findViewById(R.id.description);

                descriptionBox.setText(intent.getStringExtra("description"));
                amountBox.setText(intent.getStringExtra("amount"));
            }
        }

    }

    public void OnDone(View view) {

        EditText amountBox = findViewById(R.id.amount_input);
        EditText descriptionBox = findViewById(R.id.description);

        String amount = amountBox.getText().toString();
        String description = descriptionBox.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("description", description);
        intent.putExtra("amount", amount);
        setResult(RESULT_OK, intent);
        finish();
    }
}
