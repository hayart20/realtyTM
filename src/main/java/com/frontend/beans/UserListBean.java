package com.frontend.beans;

import com.backend.controllers.RoleController;
import com.backend.controllers.UserController;
import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.RoleEntity;
import com.backend.entities.UserEntity;
import com.frontend.representation.beans.UserBean;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Hayk Harutyunyan
 */
@ManagedBean(name = "userListBean")
@ViewScoped
public class UserListBean implements Serializable {

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    private UserController controller = new UserController();
    private List<SelectItem> allRoleSelectItems = new ArrayList<SelectItem>();
    
    private RoleController roleController = new RoleController();
    private List<SelectItem> allUsersSelectItems = new ArrayList<SelectItem>();
     @ManagedProperty("#{userBean}")
    private UserBean userBean;
     private UserBean newUserBean = new UserBean();
     
    public UserListBean() {
             FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = 
        fc.getExternalContext().getRequestParameterMap();
        if(params.get("edit_id") != null){
            String strEdit_id = params.get("edit_id");
             Long edit_id = Long.valueOf(strEdit_id); 
            UserEntity findEntity = controller.findUserEntity(edit_id);
           newUserBean.setName(findEntity.getName());
           newUserBean.setPassword(findEntity.getPassword());
           newUserBean.setRole(findEntity.getRole().longValue());
           newUserBean.setId(edit_id);
        }
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
    }

    public List<UserEntity> getAllUsers() {
        return controller.getAllUsers();
    }

    public List<SelectItem> getAllUsersSelectItems() {
        if(allUsersSelectItems != null){
            List<UserEntity> list = getAllUsers();
            for(UserEntity item : list){
                allUsersSelectItems.add(new SelectItem(item.getId(), item.getName()));
            }
        }
        return allUsersSelectItems;
    }

    public void setAllUsersSelectItems(List<SelectItem> allUsersSelectItems) {
        this.allUsersSelectItems = allUsersSelectItems;
    }
    
    public String create() {
      FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("edit_id") != null || (newUserBean.getId() != null && !newUserBean.getId().equals(""))){
          UserEntity userEntity = new UserEntity();
          Long edit_id = null;
          String strEdit_id = params.get("edit_id");
          if(strEdit_id == null){
              edit_id = newUserBean.getId();
          } else {
              edit_id = Long.valueOf(strEdit_id);
          }
          userEntity.setId(edit_id);
          userEntity.setName(newUserBean.getName());
          userEntity.setPassword(newUserBean.getPassword());
          userEntity.setRole(newUserBean.getRole().intValue());
          try {  
              controller.edit(userEntity);
          } catch (Exception ex) {
              Logger.getLogger(RoleListBean.class.getName()).log(Level.SEVERE, null, ex);
          }
      } else {
        controller.create(newUserBean);
      }  
        return "successUser";
    }
    public String getAutUserName(){
        if(userBean.getName() != null){    
            return userBean.getName();
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.jsf");
            } catch (IOException ex) {
                Logger.getLogger(UserLoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return "unknow";
    }     

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
    
    public String editUser(){
        return null;
    }
    
    
    public List<SelectItem> getAllRoleSelectItems() {
        if(allRoleSelectItems != null){
            List<RoleEntity> list = roleController.getAllRole();
            for(RoleEntity item : list){
                allRoleSelectItems.add(new SelectItem(item.getId(), item.getName()));
            }
        }
        return allRoleSelectItems;
    }
    
    public String deleteUser(){
         FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("delete_id") != null){
        Long delete_id = Long.valueOf(params.get("delete_id")); 
             try {
                 controller.destroy(delete_id);
                 return "successUser";
             } catch (NonexistentEntityException ex) {
                 Logger.getLogger(UserListBean.class.getName()).log(Level.SEVERE, null, ex);
             }
      }  
        return "";
    }

    public UserBean getNewUserBean() {
        return newUserBean;
    }

    public void setNewUserBean(UserBean newUserBean) {
        this.newUserBean = newUserBean;
    }

    
}
