/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.presentation;

import boundary.BukseFacade;
import boundary.MannFacade;
import boundary.ProductFacade;
import entities.Bukse;
import entities.Mann;
import entities.Product;
import java.util.ArrayList;
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
    @EJB
    private MannFacade mannFacade;
    @EJB
    private BukseFacade bukseFacade;

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
        Mann enmann = new Mann();
        Bukse bu1 = new Bukse();
        Bukse bu2 = new Bukse();
        bu1.setType("jean");
        bu2.setType("tran");
        enmann.setNavn("Eivind");
        List<Bukse> bukser = new ArrayList<Bukse>();
        bukser.add(bu1);
        bukser.add(bu2);
        bukseFacade.create(bu1);
        bukseFacade.create(bu2);
        enmann.setBukser(bukser);
        mannFacade.create(enmann);
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
