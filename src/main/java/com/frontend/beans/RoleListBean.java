package com.frontend.beans;

import com.backend.controllers.RoleController;
import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.RoleEntity;
import com.frontend.representation.beans.RoleBean;
import com.frontend.representation.beans.UserBean;
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
@ManagedBean(name = "roleListBean")
@ViewScoped
public class RoleListBean implements Serializable {

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    private RoleController controller = new RoleController();
    @ManagedProperty("#{userBean}")
    private UserBean userBean;
     private RoleBean newRoleBean = new RoleBean();
     
    public RoleListBean() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = 
        fc.getExternalContext().getRequestParameterMap();
        if(params.get("edit_id") != null){
            String strEdit_id = params.get("edit_id");
             Long edit_id = Long.valueOf(strEdit_id); 
            RoleEntity findRoleEntity = controller.findRoleEntity(edit_id);
           newRoleBean.setName(findRoleEntity.getName());
           newRoleBean.setId(strEdit_id);
        }
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();

    }

    public List<RoleEntity> getAllRole() {
        return controller.getAllRole();
    }

    public String create() {
      FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("edit_id") != null || (newRoleBean.getId() != null && !newRoleBean.getId().equals(""))){
          RoleEntity roleEntity = new RoleEntity();
          String edit_id = params.get("edit_id");
          if(edit_id == null){
              edit_id = newRoleBean.getId();
          }
          roleEntity.setId(Long.valueOf(edit_id));
          roleEntity.setName(newRoleBean.getName());
          try {  
              controller.edit(roleEntity);
          } catch (Exception ex) {
              Logger.getLogger(RoleListBean.class.getName()).log(Level.SEVERE, null, ex);
          }
      } else {
        controller.create(newRoleBean);
      }  
        return "successRole";
    }
   

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
    
    public String editRole(){
        return null;
    }
    
    public String deleteRole(){
         FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("delete_id") != null){
        Long delete_id = Long.valueOf(params.get("delete_id")); 
             try {
                 controller.destroy(delete_id);
                 return "successRole";
             } catch (NonexistentEntityException ex) {
                 Logger.getLogger(RoleListBean.class.getName()).log(Level.SEVERE, null, ex);
             }
      }  
        return "";
    }

    public RoleBean getNewRoleBean() {
        return newRoleBean;
    }

    public void setNewRoleBean(RoleBean newRoleBean) {
        this.newRoleBean = newRoleBean;
    }

}
