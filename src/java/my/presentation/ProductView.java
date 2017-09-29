/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.FeedbackFacade;
import boundary.ProductFacade;
import boundary.AuctionUserFacade;
import boundary.ProductListingFacade;
import entities.AuctionUser;
import entities.Feedback;
import entities.Product;
import entities.AuctionUser;
import helpers.Category;
import helpers.DBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Sindre
 */
@Named(value = "ProductView")
@RequestScoped
public class ProductView {

    @EJB
    ProductFacade productFacade;
    @EJB
    AuctionUserFacade a;
     
    @Inject
    private DBean dbi;
    
    private Product product;

    /**
     * Creates a new instance of ProductView
     */
    public ProductView() {
        this.product = new Product();
    }
    public Product getProduct(){
        return product;
    }
    
  
    public void addProduct(){

        dbi.test();
        
        
        
         
       
    }
    
    public String getLastProduct(){
        Product prod = new Product();
        prod.setName("noProd");
        prod.setFeatures("no feats");
        List<Product> products = productFacade.findAll();
        int size = products.size();
        if(size > 0) {
            prod = products.get(size - 1);         
        }
        return prod.getName() + " " + prod.getFeatures();
    }
    
    public int getNumberOfProducts(){
        return productFacade.findAll().size();
    }
}