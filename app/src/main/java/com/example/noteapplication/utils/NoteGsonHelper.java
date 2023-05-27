package com.example.noteapplication.utils;

import com.example.noteapplication.data.database.Note;
import com.google.gson.Gson;

public final class NoteGsonHelper {
    public static String noteToJson(Note note) {
        Gson gson = new Gson();
        return gson.toJson(note);
    }

    public static Note jsonToNote(String noteJson) {
        Gson gson = new Gson();
        return gson.fromJson(noteJson, Note.class);
    }
}
