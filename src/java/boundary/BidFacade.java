/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.AuctionUser;
import entities.Bid;
import entities.ProductListing;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tomas
 */
@DeclareRoles({"user"})
@Stateless
public class BidFacade extends AbstractFacade<Bid> {

    @PersistenceContext(unitName = "AuctionWebAppPU")
    private EntityManager em;
    
    @Inject
    AuctionUserFacade auctionUserFacade;
    
    @Inject
    ProductFacade productFacade;
    
    @Inject
    ProductListingFacade productListingFacade;
   
    
    static final Logger logger = Logger.getLogger("Main");
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BidFacade() {
        super(Bid.class);
    }
    
    
    /**
     * Place a new bid for a productlisting
     * @param bidder
     * @param pl
     * @param seller
     * @param highestBid
     * @param newBidValue
     * @return String of a potensial error message or null
     */
    //@RolesAllowed("user")
    public String addBid(Bid bid, ProductListing pl) {
        
        AuctionUser seller = auctionUserFacade.getSeller(pl.getId());
        AuctionUser bidder = bid.getUser();
        Bid highestBid = productListingFacade.getHighestBid(pl);
        
        if (bidder.getId() == seller.getId()) {
            return "You cannot bid on your own product";
        }
        
        if(highestBid.getUser() != null && bid.getUser().getId() == highestBid.getUser().getId()){
            return "You already have the highest bid";
        }

        if (pl == null) {
            return "No productlisting";
        }
        if (pl.getClosing().before(new Date())) {
            return "Bidding has closed";
        }

        if (bid.getAmount() <= highestBid.getAmount()) {
            return "Bid too low";
        } else if (bid.getAmount() == highestBid.getAmount() && highestBid.getUser() != null) {
            return "Someone already made that bid";
        }
        
        create(bid);
        
        bidder.getBids().add(pl);
        auctionUserFacade.edit(bidder);

        List<Bid> bids = pl.getBids();
        bids.add(bid);
        pl.setBids(bids);
        productListingFacade.edit(pl);

        return null;
    }    
    
    /*
    For use with SOAP, since we use form authentication
    */
    public String addBidSOAP(Bid bid, ProductListing pl, String username, String password){
        AuctionUser user = auctionUserFacade.login(username, password);
        if(user.getRole().equals("user")) return addBid(bid, pl);
        else {
            return "unauthorized: role 'user' required";
        }
    }
    
    
}
