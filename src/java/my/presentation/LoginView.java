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
import java.io.IOException;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Session bean handles both when a user log in and when a user logs out of
 * the auction web app.
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

    /**
     * Here we send the email and the password to the auctionUser facade to
     * check if the input exists in the database. If something wrong happens, we
     * have appropriate error messages so that the user can se what went wrong,
     * either if it was wrong input or something else happend. If everything is
     * correct we send the id of the user to the loginBean and create a session
     * with that user id as a parameter.
     *
     * @return Either you return to the indext or the previous page that
     * redirected you to the login page.
     */
    public String login() {
        AuctionUser user = auctionUserFacade.login(email, password);
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        if (user != null) {
            long id = user.getId();
            if (id != 0) {
                try {
                    request.login(user.getEmail(), password + user.getSalt());
                    request.authenticate(response);
                    return "/faces/index?faces-redirect=true";
                } catch (ServletException e) {
                    return "/faces/loginPage.xhtml";
                } catch (IOException i){
                    return "/faces/loginPage.xhtml";
                }
                /*if (loginBean.login(id)) {
                    return "index?faces-redirect=true";
                } else {
                    FacesMessage msg = new FacesMessage("Wrong user input!", "ERROR MSG");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return "loginPage";
                }*/
            } else {
                FacesMessage msg = new FacesMessage("Wrong user input!", "ERROR MSG");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return "/faces/loginPage";
            }
        } else {
            FacesMessage msg = new FacesMessage("Wrong user input!", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "/faces/loginPage";
        }
    }

    /**
     * call the loginbean in order to invalidate the session.
     *
     * @return goes back to the index
     */
    public String logOut() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try{
            request.logout();
            //loginBean.logOut();
            return "/faces/index";
        } catch (ServletException e){
            return "/faces/index";
        }
    }

    /**
     *
     * @return Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email that the user typed in the input field.
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password the user typed in the input field.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * If the user is logged in we redirect him to his user profile page. If not
     * then we redirect him to the index page. We pass the user id as a request
     * parameter so that we can find it in the userprofile view.
     *
     * @return either index page or the userprofile page
     */
    public String toUserProfile() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (/*loginBean.isLoggedIn()*/ ec.isUserInRole("user")) {
            //ec.getRequestMap().put("userId", loginBean.getUserId());
            return "/profile/userProfile.xhtml";
        } else {
            return "/faces/index";
        }
    }
}
