package com.example.pruebaandroid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
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
        int tareaId = getIntent().getIntExtra("tareaId", -1);
        if (tareaId == -1) {
            Toast.makeText(this, "Tarea no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Conexion a base de datos
        SQLiteDatabase db = openOrCreateDatabase("TaskDB", MODE_PRIVATE, null);
        String createTable = "CREATE TABLE IF NOT EXISTS tasks (" +
                "idTask INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT" +
                ");";
        db.execSQL(createTable);

        // Ejecutar consulta para obtener los detalles de la tarea por id
        final Cursor cursor = db.rawQuery("SELECT idTask, name, description FROM tasks WHERE idTask = ?", new String[]{String.valueOf(tareaId)});

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

        txtTitulo.setText("Editar Estudiante #" + task.getIdTask());
        inputTarea.setText(task.getNombreTask());
        inputDesc.setText(task.getDescripcion());

        // 5. Boton editar tarea
        Button btnEditar = findViewById(R.id.btnEdit);
        btnEditar.setOnClickListener(v -> {
            // Obtener los valores del input
            EditText inputTarea2 = findViewById(R.id.txtTarea);
            EditText inputDesc2 = findViewById(R.id.txtDescripcion);
            String nuevoNombre = inputTarea2.getText().toString().trim();
            String nuevaDesc = inputDesc2.getText().toString().trim();

            // Actualizar la tarea en la base de datos
            String sql = "UPDATE tasks SET name = ?, description = ? WHERE idTask = ?";
            SQLiteStatement stm = db.compileStatement(sql);
            stm.bindString(1, nuevoNombre);
            stm.bindString(2, nuevaDesc);
            stm.bindString(3, String.valueOf(task.getIdTask()));
            stm.execute();
            db.close();

            // Redirigir a ListarActivity
            Toast.makeText(VerActivity.this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VerActivity.this, ListarActivity.class);
            startActivity(intent);
            finish();
        });

        // 6. Boton eliminar tarea
        Button btnEliminar = findViewById(R.id.btnDelete);
        btnEliminar.setOnClickListener(v -> {
            // Eliminar la tarea de la base de datos
            String sql = "DELETE FROM tasks WHERE idTask = ?";
            SQLiteStatement stm = db.compileStatement(sql);
            stm.bindString(1, String.valueOf(task.getIdTask()));
            stm.execute();
            db.close();

            // Redirigir a ListarActivity
            Toast.makeText(VerActivity.this, "Tarea" + task.getIdTask() + " eliminada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VerActivity.this, ListarActivity.class);
            startActivity(intent);
            finish();
        });
    }
}