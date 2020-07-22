package com.example.cflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class EntryAdapter extends ArrayAdapter<Entry> {
    public EntryAdapter(Context context, List<Entry> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Entry entry = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entry_layout, parent, false);
        }
        // Lookup view for data population
        TextView listDescription = convertView.findViewById(R.id.listDescription);
        TextView listAmount = convertView.findViewById(R.id.listAmount);

        if (entry.amount >= 0) {
            listAmount.setTextColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimaryDark));
        } else {
            listAmount.setTextColor(ContextCompat.getColor(this.getContext(), R.color.colorAccent));
        }

        // Populate the data into the template view using the data object
        listDescription.setText(entry.description);
        listAmount.setText(Float.toString(entry.amount));
        return convertView;
    }

    public float updateData(List<Entry> list){
        float total = 0;
        this.clear();

        for (Entry e:list) {
            total+=e.amount;
            this.add(e);
        }
        this.notifyDataSetChanged();
        return total;
    }
}
