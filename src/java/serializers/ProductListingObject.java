/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializers;

import entities.Bid;
import entities.Product;
import entities.ProductListing;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Joakim
 */
public class ProductListingObject implements Serializable {
    private Long id;
    private double basePrice;
    private String image;
    private String description;
    private Date closing = new Date();
    private Date published = new Date();
    private List<BidObject> bids;
    private long productId;
    private String productName;
    
    public ProductListingObject() {
        
    }
    
    public ProductListingObject(ProductListing pl){
        id = pl.getId();
        basePrice = pl.getBasePrice();
        image = "http://localhost:8080/AuctionWebApp/image/productListing/" + id;
        description = pl.getDescription();
        closing = pl.getClosing();
        published = pl.getPublished();
        
        bids = new ArrayList<BidObject>();
        for(Bid bid : pl.getBids()){
            bids.add(new BidObject(bid));
        }
 
        productId = pl.getId();
        productName = pl.getProduct().getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getClosing() {
        return closing;
    }

    public void setClosing(Date closing) {
        this.closing = closing;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public List<BidObject> getBids() {
        return bids;
    }

    public void setBids(List<BidObject> bids) {
        this.bids = bids;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    
    

 
}
