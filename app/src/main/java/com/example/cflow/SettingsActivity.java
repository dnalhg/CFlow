package com.example.cflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void clearAll(View view) {

        DialogFragment confirmationBox = new ConfirmationDialog();
        confirmationBox.show(getSupportFragmentManager(),"Confirmation Dialog");

    }

}
