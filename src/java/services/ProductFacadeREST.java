/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import boundary.ProductFacade;
import boundary.ProductListingFacade;
import entities.Product;
import entities.ProductListing;
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
import serializers.ProductObject;

/**
 *
 * @author Ragnhild
 */
@Stateless
@Path("product")
public class ProductFacadeREST extends AbstractFacade<Product> {

    @PersistenceContext(unitName = "AuctionWebAppPU")
    private EntityManager em;

    @Inject
    ProductFacade pFacade;
    
    @Inject
    ProductListingFacade plFacade;
    
    public ProductFacadeREST() {
        super(Product.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Product entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Product entity) {
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
    public Product find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Product> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Product> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("{id}/productlistings")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProductListing> getProductListings(@PathParam("id") Long id) {
        Product p = super.find((id));
        List<ProductListing> list = p.getProductListings();
        return list;
    }
    
    @POST
    @Path("fromlisting/{id}/{fieldname}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ProductObject getProductFromListing(@PathParam("id") Long id, @PathParam("fieldname") String fieldname) {
        ProductObject product = new ProductObject(pFacade.getProductFromListing(fieldname, id));
        return product;
    }
    
    @POST
    @Path("productlistings")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProductListing> getProductListings(Product product) {
        return pFacade.getProductListings(product);
    }
    
    @GET
    @Path("productids/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Long> getProductIds() {
        return pFacade.getIDs();
    }
    
    @GET
    @Path("{productId}/productrating")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getAverageProductRating(@PathParam("productId") Long productId) {
        Product product = super.find(productId);
        return plFacade.getAverageProductRating(product);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}