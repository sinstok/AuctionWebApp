/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializers;

import entities.AuctionUser;
import entities.Bid;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Joakim
 */
public class BidObject {
    private Long id;
    private double amount;
    private Date bidDate;
    private long userId;
    
    public BidObject(){
        
    }
    
    public BidObject(double amount, Date bidDate, long bidder){
        this.amount = amount;
        this.bidDate = bidDate;
        userId = bidder;
    }
    
    public BidObject(Bid bid){
        id = bid.getId();
        amount = bid.getAmount();
        bidDate = bid.getBidDate();
        userId = bid.getUser().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getBidDate() {
        return bidDate;
    }

    public void setBidDate(Date bidDate) {
        this.bidDate = bidDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long user) {
        this.userId = user;
    }
    
    
}
