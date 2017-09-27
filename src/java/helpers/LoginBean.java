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

/**
 *
 * @author Sindre
 */
@ManagedBean
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
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
