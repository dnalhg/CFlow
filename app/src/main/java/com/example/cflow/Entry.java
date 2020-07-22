package com.example.cflow;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "records")
public class Entry {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "amount")
    public float amount;

    public Entry(String description, float amount) {
        this.description = description;
        this.amount = amount;
    }

    public Integer setId(Integer id) {
        this.id = id;
        return id;
    }

    public String toString() {
        return this.description +" "+ Float.toString(this.amount);
    }

}


