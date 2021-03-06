/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import boundary.AuctionUserFacade;
import boundary.BidFacade;
import boundary.ProductListingFacade;
import entities.AuctionUser;
import entities.Bid;
import entities.Product;
import entities.ProductListing;
import helpers.Category;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import serializers.BidObject;
import serializers.ProductListingObject;

/**
 *
 * @author Joakim
 */
@WebService(serviceName = "AuctionService")
public class AuctionServiceSOAP {

    @EJB
    private AuctionUserFacade auctionUserFacade;
    @EJB
    private BidFacade bidFacade;
    @EJB
    private ProductListingFacade ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "getBiddables")
    public List<ProductListingObject> getBiddables() {
        List<ProductListing> biddables = ejbRef.getBiddables();
        List<ProductListingObject> biddableObjects = new ArrayList<ProductListingObject>();
        for (ProductListing pl : biddables) {
            biddableObjects.add(new ProductListingObject(pl));
        }

        return biddableObjects;
    }

    @WebMethod(operationName = "makeBid")
    public String makeBid(BidObject bidObj, long productListingId, String username, String password) {

        AuctionUser bidder = auctionUserFacade.login(username, password);
        if(bidder == null){
            return "wrong username and password";
        }
        ProductListing pl = ejbRef.find(productListingId);

        if (pl == null) {
            return "no product listing with id " + productListingId + " exists";
        }

        Bid bid = new Bid();
        bid.setAmount(bidObj.getAmount());
        //bid.setBidDate(bidObj.getBidDate());

        bid.setUser(bidder);

        String returnMessage = bidFacade.addBidSOAP(bid, pl, username, password);
        if (returnMessage == null) {
            return "Bid has been successfully placed!";
        } else {
            return returnMessage;
        }

    }

    @WebMethod(operationName = "test")
    public String test() throws ServletException {
       return ejbRef.test();
        
    }

}
