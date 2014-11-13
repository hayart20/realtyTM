package com.frontend.beans;

import com.backend.controllers.PriorityController;
import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.PriorityEntity;
import com.frontend.representation.beans.PriorityBean;
import com.frontend.representation.beans.UserBean;
import java.io.Serializable;
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

/**
 *
 * @author Hayk Harutyunyan
 */
@ManagedBean(name = "priorityListBean")
@ViewScoped
public class PriorityListBean implements Serializable {

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    private PriorityController controller = new PriorityController();
     @ManagedProperty("#{userBean}")
    private UserBean userBean;
     private PriorityBean newPriorityBean = new PriorityBean();
     
    public PriorityListBean() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = 
        fc.getExternalContext().getRequestParameterMap();
        if(params.get("edit_id") != null){
            String strEdit_id = params.get("edit_id");
             Long edit_id = Long.valueOf(strEdit_id); 
            PriorityEntity findPriorityEntity = controller.findEntity(edit_id);
           newPriorityBean.setName(findPriorityEntity.getName());
           newPriorityBean.setId(strEdit_id);
        }
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();

    }

    public List<PriorityEntity> getAllPriority() {
        return controller.getAll();
    }
    
    public String create() {
      FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("edit_id") != null ){
          PriorityEntity priorityEntity = new PriorityEntity();
          String edit_id = params.get("edit_id");
          if(edit_id == null){
              edit_id = newPriorityBean.getId();
          }
          priorityEntity.setId(Long.valueOf(edit_id));
          priorityEntity.setName(newPriorityBean.getName());
          try {  
              controller.edit(priorityEntity);
          } catch (Exception ex) {
              Logger.getLogger(PriorityListBean.class.getName()).log(Level.SEVERE, null, ex);
          }
      } else {
        controller.create(newPriorityBean);
      }  
        return "successPriority";
    }
   

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
    
    public String editPriority(){
        return null;
    }
    
    public String deletePriority(){
         FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("delete_id") != null){
        Long delete_id = Long.valueOf(params.get("delete_id")); 
             try {
                 controller.destroy(delete_id);
                 return "successPriority";
             } catch (NonexistentEntityException ex) {
                 Logger.getLogger(PriorityListBean.class.getName()).log(Level.SEVERE, null, ex);
             }
      }  
        return "";
    }

    public PriorityBean getNewPriorityBean() {
        return newPriorityBean;
    }

    public void setNewPriorityBean(PriorityBean newPriorityBean) {
        this.newPriorityBean = newPriorityBean;
    }

}
