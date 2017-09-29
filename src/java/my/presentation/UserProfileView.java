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
import javax.faces.context.FacesContext;

/**
 *
 * @author Sindre
 */
@Named(value = "UserProfileView")
@RequestScoped
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
