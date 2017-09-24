/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.ProductFacade;
import boundary.ProductListingFacade;
import entities.Product;
import entities.ProductListing;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.flow.FlowScoped;

/**
 *
 * @author Joakim
 */
@Named(value = "createProductListingView")
@FlowScoped(value="flow-productCreation")
public class CreateProductListingView implements Serializable{
    
    @EJB
    ProductListingFacade productListingFacade;
    @EJB
    ProductFacade productFacade;

    private Product product;

  
    private ProductListing productListing;
    
    
    public CreateProductListingView() {
        productListing = new ProductListing();
        product = new Product();
    }
    
     public void postProductListing(){
        if(product.getId() == null){
            productFacade.create(product);
        }
        
        product.addListing(productListing);
        productFacade.edit(product);
        //productListingFacade.create(productListing);
    }
     
    public String selectProduct(long id){
        product = productFacade.find(id);
        return "createProductListing";
    }
    
    

    public List<Product> getAllProducts() {
        return productFacade.findAll();
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
     public Product getProduct() {
        return product;
    }
     
    public ProductListing getProductListing() {
        return productListing;
    }

    public void setProductListing(ProductListing productListing) {
        this.productListing = productListing;
    }
    
}
