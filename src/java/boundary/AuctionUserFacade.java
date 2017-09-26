/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.AuctionUser;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Joakim
 */
@Stateless
public class AuctionUserFacade extends AbstractFacade<AuctionUser> {

    @PersistenceContext(unitName = "AuctionWebAppPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuctionUserFacade() {
        super(AuctionUser.class);
    }
    
    public AuctionUser getSeller(String fieldName, Long id){
        /*List<AuctionUser> list = em.createQuery("SELECT t FROM " + "AuctionUser_ProductListing"
                    + " t WHERE t." + fieldName + " " + "=" + " :val 
                    ORDER BY t.id ASC", long.class).setParameter("val", id).getResultList();
        long id = 0;
        if(list.size() == 1){
            id = list.get(0);
        }*/
        
        /*List<AuctionUser> list = em.createQuery("SELECT t FROM " + AuctionUser.class.getSimpleName() + 
                "as t join t." + "listings" + " as ab WHERE ab.AuctionUser_id = :val ORDER BY t.id ASC",
                AuctionUser.class).setParameter("val", id).getResultList();*/
        
        List<Long> userId = em.createQuery("SELECT auctionuser_id FROM auctionuser_productlisting WHERE listings_id = :val", Long.class).setParameter("val", id).getResultList();
        
        AuctionUser user = null;
        if(userId.size() == 1){
            List<AuctionUser> users = em.createQuery("SELECT a FROM AuctionUser a WHERE a.id = :val", AuctionUser.class).setParameter("val", userId.get(0)).getResultList();
            user = users.get(0);
        }
        //long test = 0;
        return user;
    }
}
