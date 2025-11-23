package com.example.pruebaandroid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
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

public class ListarActivity extends AppCompatActivity {

    ListView lista;
    ArrayList<String> datos;
    ArrayList<Task> tasks;
    ArrayAdapter<String> listaAdapter;
    int selectedPosition = -1;

    // 2. Carga las tareas desde la base de datos
    private void loadTasksFromDb() {
        if (datos == null) datos = new ArrayList<>();
        datos.clear();

        try (SQLiteDatabase db = openOrCreateDatabase("TaskDB", MODE_PRIVATE, null);
                Cursor cursor = db.rawQuery("SELECT idTask, name, description FROM tasks ORDER BY idTask ASC", null)) {

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("idTask"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    datos.add(id + ". " + name + ": " + desc);
                } while (cursor.moveToNext());
            }

            if (listaAdapter != null)
                listaAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

        // Crear o abrir la base de datos y crear la tabla si no existe
        SQLiteDatabase db = openOrCreateDatabase("TaskDB", MODE_PRIVATE, null);
        String createTable = "CREATE TABLE IF NOT EXISTS tasks (" +
                "idTask INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT" +
                ");";
        db.execSQL(createTable);
        db.close();

        datos = new ArrayList<>();

        lista = findViewById(R.id.List);
        listaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        lista.setAdapter(listaAdapter);

        // 2. Cargar datos desde la base de datos SQLite y mostrar en el ListView
        loadTasksFromDb();

        lista.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            String tareaActual = datos.get(position);
            Toast.makeText(ListarActivity.this, "Tarea seleccionada: " + tareaActual, Toast.LENGTH_SHORT).show();
        });

        // Agregar tarea
        Button addTask = findViewById(R.id.btnAddTask);
        addTask.setOnClickListener(v -> {
            EditText newTask = findViewById(R.id.txtNombreTarea);
            EditText newDesc = findViewById(R.id.txtDescripcion);

            // Obtener texto de los EditText
            String task = newTask.getText().toString().trim();
            String desc = newDesc.getText().toString().trim();

            // Validar que los campos no esten vacios
            if (task.isEmpty()) {
                Toast.makeText(ListarActivity.this, "No puedes agregar una tarea vacia", Toast.LENGTH_SHORT).show();
                return;
            }

            if (desc.isEmpty()) {
                Toast.makeText(ListarActivity.this, "No puedes agregar una descripcion vacia", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase dbInsert = openOrCreateDatabase("TaskDB", MODE_PRIVATE, null);
            if (dbInsert == null) {
                System.out.println("Error al abrir la base de datos");
                return;
            }

            // Agregar la tarea a la base de datos
            String sql = "INSERT INTO tasks (name, description) VALUES (?, ?)";
            SQLiteStatement stm = dbInsert.compileStatement(sql);
            stm.bindString(1, task);
            stm.bindString(2, desc);
            
            int newId = (int) stm.executeInsert();
            dbInsert.close();

            datos.add(newId + ". " + task + ": " + desc);
            listaAdapter.notifyDataSetChanged();

            // Limpiar los campos de texto
            newTask.setText("");
            newDesc.setText("");
            Toast.makeText(ListarActivity.this, "Tarea agregada", Toast.LENGTH_SHORT).show();
        });

        // Eliminar tarea
        Button deleteTask = findViewById(R.id.btnEliminar);
        deleteTask.setOnClickListener(v -> {

            // Validar que haya una tarea seleccionada
            if (selectedPosition == -1) {
                Toast.makeText(ListarActivity.this, "No hay tarea seleccionada", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedPosition >= datos.size()) {
                Toast.makeText(ListarActivity.this, "Posición inválida", Toast.LENGTH_SHORT).show();
                selectedPosition = -1;
                return;
            }

            // Eliminar tarea de la lista
            datos.remove(selectedPosition);
            listaAdapter.notifyDataSetChanged();
            selectedPosition = -1;
            Toast.makeText(ListarActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
        });
    }
}