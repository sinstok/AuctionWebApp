/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.AuctionUserFacade;
import entities.AuctionUser;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import helpers.LoginBean;
import helpers.PasswordHash;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Sindre
 */
@Named(value = "LoginView")
@RequestScoped
public class LoginView {

    @EJB
    private final AuctionUserFacade auctionUserFacade;

    @Inject
    private LoginBean loginBean;

    private final PasswordHash hash;
    private String email;
    private String password;

    /**
     * Creates a new instance of LoginView
     */
    public LoginView() {
        auctionUserFacade = new AuctionUserFacade();
        hash = new PasswordHash();
    }

    public String login() {
        AuctionUser user = auctionUserFacade.login(email, password);
        long id = 0;
        id = user.getId();

        if (id != 0) {
            if (loginBean.login(id)) {
                return "index?faces-redirect=true";
            } else {
                FacesMessage msg = new FacesMessage("Wrong user input!", "ERROR MSG");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return "loginPage";
            }
        } else {
            FacesMessage msg = new FacesMessage(user.getSalt(), "ERROR MSG");
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

    public String toUserProfile() {
        if (loginBean.isLoggedIn()) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getRequestMap().put("userId", loginBean.getUserId());
            return "userProfile";
        } else {
            return "index";
        }
    }
}
