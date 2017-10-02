/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.AuctionUser;
import helpers.PasswordHash;
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
    private PasswordHash hash;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuctionUserFacade() {
        super(AuctionUser.class);

    }

    public AuctionUser getSeller(Long id) {
        List<AuctionUser> users = em.createQuery("SELECT a FROM AuctionUser a JOIN a.listings p WHERE p.id = :val ", AuctionUser.class).setParameter("val", id).getResultList();

        AuctionUser user = null;
        if (users != null) {

            user = users.get(0);
        }

        return user;
    }

    public synchronized boolean register(String fieldName, Object value) {
        List<AuctionUser> list = em.createQuery("SELECT t FROM " + AuctionUser.class.getSimpleName() + " t WHERE t." + fieldName + " " + "=" + " :val ORDER BY t.id ASC", AuctionUser.class).setParameter("val", value.toString()).getResultList();
        return list.size() == 1;
    }

    public synchronized AuctionUser login(String username, String password) {
        AuctionUser user = em.createQuery("SELECT t FROM AuctionUser t WHERE t.email = :val", AuctionUser.class).setParameter("val", username).getSingleResult();
        
        if (user == null) {
            return null;
        }
        try {
            String hashed = hash.hashPassword(password + user.getSalt()).toString();
            if (user.getPassword().equals(hashed)) {
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {

        }
        return user;
    }
}
