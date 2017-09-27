/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.ProductListingFacade;
import entities.Product;
import entities.ProductListing;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Joakim
 */
@Named(value = "productOverView")
@RequestScoped
public class ProductOverView {
    
    @EJB
    ProductListingFacade plFacade;

    public ProductOverView() {
    }
    
    public String test(){
        List<ProductListing> pls = plFacade.findAll();
        int size = pls.size();
        ProductListing pl = new ProductListing();
        if(size > 0) {
           pl  = pls.get(size - 1);         
        }
        
        return "hehe";
    }
    
}
