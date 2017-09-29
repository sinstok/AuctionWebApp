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
import helpers.RatingCalculator;
import helpers.TimeManger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Tomas
 */
@Named(value = "PDView")
@RequestScoped
public class ProductDescriptionView implements Serializable {

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
    private DBean dbi;

    private Product product;
    private String value;
    private String comment;
    private String rating;
    private ProductListing pl;
    private int plID;

    /**
     * Creates a new instance of SomeView
     */
    public ProductDescriptionView() {
        this.pl = new ProductListing();
        this.product = new Product();
    }

    //From productlisting
    public ProductListing getProductListing(int id) {
        Long proListId = Long.valueOf(id);
        ProductListing proList = plFacade.find(proListId);
        return proList;
    }

    public String toProductListing(int id) {
        plID = id;
        Long proListId = new Long(id);
        this.pl = plFacade.find(proListId);
        if (pl != null) {
            return "productdescription";
        } else {
            return "index.xhtml";
        }
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
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
    //

    public AuctionUser getSeller() {
        /*Må finnes en seller før denne funker
        
        AuctionUser sell =  auctionUserFacade.getSeller("listings_id", Long.valueOf(plID);
        if(sell != null){
            return sell;
        } else {*/

        //Placeholder
        AuctionUser us = new AuctionUser();
        us.setName("Sindre");
        return us;
        //}
    }

    public String getProductRating() {
        //Product prod = this.getProduct();
        Product prod = this.pl.getProduct();
        List<Feedback> feeds = prod.getFeedbacks();
        List<Double> ratings = new ArrayList<>();
        int size = feeds.size();
        if (size != 0) {
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
    public void addBid(int pID) {
        
        Bid newBid = new Bid();
        newBid.setAmount(Double.parseDouble(this.getValue()));
        //ProductListing prolis = this.getProductListing(this.plID);
        if (pl == null) {
            FacesMessage msg = new FacesMessage("ProductListing er null", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            List<Bid> bids = pl.getBids();
            bids.add(newBid);
            pl.setBids(bids);
            plFacade.edit(pl);
            bidFacade.create(newBid);
        }
        
        this.toProductListing(pID);
    }

    //Må legge til innlogget bruker
    public void addFeedback(int pID) {
        plID = pID;
        Product prod = null; //this.getProduct();
        if (this.getProduct() == null) {
            FacesMessage msg = new FacesMessage("product er null", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            prod = this.getProduct();
            Feedback feed = new Feedback();
            String com = this.getComment();
            //Placholder user
            AuctionUser rater = new AuctionUser();
            rater.setName("No user");
            //AuctionUser rater = auctionUserFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
            auctionUserFacade.create(rater);
            feed.setRater(rater);
            feed.setRating(Double.parseDouble(this.getRating()));
            feed.setFeedback(com);
            List<Feedback> feeds = prod.getFeedbacks();
            feeds.add(feed);
            prod.setFeedbacks(feeds);
            productFacade.edit(prod);
            feedbackFacade.create(feed);
        }

    }

    public String getTimeLeft() {
        Date closing = this.pl.getClosing();
        Date now = new Date();
        TimeManger tm = new TimeManger();
        String time = tm.getTimeRemaining(closing, now);
        return time;
    }

    public double getHighestBid() {
        List<Bid> bids = this.pl.getBids();
        double b = this.pl.getBasePrice();
        if (!(bids.isEmpty())) {
            for (int i = 0; i <= bids.size() - 1; i++) {
                double current = bids.get(i).getAmount();
                if (current > b) {
                    b = current;
                }
            }
        }
        return b;
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

    public String getSellerName() {
        String name = this.getSeller().getName();
        return name;
    }
}
