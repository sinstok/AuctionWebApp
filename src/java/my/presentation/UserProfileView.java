/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.AuctionUserFacade;
import entities.AuctionUser;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sindre
 */
@ManagedBean(name = "UserProfileView")
@ViewScoped
public class UserProfileView {

    @EJB
    private AuctionUserFacade auctionUserFacade;
    private AuctionUser user;

    /**
     * Creates a new instance of UserProfileView
     */
    public UserProfileView() {
        user = new AuctionUser();
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Long userId = (Long) ec.getRequestMap().get("userId");
        if (userId == null) {
            try {
                ec.redirect("loginPage.xhtml");
            } catch (IOException e) {

            }
        } else {
            this.user = auctionUserFacade.find(userId);
        }
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getUserName() {
        return user.getName();
    }

    public String findUser(Long id) {
        if (id != null) {
            this.user = auctionUserFacade.find(id);
            return "userProfile";
        } else {
            return "index?faces-redirect=true";
        }
    }

    public AuctionUser getUser() {
        return user;
    }
}
