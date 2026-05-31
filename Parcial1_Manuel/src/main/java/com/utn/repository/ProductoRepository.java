package com.utn.repository;

import com.utn.entities.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

public class ProductoRepository extends BaseRepository<Producto> {

    public ProductoRepository() {
        super(Producto.class);
    }

    @Override
    public List<Producto> listarActivos() {
        EntityManager em = null;
        try {
            em = abrirEntityManager();
            return em.createQuery(
                    "SELECT p FROM Producto p JOIN FETCH p.categoria WHERE p.eliminado = false",
                    Producto.class
            ).getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> buscarPorCategoria(Long categoriaId) {
        EntityManager em = null;
        try {
            em = abrirEntityManager();

            // Consulta productos activos filtrando por el id de la categoría asociada.
            TypedQuery<Producto> query = em.createQuery(
                    "SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.eliminado = false",
                    Producto.class
            );
            query.setParameter("categoriaId", categoriaId);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}