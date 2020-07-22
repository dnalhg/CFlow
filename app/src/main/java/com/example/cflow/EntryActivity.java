package com.example.cflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class EntryActivity extends AppCompatActivity {

    private Intent parentIntent;
    TextView descriptionBox;
    TextView amountBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        parentIntent = getIntent();

        descriptionBox = findViewById(R.id.entry_description);
        amountBox = findViewById(R.id.entry_amount);

        descriptionBox.setText(parentIntent.getStringExtra("description"));
        amountBox.setText(parentIntent.getStringExtra("amount"));
    }

    public void OnEdit(View view) {

        Intent editIntent = new Intent(this, InputForm.class);
        editIntent.putExtra("requestCode",2);
        editIntent.putExtra("description",parentIntent.getStringExtra("description"));
        editIntent.putExtra("amount",parentIntent.getStringExtra("amount"));
        startActivityForResult(editIntent,2);
    }

    public void OnDelete(View view) {

        ConfirmationDialogEntryDelete confirmationBox = new ConfirmationDialogEntryDelete();

        Entry entry = new Entry(parentIntent.getStringExtra("description"),
                                Float.parseFloat(parentIntent.getStringExtra("amount")));
        entry.setId(Integer.parseInt(parentIntent.getStringExtra("id")));

        confirmationBox.setEntry(entry);
        confirmationBox.show(getSupportFragmentManager(),"Confirmation Dialog");

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                String id = parentIntent.getStringExtra("id");
                String description = data.getStringExtra("description");
                String amount = data.getStringExtra("amount");

                if (description.isEmpty() && amount.isEmpty()) {
                    return;
                }

                final float amountNumber;
                if (!amount.isEmpty()) {
                    amountNumber = Float.parseFloat(amount);
                } else {
                    amountNumber = 0;
                }

                final AppDatabase database = AppDatabase.getInstance(this);
                final Entry newEntry = new Entry(description,amountNumber);
                newEntry.setId(Integer.parseInt(id));

                Completable.fromRunnable(new Runnable(){
                    public void run() {
                        database.entryDao().updateEntry(newEntry);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action() {
                            @Override public void run() {
                                Log.i("UPDATED",newEntry.toString());
                            }
                        });

                descriptionBox.setText(description);
                amountBox.setText(Float.toString(amountNumber));

            }

        }

    }

}
