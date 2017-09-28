/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.AuctionUser;
import entities.Bid;
import entities.Feedback;
import entities.Product;
import entities.ProductListing;
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
public class ProductFacade extends AbstractFacade<Product> {

    @PersistenceContext(unitName = "AuctionWebAppPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductFacade() {
        super(Product.class);
    }
    
    public Product getProductFromListing(String fieldName, Long id) {
        List<Product> products = em.createQuery("SELECT p FROM ProductListing s JOIN Product p WHERE s.id = :val", Product.class).setParameter("val", id).getResultList();

        Product prod = null;
        if (products != null) {

            prod = products.get(0);
        }
        
        return prod;
    }
    
}
