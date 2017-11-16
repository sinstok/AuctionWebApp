/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import boundary.ProductListingFacade;
import entities.Bid;
import entities.ProductListing;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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
import serializers.ProductListingObject;

/**
 *
 * @author Ragnhild
 */
@Stateless
@Path("productlisting")
public class ProductListingFacadeREST extends AbstractFacade<ProductListing> {

    @PersistenceContext(unitName = "AuctionWebAppPU")
    private EntityManager em;
    
    @Inject
    ProductListingFacade plFacade;
    
    public ProductListingFacadeREST() {
        super(ProductListing.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProductListing entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, ProductListing entity) {
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
    public ProductListing find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProductListingObject> getBiddables() {
        
        List<ProductListing> biddables = plFacade.getBiddables();
        List<ProductListingObject> biddableObjects = new ArrayList<ProductListingObject>();
        for (ProductListing pl : biddables) {
            biddableObjects.add(new ProductListingObject(pl));
        }

        return biddableObjects;
        
        //return plFacade.getBiddables();
    }
        
    /*@GET
    @Path("{search}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProductListing> getSearchBiddables(@PathParam("search")String search) {
        return plFacade.searchBiddable(search);
    }*/
    
    @GET
    @Path("bids/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Bid> getSearchBiddables(@PathParam("id")Long id) {
        ProductListing pl = super.find(id);
        List<Bid> bids = plFacade.getBids(pl);
        return bids;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
