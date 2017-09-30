package my.presentation;

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
import helpers.DBean;
import helpers.LoginBean;
import helpers.RatingCalculator;
import helpers.TimeManger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Tomas
 */
@ManagedBean(name = "PDView")
@ViewScoped
public class ProductDescriptionView implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    AuctionUserFacade auctionUserFacade;

    @EJB
    ProductFacade productFacade;

    @EJB
    ProductListingFacade plFacade;

    @EJB
    BidFacade bidFacade;

    @EJB
    FeedbackFacade feedbackFacade;

    @Inject
    LoginBean login;

    @Inject
    private DBean dbi;

    private Product product;
    private double newBidValue;
    private String comment;
    private String rating;
    private ProductListing pl;
    private int plID;

    /**
     * Creates a new instance of SomeView
     */
    public ProductDescriptionView() {
        this.pl = null;
        this.product = new Product();
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        this.pl = (ProductListing) ec.getRequestMap().get("productListing");
    }

    //From productlisting
    public ProductListing getProductListing(int id) {
        Long proListId = Long.valueOf(id);
        ProductListing proList = plFacade.find(proListId);
        return proList;
    }

    public AuctionUser getSeller() {
        return auctionUserFacade.getSeller(pl.getId());
    }

    public String getProductRating() {
        //Product prod = this.getProduct();
        Product prod = pl.getProduct();
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

    //Må legge til innlogget bruker
    public String addBid(int pID) {
        if (!login.isLoggedIn()) {
            return "loginPage";
        }

        AuctionUser bidder = auctionUserFacade.find(login.getUserId());
        if (bidder.getId() == getSeller().getId()) {
            FacesMessage msg = new FacesMessage("You cannot bid on your own product", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        //ProductListing prolis = this.getProductListing(this.plID);
        if (pl == null) {
            FacesMessage msg = new FacesMessage("No productlisting", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        if(pl.getClosing().before(new Date())){
             FacesMessage msg = new FacesMessage("Bidding has closed", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        
        Bid highestBid = getHighestBid();
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
        newBid.setAmount(this.newBidValue);
        newBid.setUser(bidder);

        List<Bid> bids = pl.getBids();
        bids.add(newBid);
        pl.setBids(bids);
        plFacade.edit(pl);
        //bidFacade.create(newBid);

        return null;
    }

    //Må legge til innlogget bruker
    public String addFeedback(int pID) {
        if (!login.isLoggedIn()) {
            return "loginPage";
        }

        AuctionUser rater = auctionUserFacade.find(login.getUserId());
        Date now = new Date();
        Date closing = pl.getClosing();
        Bid highestBid = getHighestBid();

        if (highestBid.getUser() == null || highestBid.getUser().getId() != rater.getId() || closing.after(now)) {
            FacesMessage msg = new FacesMessage("You must purchase the product before adding feedback", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        
        Feedback oldFeedback = pl.getProduct().getFeedbackOfUser(rater.getId());
        if(oldFeedback != null){
            oldFeedback.setRating(Double.parseDouble(this.getRating()));
            oldFeedback.setFeedback(comment);
            feedbackFacade.edit(oldFeedback);
            return null;    
        }

        Product prod = null; //this.getProduct();
        if (this.getProduct() == null) {
            FacesMessage msg = new FacesMessage("product is null", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "index";
        } else {
            prod = this.getProduct();
            Feedback feed = new Feedback();
            String com = this.getComment();

            feed.setRater(rater);
            feed.setRating(Double.parseDouble(this.getRating()));
            feed.setFeedback(com);
            List<Feedback> feeds = prod.getFeedbacks();
            feeds.add(feed);
            prod.setFeedbacks(feeds);
            productFacade.edit(prod);
            feedbackFacade.create(feed);
        }

        return null;

    }

    public String getTimeLeft() {
        Date closing = this.pl.getClosing();
        Date now = new Date();
        TimeManger tm = new TimeManger();
        String time = tm.getTimeRemaining(closing, now);
        return time;
    }

    public Bid getHighestBid() {
        List<Bid> bids = this.pl.getBids();
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

    public List<String> getAllComments() {
        //Product prod = this.getProduct();
        Product prod = this.pl.getProduct();
        List<Feedback> feeds = prod.getFeedbacks();
        List<String> comments = new ArrayList<>();
        if (!(feeds.isEmpty())) {
            for (int i = 0; i < feeds.size(); i++) {
                comments.add(feeds.get(i).getRater().getName() + ": " + feeds.get(i).getFeedback());
            }
        }
        return comments;
    }

    public ProductListing getPl() {
        return pl;
    }

    public void setPl(ProductListing pl) {
        this.pl = pl;
    }

    public int getID() {
        return plID;
    }

    public String getDescription() {
        return this.pl.getDescription();
    }

    public byte[] getImage() {
        return this.pl.getImage();
    }

    public Date getPublished() {
        return this.pl.getPublished();
    }

    public Date getClosing() {
        return this.pl.getClosing();
    }
    //

    //From product
    public Product getProduct() {
        return this.pl.getProduct();
    }

    public Product getProduct(int pID) {
        return this.getProductListing(pID).getProduct();
    }

    public String getThisProduct() {
        Product prod = this.getProduct();
        return prod.getName();
    }

    public String getThisProductFeats() {
        Product prod = this.getProduct();
        return prod.getFeatures();
    }
    //

    //From userinput
    public double getValue() {
        return newBidValue;
    }

    public void setValue(double value) {
        this.newBidValue = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
