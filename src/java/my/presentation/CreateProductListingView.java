/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.AuctionUserFacade;
import boundary.ProductFacade;
import boundary.ProductListingFacade;
import entities.AuctionUser;
import entities.Product;
import entities.ProductListing;
import helpers.LoginBean;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.servlet.http.Part;

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
    @EJB
    AuctionUserFacade auctionUserFacade;

    private Product product;

    private Part file;
  
    private ProductListing productListing;
    
    
    public CreateProductListingView() {
        productListing = new ProductListing();
        product = new Product();
    }
    
     public String postProductListing() throws IOException{
         
        if(product.getId() == null){
            productFacade.create(product);
        }
        
        InputStream is = file.getInputStream();
        byte[] targetArray = new byte[is.available()];
        is.read(targetArray);
        productListing.setImage(targetArray);
        
        //AuctionUser au = auctionUserFacade.find(userId);
        //au.addListing(productListing);
        //auctionUserFacade.edit(au);
        
        product.addListing(productListing);
        productFacade.edit(product);
        //productListingFacade.create(productListing);
        return "returnFromproductCreation";
    }
     
    public String selectProduct(long id){
        product = productFacade.find(id);
        return "createProductListing";
    }
    
    

    public List<Product> getAllProducts() {
        return productFacade.findAll();
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
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
