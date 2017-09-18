/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.ProductFacade;
import entities.Product;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Sindre
 */
@Named(value = "ProductView")
@RequestScoped
public class ProductView {

    @EJB
    private ProductFacade productFacade;
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
    
    public String addProduct(){
        productFacade.create(product);
        return null;
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
