package com.backend.controllers;

import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.StatusEntity;
import com.frontend.representation.beans.StatusBean;
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
 * @author hayk harutyunyan
 */
public class StatusController implements Serializable {

    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public StatusController() {
        emf = Persistence.createEntityManagerFactory("JSFPU");
    }

    public void create(StatusBean customerBean) {
        StatusEntity customerEntity = new StatusEntity();
        customerEntity.setName(customerBean.getName());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(customerEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StatusEntity userEntity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            userEntity = em.merge(userEntity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = userEntity.getId();
                if (findEntity(id) == null) {
                    throw new NonexistentEntityException("The userEntity with id " + id + " no longer exists.");
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
            StatusEntity userEntity;
            try {
                userEntity = em.getReference(StatusEntity.class, id);
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

    public List<StatusEntity> getAll() {
        return findEntityEntities(true, -1, -1);
    }

    public List<StatusEntity> findEntityEntities(int maxResults, int firstResult) {
        return findEntityEntities(false, maxResults, firstResult);
    }

    private List<StatusEntity> findEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StatusEntity.class));
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

    public StatusEntity findEntity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StatusEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StatusEntity> rt = cq.from(StatusEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Map<Long, String> getStatusMap(){
        Map<Long, String> resultMap = new HashMap<Long, String>();
        List<StatusEntity> allList = getAll();
        for(StatusEntity entity : allList){
            resultMap.put(entity.getId(), entity.getName());
        }
        return resultMap;
    }
}
