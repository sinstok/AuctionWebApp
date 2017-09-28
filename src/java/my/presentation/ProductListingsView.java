/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.ProductListingFacade;
import helpers.Category;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Ragnhild
 */
@Named(value = "productListingsView")
@RequestScoped
public class ProductListingsView {

    @EJB
    ProductListingFacade plFacade;
    Category c;
    
    /**
     * Creates a new instance of ProductListingsView
     */
    public ProductListingsView() {
    }
}
