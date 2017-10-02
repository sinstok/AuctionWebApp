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
import helpers.Category;
import helpers.LoginBean;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Joakim
 */
@Named(value = "createProductListingView")
@FlowScoped(value = "flow-productCreation")
public class CreateProductListingView implements Serializable {

    @EJB
    ProductListingFacade productListingFacade;
    @EJB
    ProductFacade productFacade;
    @EJB
    AuctionUserFacade auctionUserFacade;

    @Inject
    LoginBean login;

    private Product product;

    @NotNull
    private Part file;

    private ProductListing productListing;

    @NotNull
    private int category;

    public CreateProductListingView() {
        productListing = new ProductListing();
        product = new Product();
        category = 0;
    }

    public String postProductListing() throws IOException {

        if (!login.isLoggedIn()) {
            FacesMessage msg = new FacesMessage("       You must be logged in in order to add a product", "ERROR MSG");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "returnFromproductCreation";
        }

        if (product.getId() == null) {
            Category[] categories = Category.values();
            if (category < 0 || categories.length - 1 < category) {
                return "returnFromproductCreation";
            }
            product.setCategory(categories[category]);

            productFacade.create(product);
        } else {
            productFacade.edit(product);
        }

        productListing.setProduct(product);

        InputStream is = file.getInputStream();
        byte[] targetArray = new byte[is.available()];
        is.read(targetArray);
        productListing.setImage(targetArray);
        productListing.setPublished(null);

        productListingFacade.create(productListing);
        product.addListing(productListing);

        AuctionUser au = auctionUserFacade.find(login.getUserId());
        au.addListing(productListing);
        auctionUserFacade.edit(au);

        //productListingFacade.create(productListing);
        return "returnFromproductCreation";
    }

    public String selectProduct(long id) {
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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
