/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.objects;

import entities.Bid;
import entities.ProductListing;

/**
 *
 * @author Ragnhild
 */
public class AddBidObject {
    Bid bid;
    ProductListing productListing;

    public AddBidObject(Bid bid, ProductListing productListing) {
        this.bid = bid;
        this.productListing = productListing;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public ProductListing getProductListing() {
        return productListing;
    }

    public void setProductListing(ProductListing productListing) {
        this.productListing = productListing;
    }
    
    
    
}
