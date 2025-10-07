package com.example.pruebaandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;


public class SecondActivity extends AppCompatActivity {

    ListView lista;
    ArrayList<String> datos;
    ArrayAdapter<String> listaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        datos = new ArrayList<>();
        datos.add("Tarea 1");
        datos.add("Tarea 2");

        lista = findViewById(R.id.List);
        listaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        lista.setAdapter(listaAdapter);

        lista.setOnItemClickListener((parent, view, position, id) -> {
            String datoActual = datos.get(position);
            Toast.makeText(SecondActivity.this, "Click indice " + position + " Valor " + datoActual, Toast.LENGTH_LONG).show();
        });

        Button addTask = findViewById(R.id.btnAddTask);
        addTask.setOnClickListener(v -> {
            EditText newTask = findViewById(R.id.txtNombreTarea);
            String task = newTask.getText().toString().trim();

            if (task.isEmpty()) {
                Toast.makeText(SecondActivity.this, "No puedes agregar una tarea vacia", Toast.LENGTH_LONG).show();
                return;
            }

            listaAdapter.add(task);
            newTask.setText("");
            Toast.makeText(SecondActivity.this, "Tarea agregada", Toast.LENGTH_SHORT).show();
        });
    }
}