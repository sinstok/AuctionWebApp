/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.BidFacade;
import boundary.ProductFacade;
import boundary.ProductListingFacade;
import entities.Bid;
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
     
    @Inject
    private DBean dbi;
    
    private Product product;
    private String value;
    //private String time;
    //private Bid bid;
    private ProductListing pl;
    

    /**
     * Creates a new instance of SomeView
     */
    public ProductDescriptionView() {
        this.pl = new ProductListing();
        pl.setBasePrice(10);
        //pl.setImage(image);
        pl.setDescription("Some text");
        Date d1 = new Date();
        Date d2 = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(d1); 
        c.add(Calendar.DATE, 1);
        d2 = c.getTime();
        pl.setPublished(d1); 
        pl.setClosing(d2);
        //Bid bid = bidFacade.find(109);
        //Bid bid2 = bidFacade.find(108);
        Bid b1 = new Bid();
        b1.setAmount(30);
        Bid b2 = new Bid();
        b2.setAmount(35);
        List<Bid> bids = new ArrayList<Bid>();
        bids.add(b2);
        bids.add(b1);
        //plFacade.find(pl);
        pl.setBids(bids);
    }
    
    public ProductListing getProductListing(){
        return pl;
    }
    
    public Product getProduct(){
        Product prod = new Product();
        prod.setName("Newman");
        prod.setFeatures("no feats");
        return prod;
    }
    
    public String getValue(){
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getThisProduct(){
        Product prod = this.getProduct();
        return prod.getName();
    }
    
    public String getThisProductFeats(){
        Product prod = this.getProduct();
        return prod.getFeatures();
    }
    
    public void addProduct(){
        productFacade.create(product);
    }
    
    public void addBid(){
        Bid newBid = new Bid();
        newBid.setAmount(Double.parseDouble(this.getValue()));
        List<Bid> bids = pl.getBids();
        bids.add(newBid);
        pl.setBids(bids);
        //bidFacade.create(newBid);
    }
    
    public String getTimeLeft(){
        Date closing = pl.getClosing();
        Date now = new Date();
        String time;
        
        if(closing.after(now)){
           long diff = closing.getTime() - now.getTime();
           long diffHours = diff / (60 * 60 * 1000);
           time = "There are " + Objects.toString(diffHours, null) + " hours left";
        }else{
            time = "Biding is closed";
        }
        return time;   
    }
    
    
    public double getHighestBid() {
        List<Bid> bids = pl.getBids();
        double b = 0;
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
    
}
