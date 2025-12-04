package com.example.pruebaandroid;

public class Task {
    String idTask;
    String nombreTask;
    String descripcion;

    public Task() {}

    public Task(String idTask, String nombreTask, String descripcion) {
        this.idTask = idTask;
        this.nombreTask = nombreTask;
        this.descripcion = descripcion;
    }

    public String getIdTask() {
        return idTask;
    }

    public String getNombreTask() {
        return nombreTask;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public void setNombreTask(String nombreTask) {
        this.nombreTask = nombreTask;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
