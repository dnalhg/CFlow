package com.example.cflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {

    private float total = 0;
    private ListView recordsView;
    private TextView totalView;
    private LinkedList<Entry> recordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grabbing the records container and total counter container
        recordsView = findViewById(R.id.records);
        totalView = findViewById(R.id.counter);

        // Setting records to update with new entries
        recordsList = new LinkedList<Entry>();
        final EntryAdapter adapter = new EntryAdapter(this, recordsList);
        recordsView.setAdapter(adapter);

        Consumer<List<Entry>> recordBuilder = new Consumer<List<Entry>>(){
            @Override
            public void accept(List<Entry> l) {
                Log.i("DISPLAYING", l.toString());
                Collections.reverse(l);
                total = adapter.updateData(l);
                refreshTotal(total);
            }
        };

        Flowable<List<Entry>> source = AppDatabase.getInstance(this).entryDao().getEntriesList();
        source.observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordBuilder);

        // Making records updateable
        recordsView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg0) {
                Entry clickedItem = (Entry)adapter.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),EntryActivity.class);
                intent.putExtra("id",Integer.toString(clickedItem.id));
                intent.putExtra("amount",Float.toString(clickedItem.amount));
                intent.putExtra("description",clickedItem.description);
                startActivityForResult(intent,1);
            }
        });
    }

    public void refreshTotal(float total) {
        float rounded_total = round(total*100);
        totalView.setText(String.format("%.2f",rounded_total/100));

        if (rounded_total >= 0) {
            totalView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        } else {
            totalView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
    }

    public void onAdd(View view) {
        Intent intent = new Intent(this, InputForm.class);
        startActivityForResult(intent,1);
    }

    public void onSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
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
                final Entry newEntry = new Entry(description,amountNumber);;

                Completable.fromRunnable(new Runnable(){
                    public void run() {
                        database.entryDao().insert(newEntry);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action() {
                            @Override public void run() {
                                Log.i("INSERTED",newEntry.toString());
                            }
                        });
            }

        }

    }
}
