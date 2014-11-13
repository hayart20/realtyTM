package com.backend.controllers;

import com.backend.entities.UserEntity;
import com.frontend.representation.beans.UserBean;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.Persistence;

/**
 *
 * @author Armen Arzumanyan
 */
public class UserLoginController implements Serializable {

    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public UserLoginController() {
        emf = Persistence.createEntityManagerFactory("JSFPU");
    }

     public UserEntity getUserByParameter(String name, String password) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select c from UserEntity c where c.name=:name and c.password=:password");
            q.setParameter("name", name);
            q.setParameter("password", password);
            List<UserEntity> resultList = q.getResultList();
            if(resultList != null && !resultList.isEmpty()){
                return resultList.get(0);
            }
           
        } finally {
            em.close();
        }
        return null;
    }
}
