/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.objects;


/**
 *
 * @author Ragnhild
 */
public class AddBidObject {
    double bid;
    Long productListingId;
    Long auctionUserId;

    public AddBidObject(double bid, Long productListingId) {
        this.bid = bid;
        this.productListingId = productListingId;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public Long getProductListingId() {
        return productListingId;
    }

    public void setProductListing(Long productListingId) {
        this.productListingId = productListingId;
    }

    public Long getAuctionUserId() {
        return auctionUserId;
    }

    public void setAuctionUserId(Long auctionUserId) {
        this.auctionUserId = auctionUserId;
    }

}
