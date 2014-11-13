package com.backend.controllers;

import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.FileEntity;
import com.backend.entities.TaskEntity;
import com.frontend.representation.beans.FileBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Hayk Harutyunyan
 */
public class FileController implements Serializable {

    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public FileController() {
        emf = Persistence.createEntityManagerFactory("JSFPU");
    }

    public void create(FileBean fileBean) {
        EntityManager em = null;
        try {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setPid(fileBean.getpId());
            fileEntity.setName(fileBean.getName());
            fileEntity.setExt(fileBean.getExt());
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(fileEntity);
            em.getTransaction().commit();
            fileBean.setId(fileEntity.getId().toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FileEntity fileEntity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            fileEntity = em.merge(fileEntity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fileEntity.getId();
                if (findFileEntity(id) == null) {
                    throw new NonexistentEntityException("The fileEntity with id " + id + " no longer exists.");
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
            FileEntity fileEntity;
            try {
                fileEntity = em.getReference(FileEntity.class, id);
                fileEntity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userEntity with id " + id + " no longer exists.", enfe);
            }
            em.remove(fileEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FileEntity> getAllFile() {
        return findFileEntityEntities(true, -1, -1);
    }

    public List<FileEntity> findFileEntityEntities(int maxResults, int firstResult) {
        return findFileEntityEntities(false, maxResults, firstResult);
    }

    private List<FileEntity> findFileEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FileEntity.class));
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

    public List<FileBean> findFileByPID(Long pid) {
        EntityManager em = getEntityManager();
        try {
           CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root t = cq.from(FileEntity.class);
            cq.select(t);
          
            List<Predicate> predicates=new ArrayList<Predicate>();
              Predicate predicate = cb.equal(t.<String>get("pid"),pid);
              predicates.add(predicate);

            cq.where(predicates.toArray(new Predicate[]{}));
            Query q = em.createQuery(cq);
           
            
            List<FileEntity> list = q.getResultList();
            List<FileBean> resultList = new ArrayList<FileBean>();
            for(FileEntity fileEntity : list){
                FileBean fileBean = new FileBean();
               fileBean.setId(fileEntity.getId().toString());
               fileBean.setName(fileEntity.getName());
               fileBean.setExt(fileEntity.getExt());
               fileBean.setFileLink("c:\\"+fileEntity.getId().toString()+"."+fileEntity.getExt());
               resultList.add(fileBean);
            }
            return resultList;
        } finally {
            em.close();
        }
    }
    
    public FileEntity findFileEntity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FileEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getFileEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FileEntity> rt = cq.from(FileEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
}
