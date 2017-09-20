/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import boundary.AuctionUserFacade;
import boundary.BidFacade;
import boundary.FeedbackFacade;
import boundary.ProductFacade;
import boundary.ProductListingFacade;
import entities.AuctionUser;
import entities.Bid;
import entities.Feedback;
import entities.Product;
import entities.ProductListing;
import java.time.LocalDate;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Joakim
 */
@Named(value = "dBean")
@RequestScoped
public class DBean {

     @EJB
    private AuctionUserFacade userFacade;
     
     @EJB
    private FeedbackFacade feedbackFacade;
     
     @EJB
    private ProductFacade productFacade;
     
     @EJB
    private ProductListingFacade productListingFacade;
    
     
     
    
    public void test(){
        //Sellers
        AuctionUser seller = new AuctionUser();
        seller.setName("Arne Seller");
        AuctionUser customer = new AuctionUser();
        customer.setName("Nilsen Customer");
        AuctionUser customer2 = new AuctionUser();
        customer.setName("Rekdal Customer");
        
        userFacade.create(customer);
        userFacade.create(seller);
        userFacade.create(customer2);
        
        
        //Feedback
        Feedback feedback = new Feedback();
        feedback.setFeedback("brabrabra!");
        feedback.setRating(4.5);
        feedback.setRater(customer);
        seller.addFeedback(feedback);
        userFacade.edit(seller);
        
        //Products
        Product prod1 = new Product();
        prod1.setFeatures("nice");
        prod1.setName("Book");
        productFacade.create(prod1);
        
        Product prod2 = new Product();
        prod1.setFeatures("cool");
        prod1.setName("Island");
        productFacade.create(prod2);
        
        
        
        ProductListing pl1 = new ProductListing();
        pl1.setBasePrice(3);
        pl1.setClosing(new Date());
        pl1.setPublished(new Date());
        pl1.setDescription("a little rough around the edges");
        prod1.addListing(pl1);
        productFacade.edit(prod1);
        seller.addListing(pl1);
        userFacade.edit(seller);
        
           
        //Bids
        Bid b1 = new Bid();
        b1.setAmount(30);
        b1.setBidDate(new Date());
        b1.setUser(customer);
        
        Bid b2 = new Bid();
        b2.setAmount(35);
        b2.setBidDate(new Date());
        b2.setUser(customer2);
        
        pl1.addBid(b1);
        pl1.addBid(b2);
        productListingFacade.edit(pl1);
        
        
        
        
        
    }
    
}
