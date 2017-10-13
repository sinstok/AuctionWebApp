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
    
    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup = "jms/MyQueue")
    private Queue queue;
    
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
    public String addBid(Bid bid, ProductListing pl) {
        
        
        String text;
        try (JMSContext context = connectionFactory.createContext();) {

            text = "---- START EMAIL to customer Sean Bean ----\n"
                    + "Dear Sean Bean,\n"
                    + "Congratulations! You have won in bidding for product Saxophone.\n"
                    + "You can access the product using the following link:\n"
                    + "URL=<LINK>\n"
                    + "---- END EMAIL to customer Sean Bean ----";
            context.createProducer().send(queue, text);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred: {0}", e.toString());
        }
        
        AuctionUser seller = auctionUserFacade.getSeller(pl.getId());
        AuctionUser bidder = bid.getUser();
        Bid highestBid = productListingFacade.getHighestBid(pl);
        
        if (bidder.getId() == seller.getId()) {
            return "You cannot bid on your own product";
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

        bidder.getBids().add(pl);
        auctionUserFacade.edit(bidder);

        List<Bid> bids = pl.getBids();
        bids.add(bid);
        pl.setBids(bids);
        productListingFacade.edit(pl);

        return null;
    }    
    
    
}
