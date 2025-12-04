package com.example.pruebaandroid;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener el idTask pasado desde ListarActivity
        String tareaId = getIntent().getStringExtra("tareaId");
        if (tareaId == null || tareaId.isEmpty()) {
            Toast.makeText(this, "Tarea no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Objeto Task para almacenar los detalles de la tarea
        Task task = new Task();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndexOrThrow("idTask");
            int nameIndex = cursor.getColumnIndexOrThrow("name");
            int descIndex = cursor.getColumnIndexOrThrow("description");

            task.setIdTask(cursor.getInt(idIndex));
            task.setNombreTask(cursor.getString(nameIndex));
            task.setDescripcion(cursor.getString(descIndex));
        }
        cursor.close();


        // Cargar los datos a los editText
        TextView txtTitulo = findViewById(R.id.textViewTitulo);
        EditText inputTarea = findViewById(R.id.txtTarea);
        EditText inputDesc = findViewById(R.id.txtDescripcion);

        // Conexion a Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        // Obtener la tarea desde Firebase
        db.collection("tasks")
                .document(tareaId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        task.setIdTask(documentSnapshot.getString("idTask"));
                        task.setNombreTask(documentSnapshot.getString("name"));
                        task.setDescripcion(documentSnapshot.getString("description"));

                        // Cargar los datos a los editText
                        txtTitulo.setText("Editar Tarea");
                        inputTarea.setText(task.getNombreTask());
                        inputDesc.setText(task.getDescripcion());
                    } else {
                        Toast.makeText(this, "Tarea no encontrada", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar tarea: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });

        // 5. Boton editar tarea
        Button btnEditar = findViewById(R.id.btnEdit);
        btnEditar.setOnClickListener(v -> {
            // Obtener los valores del input
            String nuevoNombre = inputTarea.getText().toString().trim();
            String nuevaDesc = inputDesc.getText().toString().trim();

            // Validar que los campos no esten vacios
            if (nuevoNombre.isEmpty() || nuevaDesc.isEmpty()) {
                Toast.makeText(VerActivity.this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar la tarea en Firebase Firestore
            Map<String, Object> taskData = new HashMap<>();
            taskData.put("idTask", task.getIdTask());
            taskData.put("name", nuevoNombre);
            taskData.put("description", nuevaDesc);

            db.collection("tasks")
                    .document(task.getIdTask())
                    .set(taskData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(VerActivity.this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerActivity.this, ListarActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(VerActivity.this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        // 6. Boton eliminar tarea
        Button btnEliminar = findViewById(R.id.btnDelete);
        btnEliminar.setOnClickListener(v -> {
            // Eliminar la tarea de Firebase Firestore
            db.collection("tasks")
                    .document(task.getIdTask())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(VerActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerActivity.this, ListarActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(VerActivity.this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}