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
    
    private Category category;
    private String search;

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
        List<ProductListing> list;
        if(category != null) {
            list = plFacade.getBiddableProductListingsByCategory(category);
        } else if(search != null) {
            list = plFacade.searchBiddable(search);
        } else {
            list = plFacade.getBiddables();
        }
        return list;
    }
    
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
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
    
    public Category getCategory() {
        return category;
    }

    public String setCategory(Category category) {
        this.category = category;
        return "index";
    }
    
    public String getSearch() {
        return search;
    }
    
    public void setSearch(String search) {
        this.search = search;
    }
    
}
