/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *This bean keeps track of the session of a user on the auction web app.
 * @author Sindre
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private long userId;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        userId = 0;
    }

    /**
     * When a user logs in we store the user id in the session so that we can find the user later in the database if needed.
     * @param id. The id of the user that is logged in.
     * @return true if the user successfully logs in.
     */
    public boolean login(long id) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("user", id);
        userId = id;
        return true;
    }

    /**
     * Invalidates the session when the user logs out.
     */
    public void logOut() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        userId = 0;
    }

    /**
     * Checks wheter the user id is stored in session.
     * @return If it's stored in the session it means he is logged in and returns true, if not then it returns false.
     */
    public boolean isLoggedIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get("user") != null;
    }

    /**
     * 
     * @return the id of the user that is logged in.
     */
    public long getUserId() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(context.getExternalContext().getSessionMap().get("user") == null){
            return -1;
        }
        return (long) context.getExternalContext().getSessionMap().get("user");
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
