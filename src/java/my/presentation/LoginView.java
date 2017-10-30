/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.AuctionUserFacade;
import boundary.ProductListingFacade;
import entities.AuctionUser;
import entities.ProductListing;
import helpers.PasswordHash;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Session bean handles both when a user log in and when a user logs out of
 * the auction web app.
 *
 * @author Sindre
 */
//@Named(value = "LoginView")
@ManagedBean(name = "LoginView")
@ViewScoped
public class LoginView implements Serializable {

    @EJB
    private final AuctionUserFacade auctionUserFacade;
    @Inject
    private ProductListingFacade plf;

    private final PasswordHash hash;
    private String email;
    private String password;
    private String path;

    /**
     * Creates a new instance of LoginView
     */
    public LoginView() {
        auctionUserFacade = new AuctionUserFacade();
        hash = new PasswordHash();
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ProductListing pl = (ProductListing) ec.getSessionMap().get("pl");
        if (pl != null) {
            ec.getRequestMap().put("productListing", pl);
        }
        if (ec.getSessionMap().get("FromPage") != null) {
            if (ec.getSessionMap().get("FromPage").equals("flow-productCreation")) {
                this.path = "flow-productCreation";
                ec.getSessionMap().remove("FromPage");
            } else {
                this.path = ec.getRequestContextPath() + "/faces/" + ec.getSessionMap().get("FromPage");
                ec.getSessionMap().remove("FromPage");
            }
        } else {
            this.path = (String) ec.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
            if (this.path == null) {
                this.path = ec.getRequestContextPath() + "/faces/index.xhtml";
            } else {

                String originalQuery = (String) ec.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);
                if (originalQuery != null) {
                    this.path += "?" + originalQuery;
                }
            }
        }
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
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        //System.out.println();
        if (user != null) {
            //long id = user.getId();
            //if (id != 0) {
            try {
                request.login(user.getEmail(), password + user.getSalt());
                HttpServletResponse response = (HttpServletResponse) ec.getResponse();
                request.authenticate(response);

                System.out.println("This is the path you will be redirected to: " + this.path);
                if (this.path.equals("flow-productCreation")) {
                    return this.path;
                } else {
                    ec.redirect(this.path);
                    return null;
                }
            } catch (ServletException | IOException e) {
                return null;
            }
            /*if (loginBean.login(id)) {
                return "index?faces-redirect=true";
                } else {
                FacesMessage msg = new FacesMessage("Wrong user input!", "ERROR MSG");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return "loginPage";
                }*/
 /*if (loginBean.login(id)) {
                    return "index?faces-redirect=true";
                } else {
                    FacesMessage msg = new FacesMessage("Wrong user input!", "ERROR MSG");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return "loginPage";
                }*/
 /*} else {
                FacesMessage msg = new FacesMessage("Wrong user input!", "ERROR MSG");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return "/faces/loginPage.xhtml";
            }*/
        } else {
            FacesMessage msg = new FacesMessage("Username or password is wrong", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/loginPage.xhtml");
            } catch (IOException i) {
                System.out.println("Problem redirecting from loginPage");
            }
            return null;
        }
    }

    /**
     * call the loginbean in order to invalidate the session.
     *
     * @return goes back to the index
     */
    public void logOut() throws IOException, ServletException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        request.logout();
        //loginBean.logOut();
        //return "/faces/index.xhtml";
        ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
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
     * @throws java.io.IOException
     */
    public void toUserProfile() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/profile/userProfile.xhtml");
    }

    public void toLoginPage() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/loginPage.xhtml");
    }

    public void toRegisterUserPage() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/registerUser.xhtml");
    }
}
