package com.riza.example.articuno.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riza.example.articuno.R;

public class DialogHelper {

    public static AlertDialog Loading(Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_loading);
        builder.setCancelable(false);
        return builder.create();

    }

    public static  AlertDialog confirm(Context context, String message,
                                       DialogInterface.OnClickListener yes){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Apa anda yakin?");
        builder.setMessage(message);
        builder.setPositiveButton("Ya", yes );
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();

    }


    public static  AlertDialog addCategory(Context context,
                                           final CategoryAddCallback yes){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tambah Kategori");

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16,16,16,16);
        final EditText edCategory = new EditText(context);
        edCategory.setWidth(layout.getWidth());
        edCategory.setHint("Masukan kategori");
        layout.addView(edCategory);
        builder.setView(layout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                yes.onClickOk(edCategory.getText().toString().trim());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();

    }

    public interface CategoryAddCallback{
        void onClickOk(String category);
    }


}
