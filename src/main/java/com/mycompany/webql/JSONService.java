/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.webql;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author user
 */
@Path("/json/metallica")
public class JSONService {
 
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTrackInJSON() {
            System.out.print("dcvffg");
            return "{track : 'aaa'}";
	}
}