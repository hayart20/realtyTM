/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frontend.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;

/**
 *
 * @author userr
 */
@ManagedBean(name = "uploadBean")
@ViewScoped
public class UploadBean implements Serializable {

    private Part file;
    private String fileContent;

    public void upload() {
        try {
            fileContent = new Scanner(file.getInputStream())
                    .useDelimiter("\\A").next();
            System.out.print(file.getName());
        } catch (IOException e) {
            // Error handling
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
}
