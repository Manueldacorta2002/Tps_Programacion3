package com.utn.repository;

import com.utn.entities.Base;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends Base> {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("miUnidad");

    private final Class<T> entityClass;

    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityManager abrirEntityManager() {
        return EMF.createEntityManager();
    }

    public T guardar(T entity) {
        EntityManager em = abrirEntityManager();
        try {
            em.getTransaction().begin();
            T entityGuardada = em.merge(entity);
            em.getTransaction().commit();
            return entityGuardada;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar la entidad", e);
        } finally {
            em.close();
        }
    }

    public Optional<T> buscarPorId(Long id) {
        EntityManager em = abrirEntityManager();
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } finally {
            em.close();
        }
    }

    public List<T> listarActivos() {
        EntityManager em = abrirEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.eliminado = false";
            return em.createQuery(jpql, entityClass).getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    public boolean eliminarLogico(Long id) {
        EntityManager em = abrirEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity == null) {
                em.getTransaction().rollback();
                return false;
            }

            entity.setEliminado(true);
            em.merge(entity);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al realizar baja lógica", e);
        } finally {
            em.close();
        }
    }
}