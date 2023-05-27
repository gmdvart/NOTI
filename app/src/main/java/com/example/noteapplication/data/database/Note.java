package com.example.noteapplication.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.noteapplication.ui.NoteDateSelectionIndexSaver;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey
    public int id = 0;
    @ColumnInfo(name = "importance_level")
    public int importanceLevel;
    @ColumnInfo(name = "notification_date")
    public int notificationDate;
    public String title;
    public String description;
    @ColumnInfo(name = "creation_date")
    public int creationDate;
    @Embedded
    public NoteDateSelectionIndexSaver indices;
}
