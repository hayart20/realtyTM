/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webql;

import com.backend.controllers.TaskController;
import com.frontend.representation.beans.TaskBean;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author user
 */
@Path("/json/hayk")
public class NewClass {
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTrackInJSON(@PathParam("id") String id) {
        TaskController controller = new TaskController();
        TaskBean filterTaskBean = new TaskBean();
        filterTaskBean.setAssign_user_id(id);
        List<TaskBean> allTask = controller.getAllTask(filterTaskBean);
        StringBuilder str = new StringBuilder();
        for(TaskBean bean : allTask){
            str.append(" {").append("taskname : ").append(bean.getName()).append("} ");
        }
        return str.toString();
    }
}
