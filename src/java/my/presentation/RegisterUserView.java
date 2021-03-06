/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.AuctionUserFacade;
import entities.AuctionUser;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    //private PasswordHash hashing;
    private String password;

    /**
     * Creates a new instance of RegisterUser
     */
    public RegisterUserView() {
        this.newUser = new AuctionUser();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String registerUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (auctionUserFacade.register("email", this.newUser.getEmail())) {
            if (this.confPassword.equals(this.password)) {
                try {
                    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                    AuctionUser user = auctionUserFacade.registerUser(this.newUser, this.password);
                    request.login(user.getEmail(), this.password + user.getSalt());
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
                    return null;
                } catch (Exception e) {
                    System.out.println("There was a problem hashing the password " + e.getMessage());
                    context.addMessage(null, new FacesMessage("Woops. Some of the input you wrote is wrong."));
                    return null;
                }
            } else {
                context.addMessage("confPassword", new FacesMessage("Passwords does not match"));
                return null;
            }
        } else {
            context.addMessage(null, new FacesMessage("Email already exitst."));
            return null;
        }
    }
}
