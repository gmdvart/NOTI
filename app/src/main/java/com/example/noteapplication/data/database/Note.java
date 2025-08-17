package com.example.noteapplication.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note")
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "importance_level")
    public int importanceLevel;
    @ColumnInfo(name = "notification_date")
    public long notificationDate;
    public String title;
    public String description;
    @ColumnInfo(name = "creation_date")
    public long creationDate;
}
