package com.mbkm.project1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class AdapterDaftarTask extends BaseAdapter {

    private List<SetterGetterData> sgd;
    private Context context;
    private int id;
    private AlertDialog daftarTask;

    public AdapterDaftarTask(List<SetterGetterData> sgd, Context context, AlertDialog daftarTask) {
        this.sgd = sgd;
        this.context = context;
        this.daftarTask = daftarTask;
    }

    @Override
    public int getCount() {
        return sgd.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.create_new_task, null);

        TextView itemJudul = view.findViewById(R.id.itemJudul);
        ImageView editTask = view.findViewById(R.id.editTask);
        ImageView hapusTask = view.findViewById(R.id.hapusTask);

        final SetterGetterData setterGetterData = sgd.get(position);
        itemJudul.setText(setterGetterData.getJudul());

        //Metode klik untuk melihat task
        itemJudul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("judul", setterGetterData.getJudul());
                i.putExtra("task", setterGetterData.getTask());
                i.putExtra("lihat", true);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                daftarTask.dismiss();
            }
        });

        //metode klik untuk menyunting task, dengan mengirimkan intent ke aktivitas yang sama
        //setelah aktivitas di refresh, mode edit task aktif.
        editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = setterGetterData.getId();
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("id", id);
                i.putExtra("judul", setterGetterData.getJudul());
                i.putExtra("task", setterGetterData.getTask());
                i.putExtra("edit", true);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                daftarTask.dismiss();
            }
        });

        //menghapus task satu-persatu
        hapusTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = setterGetterData.getId();
                DBHelper dbHelper = new DBHelper(context);
                dbHelper.hapusTask(id);
                notifyDataSetChanged();
                dbHelper.close();
                Toast.makeText(context, "Task dihapus", Toast.LENGTH_SHORT).show();
                daftarTask.dismiss();
            }
        });

        return view;
    }
}