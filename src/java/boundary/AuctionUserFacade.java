/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entities.AuctionUser;
import entities.Bid;
import entities.ProductListing;
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

    /**
     * Finds the seller of a specific Product listing
     * @param id
     * @return AuctionUser
     */
    public AuctionUser getSeller(Long id) {
        List<AuctionUser> users = em.createQuery("SELECT a FROM AuctionUser a JOIN a.listings p WHERE p.id = :val ", AuctionUser.class).setParameter("val", id).getResultList();

        AuctionUser user = null;
        if (users != null) {

            user = users.get(0);
        }

        return user;
    }

    /**
     * If a email allready exist in the database, a user should not be able to register with that email.
     * @param fieldName
     * @param value
     * @return a boolean variable.
     */
    public synchronized boolean register(String fieldName, Object value) {
        List<AuctionUser> list = em.createQuery("SELECT t FROM " + AuctionUser.class.getSimpleName() + " t WHERE t." + fieldName + " " + "=" + " :val ORDER BY t.id ASC", AuctionUser.class).setParameter("val", value.toString()).getResultList();
        return list.isEmpty();
    }

    /**
     * This method lets a user log in if the correct data has been entered in the input field of the loginPage.
     * @param username
     * @param password
     * @return the auction user that the input data corresponds to.
     */
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

    /**
     * This method checks if the user is the highest bidder on the products that he have bid on.
     * @param listing
     * @param user
     * @return A string that sates wheter the user is the highest bidder on the product or if he's no longer the highest bidder.
     */
    public String isHighestBidder(ProductListing listing, AuctionUser user) {
        List<Bid> bids = listing.getBids();
        Bid highestBid = null;
        double bidPrice = listing.getBasePrice();
        for (int i = 0; i < bids.size(); i++) {
            if (bids.get(i).getAmount() > bidPrice) {
                bidPrice = bids.get(i).getAmount();
                highestBid = bids.get(i);
            }
        }
        if (highestBid.getUser().getId().equals(user.getId())) {
            return "You are the Highest bidder!";
        } else {
            return "You are no longer the highest bidder!";
        }
    }
}
