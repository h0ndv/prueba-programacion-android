package com.example.pruebaandroid;

import android.os.Bundle;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


// Firebase Storage
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AgregarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Conexion a la abse de datos
        SQLiteDatabase db = openOrCreateDatabase("TaskDB", MODE_PRIVATE, null);
        if (db == null) {
            System.out.println("Error al abrir o crear la base de datos");
            return;
        }
        
        // Crear la tabla si no existe
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks (idTask INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT);");
        
        // Boton agregar tarea
        Button addTask = findViewById(R.id.btnAddTask);
        addTask.setOnClickListener(v -> {
            EditText newTask = findViewById(R.id.txtNombreTarea);
            EditText newDesc = findViewById(R.id.txtDescripcion);

            // Obtener texto de los EditText
            String task = newTask.getText().toString().trim();
            String desc = newDesc.getText().toString().trim();

            // Validar que los campos no esten vacios
            if (task.isEmpty()) {
                Toast.makeText(AgregarActivity.this, "No puedes agregar una tarea vacia", Toast.LENGTH_SHORT).show();
                return;
            }

            if (desc.isEmpty()) {
                Toast.makeText(AgregarActivity.this, "No puedes agregar una descripcion vacia", Toast.LENGTH_SHORT).show();
                return;
            }


            // Insertar la nueva tarea en Firebase Storage
            FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
            CollectionReference tasks = dbFirestore.collection("tasks");
            DocumentReference taskRef = tasks.document();
            String idTask = taskRef.getId();
            
            // Generar objeto con los datos de la tarea
            Map<String, Object> taskData = new HashMap<>();
            taskData.put("idTask", idTask);
            taskData.put("name", task);
            taskData.put("description", desc);

            // Insertar la tarea en Firebase Storage
            taskRef.set(taskData);
            Toast.makeText(AgregarActivity.this, "Tarea agregada", Toast.LENGTH_SHORT).show();

            // Redireccionar a ListarActivity
            Intent intent = new Intent(AgregarActivity.this, ListarActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
