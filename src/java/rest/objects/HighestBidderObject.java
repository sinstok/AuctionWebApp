/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.objects;

import entities.AuctionUser;
import entities.ProductListing;

/**
 *
 * @author Ragnhild
 */
public class HighestBidderObject {
    ProductListing productListing;
    AuctionUser auctionUser;

    public HighestBidderObject(ProductListing productListing, AuctionUser auctionUser) {
        this.productListing = productListing;
        this.auctionUser = auctionUser;
    }

    public ProductListing getProductListing() {
        return productListing;
    }

    public void setProductListing(ProductListing productListing) {
        this.productListing = productListing;
    }

    public AuctionUser getAuctionUser() {
        return auctionUser;
    }

    public void setAuctionUser(AuctionUser auctionUser) {
        this.auctionUser = auctionUser;
    }
    
    
}
