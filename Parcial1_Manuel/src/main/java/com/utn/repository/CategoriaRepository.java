package com.utn.repository;

import com.utn.entities.Categoria;

public class CategoriaRepository extends BaseRepository<Categoria> {

    public CategoriaRepository() {
        super(Categoria.class);
    }
}