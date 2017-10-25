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
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Joakim
 */
@Entity
@XmlRootElement
public class AuctionUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String address;
    private String password;
    private String salt;
    private String userRole;

    @OneToMany(cascade = PERSIST)
    private List<Feedback> feedbacks;

    @OneToMany(cascade = PERSIST)
    @JoinTable(name="auctionuser_listedproductlisting")
    private List<ProductListing> listings;

    @OneToMany(cascade = PERSIST)
    @JoinTable(name="auctionuser_biddedproductlisting")
    private List<ProductListing> bids;

    public Feedback getFeedbackOfUser(long userId){
        Feedback result = null;
        
        for(Feedback fb : feedbacks){
            if(fb.getRater().getId() == userId){
                return fb;
            }
        }
        return result;
    }
    
    public void addFeedback(Feedback feedback) {
        if (feedbacks == null) {
            feedbacks = new ArrayList<>();
        }
        feedbacks.add(feedback);
    }

    public void addListing(ProductListing listing) {
        if (listings == null) {
            listings = new ArrayList<>();
        }
        listings.add(listing);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //GETTERS&SETTERS
    public void setListings(List<ProductListing> listings) {
        this.listings = listings;
    }

    public void setBids(List<ProductListing> bids) {
        this.bids = bids;
    }

    @XmlTransient
    public List<ProductListing> getListings() {
        return listings;
    }

    @XmlTransient
    public List<ProductListing> getBids() {
        return bids;
    }

    @XmlTransient
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRole() {
        return userRole;
    }

    public void setRole(String role) {
        this.userRole = role;
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
