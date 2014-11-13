package com.backend.controllers;

import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.UserEntity;
import com.frontend.representation.beans.UserBean;
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
 * @author Armen Arzumanyan
 */
public class UserController implements Serializable {

    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public UserController() {
        emf = Persistence.createEntityManagerFactory("JSFPU");
    }

    public void create(UserBean newUserBean) {
        EntityManager em = null;
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setName(newUserBean.getName());
            userEntity.setPassword(newUserBean.getPassword());
            userEntity.setRole(newUserBean.getRole().intValue());
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(userEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserEntity userEntity) throws NonexistentEntityException, Exception {
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
                if (findUserEntity(id) == null) {
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
            UserEntity userEntity;
            try {
                userEntity = em.getReference(UserEntity.class, id);
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

    public List<UserEntity> getAllUsers() {
        return findUserEntityEntities(true, -1, -1);
    }

    public List<UserEntity> findUserEntityEntities(int maxResults, int firstResult) {
        return findUserEntityEntities(false, maxResults, firstResult);
    }

    private List<UserEntity> findUserEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserEntity.class));
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

    public UserEntity findUserEntity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserEntity> rt = cq.from(UserEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
         
    public Map<Long, String> getUserMap(){
        Map<Long, String> resultMap = new HashMap<Long, String>();
        List<UserEntity> allList = getAllUsers();
        for(UserEntity entity : allList){
            resultMap.put(entity.getId(), entity.getName());
        }
        return resultMap;
    }     
}
