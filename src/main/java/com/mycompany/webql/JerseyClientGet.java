/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.webql;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 *
 * @author user
 */
public class JerseyClientGet {
    public static void main(String[] args) {
	try {
 
		Client client = Client.create();
                WebResource webResource = client
		   .resource("http://localhost:9191/taskmanag/rest/json/hayk/1");
 /*
		WebResource webResource = client
		   .resource("http://localhost:9191/taskmanag/rest/json/metallica/get");
 

 WebResource webResource = client
		   .resource("http://api.goeuro.com/api/v2/position/suggest/en/Yerevan");*/
 
		ClientResponse response = webResource.accept("application/json")
                   .get(ClientResponse.class);
 
		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatus());
		}
 
		String output = response.getEntity(String.class);
 
		System.out.println("Output from Server .... \n");
		System.out.println(output);
 
	  } catch (Exception e) {
 
		e.printStackTrace();
 
	  }
 
	}
}
