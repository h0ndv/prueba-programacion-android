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

import com.example.pruebaandroid.Task;


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
            EditText newDesc = findViewById(R.id.txtDescripcion);

            // Obtener texto de los EditText
            String task = newTask.getText().toString().trim();
            String desc = newDesc.getText().toString().trim();

            // Validar que los campos no esten vacios
            if (task.isEmpty()) {
                Toast.makeText(SecondActivity.this, "No puedes agregar una tarea vacia", Toast.LENGTH_LONG).show();
                return;
            }

            if (desc.isEmpty()) {
                Toast.makeText(SecondActivity.this, "No puedes agregar una descripcion vacia", Toast.LENGTH_LONG).show();
                return;
            }

            // Crear objeto Task
            Task nuevaTask = new Task();
            nuevaTask.setIdTask(datos.size() + 1);
            nuevaTask.setNombreTask(task);
            nuevaTask.setDescripcion(desc);

            // Agregar tarea a la lista
            listaAdapter.add(nuevaTask.getIdTask() + ". " + nuevaTask.getNombreTask() + ": " + nuevaTask.getDescripcion());

            // Limpiar campos de texto
            newTask.setText("");
            newDesc.setText("");
            Toast.makeText(SecondActivity.this, "Tarea agregada", Toast.LENGTH_SHORT).show();
        });
    }
}