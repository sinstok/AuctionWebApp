/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.Product;
import entities.ProductListing;
import helpers.Category;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tomas
 */
@Stateless
public class ProductListingFacade extends AbstractFacade<ProductListing> {

    @PersistenceContext(unitName = "AuctionWebAppPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductListingFacade() {
        super(ProductListing.class);
    }
    
    public List<ProductListing> getBiddables(){
         Date now = new Date();
         List<ProductListing> productListings = em.createQuery(
               "SELECT pl "
               + "FROM ProductListing pl "
               + "WHERE pl.closing > :now", ProductListing.class).setParameter("now", now).getResultList();
         return productListings;
         
    }
    
    public List<ProductListing> searchBiddable(String search) 
    {
       Date now = new Date();
       List<ProductListing> productListings = em.createQuery(
               "SELECT pl "
               + "FROM ProductListing pl JOIN pl.product p "
               + "WHERE lower(pl.description) LIKE :search OR "
               + "lower(p.name) LIKE :search OR "
               + "lower(p.features) LIKE :search AND "
               + "pl.closing > :now", ProductListing.class).setParameter("search", search.toLowerCase()).setParameter("now", now).getResultList();

        return productListings;
    }
    
    public List<ProductListing> getBiddableProductListingsByCategory(Category category) 
    {
       List<ProductListing> productListings = em.createQuery(
               "SELECT pl "
               + "FROM ProductListing pl JOIN pl.product p "
               + "WHERE p.category = :category", ProductListing.class).setParameter("category", category).getResultList();

        return productListings;
    }
    
    
}
