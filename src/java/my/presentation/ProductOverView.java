/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.ProductListingFacade;
import entities.ProductListing;
import helpers.Category;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Ragnhild
 */
@Named(value = "productOverView")
@RequestScoped
@RolesAllowed("user")
public class ProductOverView {
    
    @EJB
    ProductListingFacade plFacade;
    
    private Category category;
    private String search;

    public ProductOverView() {
    }
    
    /**
     * @return List of ProductListing based on search or category or all ProductListings
     */
    @PermitAll
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
    @RolesAllowed("user")
    public String toProductCreation() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if(!ec.isUserInRole("user")){
            //ec.getRequestMap().put("FromPage", "flow-productCreation");
            ec.getSessionMap().put("FromPage", "flow-productCreation");
            ec.redirect("loginPage.xhtml");
            return "/faces/loginPage.xhtml";
        }
        
        return "flow-productCreation";
        
        //ec.redirect("flow-productCreation.xhtml");
    }
    
    /**
     * @return List of categories
     */
    @PermitAll
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
    @PermitAll
    public String toProductDescription(ProductListing productListing) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getRequestMap().put("productListing", productListing);
        //ec.redirect(ec.getRequestContextPath() + "/faces/productdescription.xhtml");
        return "/faces/productdescription";
    }
    
    @PermitAll
    public void toIndex() throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
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
