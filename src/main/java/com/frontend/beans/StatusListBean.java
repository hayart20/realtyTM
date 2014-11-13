package com.frontend.beans;

import com.backend.controllers.StatusController;
import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.StatusEntity;
import com.frontend.representation.beans.StatusBean;
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
@ManagedBean(name = "statusListBean")
@ViewScoped
public class StatusListBean implements Serializable {

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    private StatusController controller = new StatusController();
    private List<SelectItem> allStatusSelectItems = new ArrayList<SelectItem>();
     @ManagedProperty("#{userBean}")
    private UserBean userBean;
     private StatusBean newStatusBean = new StatusBean();
     
    public StatusListBean() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = 
        fc.getExternalContext().getRequestParameterMap();
        if(params.get("edit_id") != null){
            String strEdit_id = params.get("edit_id");
             Long edit_id = Long.valueOf(strEdit_id); 
            StatusEntity findStatusEntity = controller.findEntity(edit_id);
           newStatusBean.setName(findStatusEntity.getName());
           newStatusBean.setId(strEdit_id);
        }
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();

    }

    public List<StatusEntity> getAllStatus() {
        return controller.getAll();
    }

    public List<SelectItem> getAllStatusSelectItems() {
        if(allStatusSelectItems != null){
            List<StatusEntity> list = getAllStatus();
            for(StatusEntity item : list){
                allStatusSelectItems.add(new SelectItem(item.getId(), item.getName()));
            }
        }
        return allStatusSelectItems;
    }
    
    public String create() {
      FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("edit_id") != null || (newStatusBean.getId() != null && !newStatusBean.getId().equals(""))){
          StatusEntity roleEntity = new StatusEntity();
          String edit_id = params.get("edit_id");
          if(edit_id == null){
              edit_id = newStatusBean.getId();
          }
          roleEntity.setId(Long.valueOf(edit_id));
          roleEntity.setName(newStatusBean.getName());
          try {  
              controller.edit(roleEntity);
          } catch (Exception ex) {
              Logger.getLogger(StatusListBean.class.getName()).log(Level.SEVERE, null, ex);
          }
      } else {
        controller.create(newStatusBean);
      }  
        return "successStatus";
    }
   

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
    
    public String editStatus(){
        return null;
    }
    
    public String deleteStatus(){
         FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("delete_id") != null){
        Long delete_id = Long.valueOf(params.get("delete_id")); 
             try {
                 controller.destroy(delete_id);
                 return "successStatus";
             } catch (NonexistentEntityException ex) {
                 Logger.getLogger(StatusListBean.class.getName()).log(Level.SEVERE, null, ex);
             }
      }  
        return "";
    }

    public StatusBean getNewStatusBean() {
        return newStatusBean;
    }

    public void setNewStatusBean(StatusBean newStatusBean) {
        this.newStatusBean = newStatusBean;
    }
}
