package com.backend.controllers;

import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.PriorityEntity;
import com.backend.entities.RoleEntity;
import com.frontend.representation.beans.RoleBean;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Hayk Harutyunyan
 */
public class RoleController implements Serializable {

    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public RoleController() {
        emf = Persistence.createEntityManagerFactory("JSFPU");
    }

    public void create(RoleBean roleBean) {
        EntityManager em = null;
        try {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName(roleBean.getName());
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(roleEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RoleEntity roleEntity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            roleEntity = em.merge(roleEntity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = roleEntity.getId();
                if (findRoleEntity(id) == null) {
                    throw new NonexistentEntityException("The roleEntity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RoleEntity userEntity;
            try {
                userEntity = em.getReference(RoleEntity.class, id);
                userEntity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userEntity with id " + id + " no longer exists.", enfe);
            }
            em.remove(userEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RoleEntity> getAllRole() {
        return findRoleEntityEntities(true, -1, -1);
    }

    public List<RoleEntity> findRoleEntityEntities(int maxResults, int firstResult) {
        return findRoleEntityEntities(false, maxResults, firstResult);
    }

    private List<RoleEntity> findRoleEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RoleEntity.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public RoleEntity findRoleEntity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RoleEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RoleEntity> rt = cq.from(RoleEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
}
