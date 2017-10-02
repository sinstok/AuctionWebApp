/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.AuctionUserFacade;
import entities.AuctionUser;
import helpers.LoginBean;
import helpers.PasswordHash;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Sindre
 */
@Named(value = "RegisterUserView")
@RequestScoped
public class RegisterUserView {

    @EJB
    private AuctionUserFacade auctionUserFacade;
    private AuctionUser newUser;
    private String confPassword;
    private PasswordHash hashing;
    @Inject
    private LoginBean loginBean;

    /**
     * Creates a new instance of RegisterUser
     */
    public RegisterUserView() {
        this.newUser = new AuctionUser();
        this.hashing = new PasswordHash();
        this.loginBean = new LoginBean();
    }

    public AuctionUser getNewUser() {
        return newUser;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    public String registerUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (auctionUserFacade.register("email", this.newUser.getEmail())) {
            if (this.confPassword.equals(this.newUser.getPassword())) {
                try {
                    this.newUser.setSalt(hashing.generateSalt());
                    String hashedPassword = hashing.hashPassword(this.newUser.getPassword() + this.newUser.getSalt()).toString();
                    this.newUser.setPassword(hashedPassword);
                    auctionUserFacade.create(this.newUser);
                    loginBean.login(this.newUser.getId());
                    return "index?faces-redirect=true";
                } catch (Exception e) {
                    context.addMessage(null, new FacesMessage("Woops. Some of the input you wrote is wrong."));
                    return null;
                }
            } else {
                context.addMessage(null, new FacesMessage("Woops. Some of the input you wrote is wrong."));
                return null;
            }
        } else {
            context.addMessage(null, new FacesMessage("Email already exitst."));
            return null;
        }
    }

    public void logOut() {
        loginBean.logOut();
    }
}
