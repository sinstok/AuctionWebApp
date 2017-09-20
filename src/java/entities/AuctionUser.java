/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Joakim
 */
@Entity
public class AuctionUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String address;
    
    @OneToMany(cascade=PERSIST)
    private List<Feedback> feedbacks;
    
    @OneToMany(cascade=PERSIST)
    private List<ProductListing> listings;
    
     @OneToMany(cascade=PERSIST)
    private List<ProductListing> bids;
     
     public void addFeedback(Feedback feedback){
        if(feedbacks == null){
            feedbacks = new ArrayList<Feedback>();   
        }
        feedbacks.add(feedback);
    }
    
    public void addListing(ProductListing listing){
        if(listings == null){
            listings = new ArrayList<ProductListing>();
        }
        listings.add(listing);
    }
    
    //GETTERS&SETTERS

    public void setListings(List<ProductListing> listings) {
        this.listings = listings;
    }

    public void setBids(List<ProductListing> bids) {
        this.bids = bids;
    }

    public List<ProductListing> getListings() {
        return listings;
    }

    public List<ProductListing> getBids() {
        return bids;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        AuctionUser other = (AuctionUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Product[ id=" + id + " ]";
    }

}
