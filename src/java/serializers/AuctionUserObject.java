/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializers;

import entities.AuctionUser;
import entities.Feedback;
import entities.ProductListing;
import java.util.List;
import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

/**
 *
 * @author Joakim
 */
public class AuctionUserObject {
     private Long id;

    private String name;
    private String phone;
    private String email;
    private String address;
 /*
    private List<Feedback> feedbacks;
    private List<ProductListing> listings;
    private List<ProductListing> bids;'
*/
    
    public AuctionUserObject(AuctionUser au){
        id = au.getId();
        name = au.getName();
        phone = au.getPhone();
        email = au.getEmail();
        address = au.getAddress();
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
}
