/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.ProductListingFacade;
import entities.ProductListing;
import helpers.Category;
import helpers.LoginBean;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Ragnhild
 */
@Named(value = "productOverView")
@RequestScoped
public class ProductOverView {
    
    @EJB
    ProductListingFacade plFacade;
    @Inject 
    LoginBean login;
    
    private Category category;
    private String search;

    public ProductOverView() {
    }
    
    /**
     * @return List of ProductListing based on search or category or all ProductListings
     */
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
    
    /**
     * 
     * @return Path to flow-productCreation
     */
    public String toProductCreation(){
        /*if(!login.isLoggedIn()){
            return "loginPage";
        }*/
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if(!ec.isUserInRole("user")){
            return "loginPage";
        }

        
        return "flow-productCreation";
    }
    
    /**
     * @return List of categories
     */
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        for(Category c : Category.values()) {
            categories.add(c.toString());
        }
        return categories;
    }
    
    /**
     * 
     * @param productListing
     * @return Path to productdescription
     */
    public String toProductDescription(ProductListing productListing) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getRequestMap().put("productListing", productListing);
        return "/faces/productdescription";
    }
    
    /**
     * Get category
     * @return category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Set category
     * @param category 
     */
    public void setCategory(Category category) {
        this.category = category;
    }
    
    /**
     * Set category
     * @param category
     * @return Path to index
     */
    public String Category(Category category) {
        this.category = category;
        return "index";
    }
    
    /**
     * Get search
     * @return search
     */
    public String getSearch() {
        return search;
    }
    
    /**
     * Set search
     * @param search 
     */
    public void setSearch(String search) {
        this.search = search;
    }
    
}
