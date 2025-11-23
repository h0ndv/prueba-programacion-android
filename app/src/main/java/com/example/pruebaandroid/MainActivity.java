package com.example.pruebaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnIr = findViewById(R.id.button);
        btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Hilo de espera 5 segundos y redirigir a ListarActivity
                new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                        runOnUiThread(() -> {
                            Intent intent = new Intent(MainActivity.this, ListarActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("Error en el hilo: " + e.getMessage());
                    }
                }).start();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}