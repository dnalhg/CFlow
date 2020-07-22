package com.example.cflow;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface EntryDao {

    @Query("SELECT * FROM records")
    Flowable<List<Entry>> getEntriesList();

    @Insert
    void insert(Entry entry);

    @Update
    void updateEntry(Entry entry);

    @Delete
    void delete(Entry entry);

    @Query("DELETE FROM records")
    void deleteAll();



}
