/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import boundary.AuctionUserFacade;
import entities.AuctionUser;
import entities.ProductListing;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import rest.objects.HighestBidderObject;
import rest.objects.RegisterUserObject;
import rest.objects.UserObject;
import serializers.AuctionUserObject;
import serializers.ProductListingObject;
import javax.ws.rs.core.Response;

/**
 *
 * @author Ragnhild
 */
@Stateless
@Path("auctionuser")
public class AuctionUserFacadeREST extends AbstractFacade<AuctionUser> {

    @PersistenceContext(unitName = "AuctionWebAppPU")
    private EntityManager em;

    @Inject
    AuctionUserFacade auctionUserFacade;

    public AuctionUserFacadeREST() {
        super(AuctionUser.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(AuctionUser entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, AuctionUser entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AuctionUser find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<AuctionUser> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<AuctionUser> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("name")
    @Produces({MediaType.TEXT_PLAIN})
    public String findUser() {
        Long tall = new Long(1);
        AuctionUser user = auctionUserFacade.getSeller(tall);
        return user.getName();
    }

    @GET
    @Path("seller")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AuctionUser getSeller(@PathParam("id") Long id) {
        AuctionUser user = auctionUserFacade.getSeller(id);
        return user;
    }

    @POST
    @Path("register")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public boolean register(UserObject user) {
        return auctionUserFacade.register(user.getUsername(), user.getPassword());
    }

    @GET
    @Path("email")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AuctionUser findUserByEmail(@PathParam("email") String email) {
        return auctionUserFacade.findUserByEmail(email);
    }

    @GET
    @Path("{id}/productListings")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProductListingObject> getProductListingsByUser(@PathParam("id") long id) {
        AuctionUser au = auctionUserFacade.find(id);
        if (au == null) {
            return null;
        }
        List<ProductListing> bids = au.getBids();
        List<ProductListingObject> plObjects = new ArrayList<ProductListingObject>();
        for (ProductListing pl : bids) {
            plObjects.add(new ProductListingObject(pl));
        }
        return plObjects;
    }

    @POST
    @Path("registeruser")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AuctionUserObject registerUser(RegisterUserObject registerUser) {
        AuctionUserObject auctionUser = new AuctionUserObject(auctionUserFacade.registerUser(registerUser.getUser(), registerUser.getPassword()));
        return auctionUser;
    }

    @POST
    @Path("login")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public boolean login(UserObject user) {
        AuctionUser auctionUser = auctionUserFacade.login(user.getUsername(), user.getPassword());
        return (auctionUser != null);
    }

    @POST
    @Path("highestbidder")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String isHighestBidder(HighestBidderObject highestBidder) {
        return auctionUserFacade.isHighestBidder(highestBidder.getProductListing(), highestBidder.getAuctionUser());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
