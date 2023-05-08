package com.example.noteapplication.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    @ColumnInfo(name = "importance_level")
    public int importanceLevel;
    @ColumnInfo(name = "notification_date")
    public String notificationDate;
    public String title;
    public String description;
}
