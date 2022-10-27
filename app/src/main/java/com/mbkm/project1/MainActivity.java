package com.mbkm.project1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Variabel global
    private TextView simpanTask;
    private EditText judulTask;
    private EditText isiTask;
    private Button batal;
    private boolean edit = false;
    private int id = 0;
    private RelativeLayout layoutTask;
    private LinearLayout layoutUtama;
    private FloatingActionButton btn_add;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_1);

        //View id dari seluruh komponen pada layout
        judulTask = findViewById(R.id.judulTask);
        isiTask = findViewById(R.id.isiTask);
        simpanTask = findViewById(R.id.simpanTask);
        batal = findViewById(R.id.batal);
        layoutTask = findViewById(R.id.layoutTask);
//        btn_add = findViewById(R.id.btn_add);
        layoutUtama = findViewById(R.id.layoutUtama);

        //Local variabel dan view id
        TextView buatTaskBaru = findViewById(R.id.buatTaskBaru);
        TextView daftarTask = findViewById(R.id.daftarTask);


        //Menerima intent yang dikirimkan oleh tombol edit list.
        //jika nilai boolean edit adalah true maka mode sunting task aktif.
        Intent i = getIntent();
        edit = i.getBooleanExtra("edit", false);
        if (edit) {
            layoutUtama.setVisibility(View.GONE);
            layoutTask.setVisibility(View.VISIBLE);
        }

        //Mode untuk melihat task
        //Aktif saat judul task pada daftar task di klik.
        boolean lihat = i.getBooleanExtra("lihat", false);
        if (lihat) {
            simpanTask.setVisibility(View.INVISIBLE);
            batal.setText("Kembali");
            layoutUtama.setVisibility(View.GONE);
            layoutTask.setVisibility(View.VISIBLE);
        }
        judulTask.setText(i.getStringExtra("judul"));
        isiTask.setText(i.getStringExtra("task"));
        id = i.getIntExtra("id", 0);


        //Metode klik untuk tombol menyimpan task
        simpanTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (judulTask.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Judul task tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SetterGetterData sgd = new SetterGetterData();
                sgd.setJudul(judulTask.getText().toString());
                sgd.setTask(isiTask.getText().toString());

                boolean masukkanTask;
                if (edit) {
                    masukkanTask = dbHelper.perbaharuiTask(sgd, id);
                } else {
                    masukkanTask = dbHelper.masukkanTask(sgd);
                }

                if (masukkanTask) {
                    Toast.makeText(getApplicationContext(), "Task berhasil disimpan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Kesalahan terjadi!", Toast.LENGTH_SHORT).show();
                }
                dbHelper.close();

                judulTask.getText().clear();
                isiTask.getText().clear();
                layoutUtama.setVisibility(View.VISIBLE);
                layoutTask.setVisibility(View.GONE);
            }
        });

//        btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                simpanTask.setVisibility(View.VISIBLE);
//                batal.setVisibility(View.VISIBLE);
//                batal.setText("Batal");
//                layoutUtama.setVisibility(View.GONE);
//                layoutTask.setVisibility(View.VISIBLE);
//            }
//        });

        //Metode klik untuk tombol membuat task baru
        buatTaskBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanTask.setVisibility(View.VISIBLE);
                batal.setVisibility(View.VISIBLE);
                batal.setText("Batal");
                layoutUtama.setVisibility(View.GONE);
                layoutTask.setVisibility(View.VISIBLE);
            }
        });

        //Metode klik untuk tombol melihat daftar task
        daftarTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDaftarTask();
            }
        });


        //Metode klik untuk tombol membatalkan pembuatan atau penyuntingan task
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judulTask.getText().clear();
                isiTask.getText().clear();
                layoutUtama.setVisibility(View.VISIBLE);
                layoutTask.setVisibility(View.GONE);
                edit = false;
            }
        });
    }

    //Dialog untuk menampilkan daftar task
    @SuppressLint("SetTextI18n")
    private void dialogDaftarTask() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.view_daftar_task, null);
        TextView hapusSemuaTask = v.findViewById(R.id.hapusSemuaTask);
        TextView judulDaftar = v.findViewById(R.id.judulDaftar);
        b.setView(v);

        ArrayList<SetterGetterData> setterGetterData = new ArrayList<>();
        ListView listDaftar = v.findViewById(R.id.listDaftar);
        final DBHelper dh = new DBHelper(getApplicationContext());
        Cursor cursor = dh.dapatkanSemuaTask();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                SetterGetterData sgd = new SetterGetterData();
                sgd.setId((cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
                sgd.setJudul((cursor.getString(cursor.getColumnIndexOrThrow("judul"))));
                sgd.setTask((cursor.getString(cursor.getColumnIndexOrThrow("task"))));
                setterGetterData.add(sgd);
                cursor.moveToNext();
            }
            dh.close();
        }

        final AlertDialog daftarTask = b.create();if (daftarTask.getWindow() !=null) {
            daftarTask.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        AdapterDaftarTask adapterDaftarTask = new AdapterDaftarTask(setterGetterData, getApplicationContext(),
                daftarTask);
        listDaftar.setAdapter(adapterDaftarTask);

        if (listDaftar.getAdapter().getCount() < 2) {
            hapusSemuaTask.setVisibility(View.INVISIBLE);
        } else {
            hapusSemuaTask.setVisibility(View.VISIBLE);
        }

        if (listDaftar.getAdapter().getCount() < 1) {
            judulDaftar.setText("Kosong");
        } else {
            judulDaftar.setText("To Do List");
        }

        //Menghapus seluruh task
        hapusSemuaTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dh.hapusSemuaTask();
                dh.close();
                daftarTask.dismiss();
                Toast.makeText(getApplicationContext(), "Seluruh Task dihapus", Toast.LENGTH_SHORT).show();
            }
        });
        daftarTask.show();
    }
}