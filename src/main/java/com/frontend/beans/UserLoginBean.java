package com.frontend.beans;

import com.backend.controllers.UserLoginController;
import com.backend.entities.UserEntity;
import com.frontend.representation.beans.UserBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author armena
 */
@ManagedBean(name = "userLoginBean")
@ViewScoped
public class UserLoginBean implements Serializable {

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    private UserLoginController controller = new UserLoginController();
    @ManagedProperty("#{userBean}")
    private UserBean userBean;
    private String name;
    private String password;
    
    public UserLoginBean() {
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();

    }

    public String login() {
        
        UserEntity userEntity = controller.getUserByParameter(name, password);
        if(userEntity != null){
            userBean.setId(userEntity.getId());
            userBean.setName(userEntity.getName());
            userBean.setRole(Long.valueOf(userEntity.getRole()));
            return "login";
        } else {
            return "logout";
        }
        
    }

    public String logout() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        externalContext.getSessionMap().remove("userContext");
        //externalContext.getSessionMap().remove("sessionController");
        HttpSession session = (HttpSession) externalContext.getSession(true);
        session.invalidate();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "logout";
    }
    
    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
   
}
