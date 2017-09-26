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

    private boolean loggedIn;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {

    }

    public boolean login(long id) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("user", id);
        loggedIn = true;
        return true;
    }

    public void logOut() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

}
