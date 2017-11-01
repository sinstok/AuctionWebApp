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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    ProductFacade productFacade;

    @Inject
    FeedbackFacade feedbackFacade;

    @Resource
    TimerService timerService;

    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/myQueue")
    private Queue queue;
    @Resource(lookup = "jms/myTopic")
    private Topic topic;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductListingFacade() {
        super(ProductListing.class);
    }

    @Timeout
    public void timeout(Timer timer) {
        String text;
        JMSContext context = connectionFactory.createContext();
        
        ProductListing pl = find((long)timer.getInfo());
        Bid bid = getHighestBid(pl);
        
        if(bid.getUser() == null){
            text = "bidding on product listing " + pl.getId() + " has closed without bids";
        } else {
             text = "---- START EMAIL to customer " + bid.getUser().getName() + " ----\n"
                + "Dear " + bid.getUser().getName() + ",\n"
                + "Congratulations! You have won in bidding for product " + pl.getProduct().getName() + ".\n"
                + "You can access the product using the following link: " + "https://localhost:8181/AuctionWebApp/faces/profile/userProfile.xhtml" + "\n"
                + " \n"
                + "---- END EMAIL to customer " + bid.getUser().getName() + " ----";
        }
        context.createProducer().send(queue, text);
        context.createProducer().send(topic, text);

    }
    
    public String test() throws ServletException{
        String text;
         AuctionUser user = auctionUserFacade.login("joakim@test.com", "123456");
        if (user.getRole().equals("user")) {
            return "oojoj";
        }
        return "unauthorized";
    }
    
    @RolesAllowed("user")
    @Override
    public void create(ProductListing productListing) {
        
        em.persist(productListing);
        TimerConfig tc = new TimerConfig();
        tc.setInfo(productListing.getId());
        Timer timer = timerService.createSingleActionTimer(productListing.getClosing(), tc);
    }

    //@RolesAllowed("user")
    public List<ProductListing> getBiddables() {
        Date now = new Date();
        List<ProductListing> productListings = em.createQuery(
                "SELECT pl "
                + "FROM ProductListing pl "
                + "WHERE pl.closing > :now",
                ProductListing.class).setParameter("now", now)
                .getResultList();
        return productListings;

    }

    public List<ProductListing> searchBiddable(String search) {
        Date now = new Date();
        List<ProductListing> productListings = em.createQuery(
                "SELECT pl "
                + "FROM ProductListing pl JOIN pl.product p "
                + "WHERE (lower(pl.description) LIKE :search OR "
                + "lower(p.name) LIKE :search OR "
                + "lower(p.features) LIKE :search) AND "
                + "pl.closing > :now",
                ProductListing.class).setParameter("search", "%" + search.toLowerCase() + "%")
                .setParameter("now", now)
                .getResultList();
        return productListings;
    }

    public List<ProductListing> getBiddableProductListingsByCategory(Category category) {
        Date now = new Date();
        List<ProductListing> productListings = em.createQuery(
                "SELECT pl "
                + "FROM ProductListing pl JOIN pl.product p "
                + "WHERE p.category = :category AND "
                + "pl.closing > :now",
                ProductListing.class).setParameter("category", category)
                .setParameter("now", now)
                .getResultList();
        return productListings;
    }

    /**
     * Find the average rating value to a product
     *
     * @param prod
     * @return String of the rating value, or "No ratings"
     */
    public String getAverageProductRating(Product prod) {
        List<Feedback> feeds = prod.getFeedbacks();
        List<Double> ratings = new ArrayList<>();
        int size = feeds.size();
        if (!ratings.isEmpty()) {
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

    /**
     * Find the average rating value to a seller
     *
     * @param seller
     * @return String of the rating value, or "No ratings"
     */
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

    /**
     * Finds all comments a product has recieved
     *
     * @param prod
     * @return List<String>
     */
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

    /**
     * Finds the higest bid of a productlisting
     *
     * @param bids
     * @param pl
     * @return Bid
     */
    public Bid getHighestBid(ProductListing pl) {
        List<Bid> bids = pl.getBids();
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
    
    public List<Bid> getBids(ProductListing pl) {
        return pl.getBids();
    }

    /**
     * Place or edits a feedback to the database
     *
     * @param rater
     * @param pl
     * @param highestBid
     * @param rating
     * @param comment
     * @param product
     * @return String of a potensial error message or null
     */
    @RolesAllowed("user")
    public String addFeedback(AuctionUser rater, ProductListing pl, Bid highestBid, String rating, String comment, Product product) {
        Date now = new Date();
        Date closing = pl.getClosing();

        if (highestBid.getUser() == null || highestBid.getUser().getId() != rater.getId() || closing.after(now)) {
            return "You must purchase the product before adding feedback";
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
            return "Product is null";
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

    /**
     * Place a seller rating in the database
     *
     * @param rater
     * @param seller
     * @param sellerRating
     * @return String of a potensial error message or null
     */

    @RolesAllowed("user")
    public String addSellerRating(AuctionUser rater, AuctionUser seller, String sellerRating) {
        Bid highestBid = new Bid();
        Feedback oldFeedback = seller.getFeedbackOfUser(rater.getId());
        Date now = new Date();
        List<ProductListing> sellerListings = seller.getListings();
        boolean allowed = false;

        if (seller == null) {
            return "Seller is null";
        } else if (rater.getId().equals(seller.getId())) {
            return "Can't give a rating to yourself";
        }

        //Goes through every seller's productlistings
        for (int i = 0; i < sellerListings.size(); i++) {
            //Check if bidding is done
            if (!allowed) {
                if (sellerListings.get(i).getClosing().before(now)) {
                    List<Bid> bids = sellerListings.get(i).getBids();
                    //Goes through every bids to the productlisting and the finds highest bid
                    for (int j = 0; j < bids.size(); j++) {
                        double bidPrice = 0;
                        if (bids.get(j).getAmount() > bidPrice) {
                            bidPrice = bids.get(j).getAmount();
                            highestBid = bids.get(j);
                        }
                    }
                    if (highestBid.getUser().getId() == rater.getId()) {
                        allowed = true;
                    }
                }
            }
        }

        if (!(allowed)) {
            return "You must buy a produt from the seller to give a rating";
        }

        if (oldFeedback != null) {
            oldFeedback.setRating(Double.parseDouble(sellerRating));
            feedbackFacade.edit(oldFeedback);
        } else {
            Feedback feed = new Feedback();

            feed.setRater(rater);
            feed.setRating(Double.parseDouble(sellerRating));
            List<Feedback> feeds = seller.getFeedbacks();
            feeds.add(feed);
            seller.setFeedbacks(feeds);
            auctionUserFacade.edit(seller);
            feedbackFacade.create(feed);
        }
        return null;
    }
}
