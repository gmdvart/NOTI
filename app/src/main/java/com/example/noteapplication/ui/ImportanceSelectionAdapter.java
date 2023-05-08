package com.example.noteapplication.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.noteapplication.R;
import com.example.noteapplication.utils.NotiUtils;

public class ImportanceSelectionAdapter extends ArrayAdapter<String> {

    public ImportanceSelectionAdapter(@NonNull Context context, @NonNull String[] objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.importance_list_item, parent, false);
        }
        String importance = getItem(position);
        TextView textView = convertView.findViewById(R.id.note_menu_item_text);
        textView.setText(importance);
        ImageView imageView = convertView.findViewById(R.id.note_menu_item_indicator);
        imageView.setImageResource(NotiUtils.ImportanceSelection.getImageResourceForImportance(importance));

        return convertView;
    }
}
