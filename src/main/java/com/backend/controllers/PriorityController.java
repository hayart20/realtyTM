package com.backend.controllers;

import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.PriorityEntity;
import com.backend.entities.StatusEntity;
import com.frontend.representation.beans.PriorityBean;
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
public class PriorityController implements Serializable {

    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public PriorityController() {
        emf = Persistence.createEntityManagerFactory("JSFPU");
    }

    public void create(PriorityBean priorityBean) {
        PriorityEntity priorityEntity = new PriorityEntity();
        priorityEntity.setName(priorityBean.getName());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(priorityEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PriorityEntity priorityEntity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            priorityEntity = em.merge(priorityEntity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = priorityEntity.getId();
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
            PriorityEntity priorityEntity;
            try {
                priorityEntity = em.getReference(PriorityEntity.class, id);
                priorityEntity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userEntity with id " + id + " no longer exists.", enfe);
            }
            em.remove(priorityEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PriorityEntity> getAll() {
        return findEntityEntities(true, -1, -1);
    }

    public List<PriorityEntity> findEntityEntities(int maxResults, int firstResult) {
        return findEntityEntities(false, maxResults, firstResult);
    }

    private List<PriorityEntity> findEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PriorityEntity.class));
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

    public PriorityEntity findEntity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PriorityEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PriorityEntity> rt = cq.from(PriorityEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
        
    public Map<Long, String> getPriorityMap(){
        Map<Long, String> resultMap = new HashMap<Long, String>();
        List<PriorityEntity> allList = getAll();
        for(PriorityEntity entity : allList){
            resultMap.put(entity.getId(), entity.getName());
        }
        return resultMap;
    }
}
