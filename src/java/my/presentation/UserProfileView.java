/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.AuctionUserFacade;
import boundary.ProductListingFacade;
import entities.AuctionUser;
import entities.Bid;
import entities.ProductListing;
import java.io.IOException;
import java.util.Date;
import java.util.List;
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
    @EJB
    private ProductListingFacade productListingFacade;
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

    public AuctionUser getUser() {
        return user;
    }

    public String isHighestBidder(ProductListing listing) {
        return auctionUserFacade.isHighestBidder(listing, user);
    }
    
    public void publishListing(ProductListing listing){
        listing.setPublished(new Date());
        productListingFacade.edit(listing);
    }
}
