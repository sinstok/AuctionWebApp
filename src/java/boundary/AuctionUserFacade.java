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
    

    public AuctionUser getSeller(String fieldName, Long id) {
        List<AuctionUser> users = em.createQuery("SELECT p FROM ProductListing s JOIN AuctionUser p WHERE s.id = :val", AuctionUser.class).setParameter("val", id).getResultList();

        AuctionUser user = null;
        if (users != null) {

            user = users.get(0);
        }
        
        return user;
    }

    public synchronized boolean register(String fieldName, Object value) {
        List<AuctionUser> list = em.createQuery("SELECT t FROM " + AuctionUser.class.getSimpleName() + " t WHERE t." + fieldName + " " + "=" + " :val ORDER BY t.id ASC", AuctionUser.class).setParameter("val", value.toString()).getResultList();
        if (list.size() != 1) {
            return false;
        }
        return true;
    }

    public synchronized long login(String username, String password) {
        AuctionUser user = null;
        user = em.createQuery("SELECT t FROM AuctionUser t WHERE t.email = :val AND t.password = :vall", AuctionUser.class).setParameter("val", username).setParameter("vall", password).getSingleResult();

        if (user == null) {
            return 0;
        }
        return user.getId();
    }
}
