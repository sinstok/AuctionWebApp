/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializers;

import entities.Feedback;
import entities.Product;
import entities.ProductListing;
import helpers.Category;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

/**
 *
 * @author Joakim
 */
public class ProductObject implements Serializable{
    private Long id;
    private String name;
    private String features;
    @OneToMany(cascade=PERSIST)
    private List<Feedback> feedbacks;
    @OneToMany(cascade={PERSIST}, mappedBy="product")
    private List<ProductListingObject> productListings;
    
    @Enumerated(EnumType.STRING)
    private Category category;
    
    public ProductObject(Product p){
        id = p.getId();
        name = p.getName();
        features = p.getFeatures();
        feedbacks = p.getFeedbacks();
        
        productListings = new ArrayList<ProductListingObject>();
        for(ProductListing pl : p.getProductListings()){
            productListings.add(new ProductListingObject(pl));
        }
    }
}
