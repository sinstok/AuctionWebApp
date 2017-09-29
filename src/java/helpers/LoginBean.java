/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Sindre
 */
@Named
@SessionScoped
public class LoginBean implements Serializable {

    private long userId;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        userId = 0;
    }

    public boolean login(long id) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("user", id);
        userId = id;
        return true;
    }

    public void logOut() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        userId = 0;
    }

    public boolean isLoggedIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get("user") != null;
    }

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
