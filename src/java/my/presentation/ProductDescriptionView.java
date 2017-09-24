/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Tomas
 */
@Named(value = "PDView")
@RequestScoped
public class ProductDescriptionView {
    
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
    //private String time;
    //private Bid bid;
    private ProductListing pl;
    

    /**
     * Creates a new instance of SomeView
     */
    public ProductDescriptionView() {
        this.pl = new ProductListing();
        this.product = new Product();
        
        product.setName("Newman");
        product.setFeatures("no feats");
        
        pl.setBasePrice(10);
        //pl.setImage(image);
        pl.setDescription("Some text");
        
        Date d1 = new Date();
        Date d2 = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(d1); 
        c.add(Calendar.HOUR, 3);
        d2 = c.getTime();
        pl.setPublished(d1); 
        pl.setClosing(d2);
        
        //Bid bid = bidFacade.find(109);
        //Bid bid2 = bidFacade.find(108);
        Bid b1 = new Bid();
        b1.setAmount(30);
        Bid b2 = new Bid();
        b2.setAmount(35);
        List<Bid> bids = new ArrayList<>();
        bids.add(b2);
        bids.add(b1);
        pl.setBids(bids);
        
        AuctionUser rater = new AuctionUser();
        rater.setName("Gustav");
        List<Feedback> feeds = new ArrayList<>();
        Feedback f1 = new Feedback();
        f1.setFeedback("tjohei");
        f1.setRater(rater);
        feeds.add(f1);
                
        List<String> comments = new ArrayList<>();
        Product prod1 = new Product();
        
        
        List<ProductListing> prolist = new ArrayList<>();
        prolist.add(pl);
        product.setProductListings(prolist);
        product.setFeedbacks(feeds);
        
    }
    
    public ProductListing getProductListing(){
        return pl;
    }
    
    public Product getProduct(){
        return product;
    }
    
    public String getValue(){
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
    
    public String getThisProduct(){
        Product prod = this.getProduct();
        return prod.getName();
    }
    
    public String getThisProductFeats(){
        Product prod = this.getProduct();
        return prod.getFeatures();
    }
    
    //Må legge til innlogget bruker
    public void addBid(){
        Bid newBid = new Bid();
        newBid.setAmount(Double.parseDouble(this.getValue()));
        List<Bid> bids = pl.getBids();
        bids.add(newBid);
        pl.setBids(bids);
        //bidFacade.create(newBid);
    }
    
    //Må legge til innlogget bruker
    public void addComment(){
        Product prod = this.getProduct();
        Feedback feed = new Feedback();
        String com = this.getComment();
        AuctionUser rater = new AuctionUser();
        rater.setName("No user");
        feed.setRater(rater);
        feed.setFeedback(com);
        List<Feedback> feeds = prod.getFeedbacks();
        feeds.add(feed);
        prod.setFeedbacks(feeds);
        //feedbackFacade.create(feed);
    }
    
    public String getTimeLeft(){
        Date closing = pl.getClosing();
        Date now = new Date();
        String time;
        
        if(closing.after(now)){
           long diff = closing.getTime() - now.getTime();
           long min = 60000L;
           long hour = 3600000L;
           long day = 86400000L;
           if(diff < min){
                long diffSecs = diff / (1000);
                time = "There are " + Objects.toString(diffSecs, null) + " seconds left"; 
           }else if(diff < hour) {
                long diffMins = diff / (60 * 1000);
                time = "There are " + Objects.toString(diffMins, null) + " minutes left";
           }else if(diff < day){
                long diffHours = diff / (60 * 60 * 1000);
                time = "There are " + Objects.toString(diffHours, null) + " hours left";
           }else{
               long diffDays = diff / (24 * 60 * 60 * 1000);
               time = "There are " + Objects.toString(diffDays, null) + " days left";
           }
        }else{
            time = "Biding is closed";
        }
        return time;   
    }
    
    
    public double getHighestBid() {
        List<Bid> bids = pl.getBids();
        double b = pl.getBasePrice();
        if(!(bids.isEmpty())){
            for(int i = 0; i <= bids.size() - 1; i++){
                double current = bids.get(i).getAmount();
                if(current > b){
                    b = current;
                }
            }
        }
        return b;
    }
       
    public List<String> getAllComments(){
        Product prod = this.getProduct();
        List<Feedback> feeds = prod.getFeedbacks();
        List<String> comments = new ArrayList<>();
        if(!(feeds.isEmpty())) {
            for(int i = 0; i < feeds.size(); i++){
                comments.add(feeds.get(i).getRater().getName() + ": " + feeds.get(i).getFeedback());
            }
        }
        return comments;
    }
    
}
