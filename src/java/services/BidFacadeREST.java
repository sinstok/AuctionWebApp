/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import boundary.AuctionUserFacade;
import boundary.BidFacade;
import boundary.ProductListingFacade;
import entities.AuctionUser;
import entities.Bid;
import entities.ProductListing;
import java.util.Date;
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
import rest.objects.AddBidObject;
import rest.objects.Message;
import serializers.BidObject;

/**
 *
 * @author Ragnhild
 */
@Stateless
@Path("bid")
public class BidFacadeREST extends AbstractFacade<Bid> {

    @PersistenceContext(unitName = "AuctionWebAppPU")
    private EntityManager em;

    @Inject
    BidFacade bidFacade;
    
    @Inject
    ProductListingFacade plFacade;
    
    @Inject
    AuctionUserFacade auFacade;
    
    public BidFacadeREST() {
        super(Bid.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Bid entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Bid entity) {
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
    public Bid find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Bid> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Bid> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @POST
    @Path("/add/productlisting/{productListingId}/amount/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Message addBid(@PathParam("productListingId") Long plId, BidObject object) {
        Date date = new Date();
        Bid bid = new Bid();
        bid.setAmount(object.getAmount());
        bid.setBidDate(date);
        //Long plId2 = new Long(plId);
        ProductListing pl = plFacade.find(plId);
        //Long userId = new Long(userId);//object.getAuctionUserId();
        AuctionUser user = auFacade.find(object.getUserId());
        bid.setUser(user);
        Message message = new Message();
        if(bidFacade.addBid(bid, pl) == null){
            message.setMessage("Success");
        }else{
            message.setMessage(bidFacade.addBid(bid, pl));
        }
        return message;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
