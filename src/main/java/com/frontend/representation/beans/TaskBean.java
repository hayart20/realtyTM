/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frontend.representation.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Hayk Harutyunyan
 */
public class TaskBean implements Serializable {
    private Long uid;
    private String id;
    private String name;
    private String description;
    private String status_id;
    private String priority_id;
    private String assign_user_id;
    
    private String statusName;
    private String priorityName;
    private String assignUserName;

    private List<FileBean> fileList;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(String priority_id) {
        this.priority_id = priority_id;
    }

    public String getAssign_user_id() {
        return assign_user_id;
    }

    public void setAssign_user_id(String assign_user_id) {
        this.assign_user_id = assign_user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getAssignUserName() {
        return assignUserName;
    }

    public void setAssignUserName(String assignUserName) {
        this.assignUserName = assignUserName;
    }

    public List<FileBean> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileBean> fileList) {
        this.fileList = fileList;
    }

    
}
