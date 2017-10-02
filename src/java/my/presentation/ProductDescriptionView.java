package my.presentation;

import boundary.AuctionUserFacade;
import boundary.BidFacade;
import boundary.FeedbackFacade;
import boundary.ProductFacade;
import boundary.ProductListingFacade;
import entities.AuctionUser;
import entities.Bid;
import entities.Product;
import entities.ProductListing;
import helpers.DBean;
import helpers.LoginBean;
import helpers.TimeManger;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    private String productRating;
    private String sellerRating;
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

    public ProductListing getProductListing(int id) {
        Long proListId = Long.valueOf(id);
        ProductListing proList = plFacade.find(proListId);
        return proList;
    }

    /**
     * Returns the AuctionUser who are the Seller of this productlisting
     *
     * @return
     */
    public AuctionUser getSeller() {
        return auctionUserFacade.getSeller(pl.getId());
    }

    /**
     * Returns a string value of the average rating of the product, or the
     * string "No ratings" if the product has not recived any ratings
     *
     * @return
     */
    public String getAvergeProductRating() {
        return plFacade.getAverageProductRating(pl.getProduct());
    }

    /**
     * Returns a string value of the average rating of the seller, or the string
     * "No ratings" if the seller has not recived any ratings
     *
     * @return
     */
    public String getAvergeSellerRating() {
        return plFacade.getAvergeSellerRating(this.getSeller());
    }

    /**
     * Adds a Bid from the loged in AuctionUser. If the bid is not larger then
     * the current highest bid, the bid will not be added and a error message
     * will be displayed
     *
     * @param pID
     * @return
     */
    public String addBid(int pID) {
        if (!login.isLoggedIn()) {
            return "loginPage";
        }

        AuctionUser bidder = auctionUserFacade.find(login.getUserId());
        return plFacade.addBid(bidder, pl, getSeller(), getHighestBid(), newBidValue);
    }

    /**
     * Make a Feedback from the values from the user inputs, if the Auction User
     * have bought the product
     *
     * @param pID
     * @return
     */
    public String addFeedback(int pID) {
        if (!login.isLoggedIn()) {
            return "loginPage";
        }
        AuctionUser rater = auctionUserFacade.find(login.getUserId());
        Bid highestBid = getHighestBid();
        return plFacade.addFeedback(rater, pl, highestBid, this.getProductRating(), this.comment, this.getProduct());
    }

    /**
     * Adds a rating to the seller from user input
     *
     * @param pID
     * @return
     */
    public String addSellerRating(int pID) {
        if (!login.isLoggedIn()) {
            return "loginPage";
        }
        
        AuctionUser rater = auctionUserFacade.find(login.getUserId());
        AuctionUser seller = this.getSeller();
        
        return plFacade.addSellerRating(rater, seller, this.getSellerRating());
    }

    /**
     * Returns a string that will tell the remainig bidding time of this
     * productlisting
     *
     * @return
     */
    public String getTimeLeft() {
        Date closing = this.pl.getClosing();
        Date now = new Date();
        TimeManger tm = new TimeManger();
        String time = tm.getTimeRemaining(closing, now);
        return time;
    }

    /**
     * Returns the largest bidding amount if it exsits else it will return the
     * productlistings baseprice
     *
     * @return
     */
    public Bid getHighestBid() {
        return plFacade.getHighestBid(this.pl.getBids(), pl);
    }

    /**
     * Returns a list of strings that includes a AuctionUser's Name and their
     * comment
     *
     * @return
     */
    public List<String> getAllComments() {
        return plFacade.getAllComments(this.pl.getProduct());
    }

    //GETTERS & SETTERS
    public ProductListing getPl() {
        return pl;
    }

    public void setPl(ProductListing pl) {
        this.pl = pl;
    }

    public int getID() {
        return plID;
    }

    //From product
    public Product getProduct() {
        return this.pl.getProduct();
    }

    public Product getProduct(int pID) {
        return this.getProductListing(pID).getProduct();
    }

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

    public String getProductRating() {
        return productRating;
    }

    public void setProductRating(String productRating) {
        this.productRating = productRating;
    }

    public String getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(String sellerRating) {
        this.sellerRating = sellerRating;
    }
}
