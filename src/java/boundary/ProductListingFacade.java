/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.AuctionUser;
import entities.Bid;
import entities.Feedback;
import entities.Product;
import entities.ProductListing;
import helpers.Category;
import helpers.RatingCalculator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tomas
 */
@Stateless
public class ProductListingFacade extends AbstractFacade<ProductListing> {

    @PersistenceContext(unitName = "AuctionWebAppPU")
    private EntityManager em;
    
    @Inject
    AuctionUserFacade auctionUserFacade;
    
    @Inject
    ProductListingFacade plFacade;
    
    @Inject
    ProductFacade productFacade;
    
    @Inject
    FeedbackFacade feedbackFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductListingFacade() {
        super(ProductListing.class);
    }
    
    public List<ProductListing> getBiddables(){
         Date now = new Date();
         List<ProductListing> productListings = em.createQuery(
               "SELECT pl "
               + "FROM ProductListing pl "
               + "WHERE pl.closing > :now", ProductListing.class).setParameter("now", now).getResultList();
         return productListings;
         
    }
    
    public List<ProductListing> searchBiddable(String search) 
    {
       Date now = new Date();
       List<ProductListing> productListings = em.createQuery(
               "SELECT pl "
               + "FROM ProductListing pl JOIN pl.product p "
               + "WHERE lower(pl.description) LIKE :search OR "
               + "lower(p.name) LIKE :search OR "
               + "lower(p.features) LIKE :search AND "
               + "pl.closing > :now", ProductListing.class).setParameter("search", "%" + search.toLowerCase() + "%").setParameter("now", now).getResultList();

        return productListings;
    }
    
    public List<ProductListing> getBiddableProductListingsByCategory(Category category) 
    {
       List<ProductListing> productListings = em.createQuery(
               "SELECT pl "
               + "FROM ProductListing pl JOIN pl.product p "
               + "WHERE p.category = :category", ProductListing.class).setParameter("category", category).getResultList();

        return productListings;
    }

    public String getAverageProductRating(Product prod) {
        List<Feedback> feeds = prod.getFeedbacks();
        List<Double> ratings = new ArrayList<>();
        int size = feeds.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (feeds.get(i).getRating() != 0.0) {
                    ratings.add(feeds.get(i).getRating());
                }
            }
            RatingCalculator rc = new RatingCalculator();
            Double rating = rc.calcuatedRating(ratings);
            if (rating > 0) {
                return rating.toString();
            }
        }
        return "No ratings";
    }

    public String getAvergeSellerRating(AuctionUser seller) {
        List<Feedback> feeds = seller.getFeedbacks();
        List<Double> ratings = new ArrayList<>();
        int size = feeds.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (feeds.get(i).getRating() != 0.0) {
                    ratings.add(feeds.get(i).getRating());
                }
            }
            RatingCalculator rc = new RatingCalculator();
            Double rating = rc.calcuatedRating(ratings);
            if (rating > 0) {
                return rating.toString();
            }
        }
        return "No ratings";
    }

    public String addBid(AuctionUser bidder, ProductListing pl, AuctionUser seller, Bid highestBid, double newBidValue) {
        if (bidder.getId() == seller.getId()) {
            FacesMessage msg = new FacesMessage("You cannot bid on your own product", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        if (pl == null) {
            FacesMessage msg = new FacesMessage("No productlisting", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        if (pl.getClosing().before(new Date())) {
            FacesMessage msg = new FacesMessage("Bidding has closed", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        if (newBidValue < highestBid.getAmount()) {
            FacesMessage msg = new FacesMessage("Bid too low", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        } else if (newBidValue == highestBid.getAmount() && highestBid.getUser() != null) {
            FacesMessage msg = new FacesMessage("Someone already made that bid", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        Bid newBid = new Bid();
        newBid.setAmount(newBidValue);
        newBid.setUser(bidder);

        bidder.getBids().add(pl);
        auctionUserFacade.edit(bidder);

        List<Bid> bids = pl.getBids();
        bids.add(newBid);
        pl.setBids(bids);
        plFacade.edit(pl);
       
        return null;
    }

    public List<String> getAllComments(Product prod) {
        List<Feedback> feeds = prod.getFeedbacks();
        List<String> comments = new ArrayList<>();
        if (!(feeds.isEmpty())) {
            for (int i = 0; i < feeds.size(); i++) {
                comments.add(feeds.get(i).getRater().getName() + ": " + feeds.get(i).getFeedback());
            }
        }
        return comments;
    }

    public Bid getHighestBid(List<Bid> bids, ProductListing pl) {
        Bid highestBid = new Bid();
        highestBid.setAmount(pl.getBasePrice());

        if (!(bids.isEmpty())) {
            for (int i = 0; i < bids.size(); i++) {
                double current = bids.get(i).getAmount();
                if (current > highestBid.getAmount()) {
                    highestBid = bids.get(i);
                }
            }
        }
        return highestBid;
    }

    public String addFeedback(AuctionUser rater, ProductListing pl, Bid highestBid, String rating, String comment, Product product) {
        Date now = new Date();
        Date closing = pl.getClosing();

        if (highestBid.getUser() == null || highestBid.getUser().getId() != rater.getId() || closing.after(now)) {
            FacesMessage msg = new FacesMessage("You must purchase the product before adding feedback", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        Feedback oldFeedback = pl.getProduct().getFeedbackOfUser(rater.getId());
        if (oldFeedback != null) {
            oldFeedback.setRating(Double.parseDouble(rating));
            oldFeedback.setFeedback(comment);
            feedbackFacade.edit(oldFeedback);
            return null;
        }

        Product prod = null;
        if (product == null) {
            FacesMessage msg = new FacesMessage("product is null", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "index";
        } else {
            prod = product;
            Feedback feed = new Feedback();
            String com = comment;

            feed.setRater(rater);
            feed.setRating(Double.parseDouble(rating));
            feed.setFeedback(com);
            List<Feedback> feeds = prod.getFeedbacks();
            feeds.add(feed);
            prod.setFeedbacks(feeds);
            productFacade.edit(prod);
            feedbackFacade.create(feed);
        }
        return null;
    }
    
    
}
