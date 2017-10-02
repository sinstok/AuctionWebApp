/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.ProductListingFacade;
import entities.ProductListing;
import helpers.Category;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

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
    
    public List<ProductListing> getProductListings() {        
        //List<ProductListing> list = plFacade.findAll();
        //List<ProductListing> list = plFacade.getProductListingsByCategory(Category.Instruments);
        List<ProductListing> list = plFacade.getBiddables();
        return list;
    }
    
    public List<String> getCategories() {
        List<String> categories;
        categories = new ArrayList<>();
        for(Category c : Category.values()) {
            categories.add(c.toString());
        }
        return categories;
    }
    
    public String toProductDescription(ProductListing pl) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getRequestMap().put("productListing", pl);
        return "productdescription";
        //return "viewProductListing";
    }
    
}
