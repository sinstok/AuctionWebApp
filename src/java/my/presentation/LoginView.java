/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.AuctionUserFacade;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import helpers.LoginBean;
import helpers.PasswordHash;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sindre
 */
@Named(value = "LoginView")
@RequestScoped
public class LoginView {

    @EJB
    private final AuctionUserFacade auctionUserFacade;
    
    private final LoginBean loginBean;
    private final PasswordHash hash;
    private String email;
    private String password;

    /**
     * Creates a new instance of LoginView
     */
    public LoginView() {
        auctionUserFacade = new AuctionUserFacade();
        loginBean = new LoginBean();
        hash = new PasswordHash();
    }

    public String login() {
        long id = 0;
        try {
            id = auctionUserFacade.login(email, hash.hashPassword(password).toString());
        } catch (Exception e) {

        }
        if (id != 0) {
            if (loginBean.login(id)) {
                return "index?faces-redirect=true";
            } else {
                FacesMessage msg = new FacesMessage("Wrong user input!", "ERROR MSG");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return "loginPage";
            }
        } else {
            FacesMessage msg = new FacesMessage("Wrong user input!", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "loginPage";
        }
    }

    public String logOut() {
        loginBean.logOut();
        return "index";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
