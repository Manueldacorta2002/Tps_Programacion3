package com.tup.programacion3.entities;

import java.time.LocalDateTime;

public abstract class Base {

    private static long contador = 0;

    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    public Base() {
        this.id = ++contador;
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
