package com.frontend.beans;

import com.backend.controllers.FileController;
import com.backend.controllers.PriorityController;
import com.backend.controllers.StatusController;
import com.backend.controllers.TaskController;
import com.backend.controllers.UserController;
import com.backend.controllers.exceptions.NonexistentEntityException;
import com.backend.entities.PriorityEntity;
import com.backend.entities.StatusEntity;
import com.backend.entities.TaskEntity;
import com.backend.entities.UserEntity;
import com.frontend.representation.beans.FileBean;
import com.frontend.representation.beans.TaskBean;
import com.frontend.representation.beans.UserBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.servlet.http.Part;

/**
 *
 * @author Hayk Harutyunyan
 */
@ManagedBean(name = "taskListBean")
@ViewScoped
public class TaskListBean implements Serializable {

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    
    private Part file;
    private String fileContent;
    
    private FileController fileController = new FileController();
    private TaskController taskController = new TaskController();
    private UserController userController = new UserController();
    private PriorityController priorityController = new PriorityController();
    private StatusController statusController = new StatusController();
    
    private List<SelectItem> allStatusSelectItems = null;
    private List<SelectItem> allPrioritySelectItems = null;
    private List<SelectItem> allUsersSelectItems = null;
    
     @ManagedProperty("#{userBean}")
    private UserBean userBean;
     private TaskBean newTaskBean = new TaskBean();
     private TaskBean filterTaskBean = new TaskBean();
     
     private List<TaskBean> allList = null;
     
    public TaskListBean() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = 
        fc.getExternalContext().getRequestParameterMap();
        if(params.get("edit_id") != null){
            String strEdit_id = params.get("edit_id");
             Long edit_id = Long.valueOf(strEdit_id); 
            TaskEntity findTaskEntity = taskController.findTaskEntity(edit_id);
           newTaskBean.setName(findTaskEntity.getName());
           newTaskBean.setDescription(findTaskEntity.getDescription());
           newTaskBean.setPriority_id(findTaskEntity.getPriority_id().toString());
           newTaskBean.setStatus_id(findTaskEntity.getStatus_id().toString());
           newTaskBean.setAssign_user_id(findTaskEntity.getAssign_user_id().toString());
           newTaskBean.setId(strEdit_id);
           newTaskBean.setFileList(fileController.findFileByPID(edit_id));
        } 
        if(userBean != null){
            filterTaskBean.setAssign_user_id(userBean.getId().toString());                    
            search();
        }
        
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        if(userBean != null){
            filterTaskBean.setAssign_user_id(userBean.getId().toString());                    
        }
        search();
    }

    
    public String search() {
        allList = taskController.getAllTask(filterTaskBean);
        return "";
    }
    
    public String create() {
      FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("edit_id") != null || (newTaskBean.getId() != null && !newTaskBean.getId().equals(""))){
          TaskEntity taskEntity = new TaskEntity();
          String edit_id = params.get("edit_id");
          if(edit_id == null){
              edit_id = newTaskBean.getId();
          }
          taskEntity.setId(Long.valueOf(edit_id));
          taskEntity.setName(newTaskBean.getName());
          
          taskEntity.setDescription(newTaskBean.getDescription());
          taskEntity.setAssign_user_id(Long.valueOf(newTaskBean.getAssign_user_id()));
          taskEntity.setPriority_id(Long.valueOf(newTaskBean.getPriority_id()));
          taskEntity.setStatus_id(Long.valueOf(newTaskBean.getStatus_id()));
          
          try {  
              taskController.edit(taskEntity);
          } catch (Exception ex) {
              Logger.getLogger(TaskListBean.class.getName()).log(Level.SEVERE, null, ex);
          }
      } else {
        taskController.create(newTaskBean);
      }  
        return "successTask";
    }
   

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
    
    public String editTask(){
        return null;
    }
    
    public String deleteTask(){
         FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("delete_id") != null){
        Long delete_id = Long.valueOf(params.get("delete_id")); 
             try {
                 taskController.destroy(delete_id);
                 return "successTask";
             } catch (NonexistentEntityException ex) {
                 Logger.getLogger(TaskListBean.class.getName()).log(Level.SEVERE, null, ex);
             }
      }  
        return "";
    }

    
    public String deleteFile(){
         FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = 
      fc.getExternalContext().getRequestParameterMap();
      if(params.get("delete_file_id") != null){
        Long delete_file_id = Long.valueOf(params.get("delete_file_id")); 
             try {
                 fileController.destroy(delete_file_id);
                 return "";
             } catch (NonexistentEntityException ex) {
                 Logger.getLogger(TaskListBean.class.getName()).log(Level.SEVERE, null, ex);
             }
      }  
        return "";
    }
    
    public TaskBean getNewTaskBean() {
        return newTaskBean;
    }

    public void setNewTaskBean(TaskBean newTaskBean) {
        this.newTaskBean = newTaskBean;
    }

    public List<SelectItem> getAllStatusSelectItems() {
        if(allStatusSelectItems == null){
            allStatusSelectItems = new ArrayList<SelectItem>();
            List<StatusEntity> list = statusController.getAll();
            for(StatusEntity item : list){
                allStatusSelectItems.add(new SelectItem(item.getId(), item.getName()));
            }
        }
        return allStatusSelectItems;
    }
    
    public List<SelectItem> getAllUsersSelectItems() {
        if(allUsersSelectItems == null){
            allUsersSelectItems = new ArrayList<SelectItem>();
            List<UserEntity> list = userController.getAllUsers();
            for(UserEntity item : list){
                allUsersSelectItems.add(new SelectItem(item.getId(), item.getName()));
            }
        }
        return allUsersSelectItems;
    }
    
    public List<SelectItem> getAllPrioritySelectItems() {
        if(allPrioritySelectItems == null){
            allPrioritySelectItems = new ArrayList<SelectItem>();
            List<PriorityEntity> list = priorityController.getAll();
            for(PriorityEntity item : list){
                allPrioritySelectItems.add(new SelectItem(item.getId(), item.getName()));
            }
        }
        return allPrioritySelectItems;
    }
    
    public String upload() throws IOException {  
        InputStream inputStream = file.getInputStream();    
        String fileName = getFilename(file);
        
        FileBean fileBean = new FileBean();
        fileBean.setpId(newTaskBean.getId());
        fileBean.setName(fileName);
        String ext = fileName.substring(fileName.lastIndexOf('.')+1, fileName.length());
        fileBean.setExt(ext);
        FileController fileController = new FileController();
        fileController.create(fileBean);
        
        FileOutputStream outputStream = new FileOutputStream("c:/"+fileBean.getId()+ext);  
        byte[] buffer = new byte[4096];          
        int bytesRead = 0;  
        while(true) {                          
            bytesRead = inputStream.read(buffer);  
            if(bytesRead > 0) {  
                outputStream.write(buffer, 0, bytesRead);  
            }else {  
                break;  
            }                         
        }  
        File targetFolder = new File("images");
        outputStream.close();  
        inputStream.close();  
         
        return "";  
    } 
 
 
    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    
    private static String getFilename(Part part) {  
        for (String cd : part.getHeader("content-disposition").split(";")) {  
            if (cd.trim().startsWith("filename")) {  
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");  
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.  
            }  
        }  
        return null;  
    } 

    public TaskBean getFilterTaskBean() {
        return filterTaskBean;
    }

    public void setFilterTaskBean(TaskBean filterTaskBean) {
        this.filterTaskBean = filterTaskBean;
    }

    public List<TaskBean> getAllList() {
        return allList;
    }

    public void setAllList(List<TaskBean> allList) {
        this.allList = allList;
    }
    
    
}
