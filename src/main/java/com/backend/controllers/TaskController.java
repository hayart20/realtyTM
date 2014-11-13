package com.backend.controllers;

import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.TaskEntity;
import com.frontend.representation.beans.TaskBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Hayk Harutyunyan
 */
public class TaskController implements Serializable {

    private EntityManagerFactory emf;
    private StatusController statusController = new StatusController();
    private UserController userController = new UserController();
    private PriorityController priorityController = new PriorityController();
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public TaskController() {
        emf = Persistence.createEntityManagerFactory("JSFPU");
    }

    public void create(TaskBean taskBean) {
        EntityManager em = null;
        try {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setName(taskBean.getName());
            taskEntity.setDescription(taskBean.getDescription());
            taskEntity.setAssign_user_id(Long.valueOf(taskBean.getAssign_user_id()));
            taskEntity.setPriority_id(Long.valueOf(taskBean.getPriority_id()));
            taskEntity.setStatus_id(Long.valueOf(taskBean.getStatus_id()));
          
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(taskEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TaskEntity taskEntity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            taskEntity = em.merge(taskEntity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = taskEntity.getId();
                if (findTaskEntity(id) == null) {
                    throw new NonexistentEntityException("The taskEntity with id " + id + " no longer exists.");
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
            TaskEntity taskEntity;
            try {
                taskEntity = em.getReference(TaskEntity.class, id);
                taskEntity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userEntity with id " + id + " no longer exists.", enfe);
            }
            em.remove(taskEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TaskBean> getAllTask(TaskBean filterTaskBean) {
        return findTaskEntityEntities(true, -1, -1, filterTaskBean);
    }

    public List<TaskBean> findTaskEntityEntities(int maxResults, int firstResult, TaskBean filterTaskBean) {
        return findTaskEntityEntities(false, maxResults, firstResult, filterTaskBean);
    }

    private List<TaskBean> findTaskEntityEntities(boolean all, int maxResults, int firstResult, TaskBean filterTaskBean) {
        EntityManager em = getEntityManager();
        try {
          
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root t = cq.from(TaskEntity.class);
            cq.select(t);
          
            List<Predicate> predicates=new ArrayList<Predicate>();
            if (filterTaskBean.getUid() != null && filterTaskBean.getUid() > 0){
              Predicate predicate = cb.equal(t.<String>get("id"),filterTaskBean.getUid());
              predicates.add(predicate);
            }
            if (filterTaskBean.getName() != null && !filterTaskBean.getName().equals("")) {
              Predicate predicate = cb.like(t.<String>get("name"),filterTaskBean.getName());
              predicates.add(predicate);
            }
            if (filterTaskBean.getPriority_id() != null && !filterTaskBean.getPriority_id().equals("")) {
              Predicate predicate = cb.like(t.<String>get("priority_id"),filterTaskBean.getPriority_id());
              predicates.add(predicate);
            }
            if (filterTaskBean.getStatus_id() != null && !filterTaskBean.getStatus_id().equals("")) {
              Predicate predicate = cb.like(t.<String>get("status_id"),filterTaskBean.getStatus_id());
              predicates.add(predicate);
            }
            if (filterTaskBean.getAssign_user_id()!= null && !filterTaskBean.getAssign_user_id().equals("")) {
              Predicate predicate = cb.like(t.<String>get("assign_user_id"),filterTaskBean.getAssign_user_id());
              predicates.add(predicate);
            }
            
            cq.where(predicates.toArray(new Predicate[]{}));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            Map<Long, String> statusMap = statusController.getStatusMap();
            Map<Long, String> userMap = userController.getUserMap();
            Map<Long, String> priorityMap = priorityController.getPriorityMap();
            
            List<TaskEntity> list = q.getResultList();
            List<TaskBean> resultList = new ArrayList<TaskBean>();
            for(TaskEntity entity : list){
                TaskBean bean = new TaskBean();
                bean.setId(entity.getId().toString());
                bean.setName(entity.getName());
                bean.setDescription(entity.getDescription());

                bean.setPriorityName(priorityMap.get(entity.getPriority_id()));
                bean.setAssignUserName(userMap.get(entity.getAssign_user_id()));
                bean.setStatusName(statusMap.get(entity.getStatus_id()));
                
                resultList.add(bean);
            }
            return resultList;
        } finally {
            em.close();
        }
    }

    public TaskEntity findTaskEntity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TaskEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaskEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TaskEntity> rt = cq.from(TaskEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
       
}
