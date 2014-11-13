package com.backend.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hayk Harutyunyan
 */
@Entity
@Table(name = "task")
@XmlRootElement
public class TaskEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 500)
    @Column(name = "description")
    private String description;
    @Column(name = "status_id")
    private Long status_id;
    @Column(name = "priority_id")
    private Long priority_id;
    @Column(name = "assign_user_id")
    private Long assign_user_id;
            
    public TaskEntity() {
    }

    public TaskEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Long status_id) {
        this.status_id = status_id;
    }

    public Long getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(Long priority_id) {
        this.priority_id = priority_id;
    }

    public Long getAssign_user_id() {
        return assign_user_id;
    }

    public void setAssign_user_id(Long assign_user_id) {
        this.assign_user_id = assign_user_id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 23 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 23 * hash + (this.status_id != null ? this.status_id.hashCode() : 0);
        hash = 23 * hash + (this.priority_id != null ? this.priority_id.hashCode() : 0);
        hash = 23 * hash + (this.assign_user_id != null ? this.assign_user_id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaskEntity other = (TaskEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        
        if (this.status_id != other.status_id && (this.status_id == null || !this.status_id.equals(other.status_id))) {
            return false;
        }
        if (this.priority_id != other.priority_id && (this.priority_id == null || !this.priority_id.equals(other.priority_id))) {
            return false;
        }
        if (this.assign_user_id != other.assign_user_id && (this.assign_user_id == null || !this.assign_user_id.equals(other.assign_user_id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TaskEntity{" + "id=" + id + ", name=" + name + ", description=" + description + ", status_id=" + status_id + ", priority_id=" + priority_id + ", assign_user_id=" + assign_user_id + '}';
    }

    
    
}
