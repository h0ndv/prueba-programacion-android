package com.example.pruebaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ListarActivity extends AppCompatActivity {

    ListView lista;
    ArrayList<String> datos;
    ArrayList<String> taskIds;
    ArrayAdapter<String> listaAdapter;
    int selectedPosition = -1;

    // 2. Carga las tareas desde Firebase Firestore
    private void loadTasksFromFirebase() {
        if (datos == null) datos = new ArrayList<>();
        if (taskIds == null) taskIds = new ArrayList<>();
        datos.clear();
        taskIds.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tasks")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String idTask = document.getString("idTask");
                        String name = document.getString("name");
                        String description = document.getString("description");
                        
                        if (idTask != null && name != null && description != null) {
                            taskIds.add(idTask);
                            datos.add(name + ": " + description);
                        }
                    }

                    if (listaAdapter != null) {
                        listaAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ListarActivity.this, "Error al cargar tareas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
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

        datos = new ArrayList<>();
        taskIds = new ArrayList<>();

        lista = findViewById(R.id.List);
        listaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        lista.setAdapter(listaAdapter);

        // 2. Cargar datos desde Firebase Firestore y mostrar en el ListView
        loadTasksFromFirebase();

        // 4. Seleccionar tarea y redirigir a VerActivity
        lista.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            String tareaActual = datos.get(position);
            Toast.makeText(ListarActivity.this, "Tarea seleccionada: " + tareaActual, Toast.LENGTH_SHORT).show();

            // Obtener idTask de Firebase de la tarea seleccionada
            if (position >= taskIds.size()) {
                Toast.makeText(ListarActivity.this, "No se pudo obtener el Id de la tarea", Toast.LENGTH_SHORT).show();
                return;
            }

            String idTask = taskIds.get(position);

            Intent intent = new Intent(ListarActivity.this, VerActivity.class);
            intent.putExtra("tareaId", idTask);
            startActivity(intent);   
        });

        // Agregar tarea
        Button addTask = findViewById(R.id.btnAddTask);
        addTask.setOnClickListener(v -> {
            // Redirecionar a AgregarActivity
            Intent intent = new Intent(ListarActivity.this, AgregarActivity.class);
            startActivity(intent);

            /* 
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
            */
        });

        // Eliminar tarea
        Button deleteTask = findViewById(R.id.btnEliminar);
        deleteTask.setOnClickListener(v -> {

            // Validar que haya una tarea seleccionada
            if (selectedPosition == -1) {
                Toast.makeText(ListarActivity.this, "No hay tarea seleccionada", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedPosition >= datos.size() || selectedPosition >= taskIds.size()) {
                Toast.makeText(ListarActivity.this, "Posición inválida", Toast.LENGTH_SHORT).show();
                selectedPosition = -1;
                return;
            }

            // Obtener el ID de la tarea a eliminar
            String taskIdToDelete = taskIds.get(selectedPosition);
            int positionToDelete = selectedPosition;

            // Eliminar tarea de Firebase Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("tasks")
                    .document(taskIdToDelete)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Eliminar de las listas locales
                        datos.remove(positionToDelete);
                        taskIds.remove(positionToDelete);
                        listaAdapter.notifyDataSetChanged();
                        selectedPosition = -1;
                        Toast.makeText(ListarActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ListarActivity.this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}