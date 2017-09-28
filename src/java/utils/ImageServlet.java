/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import boundary.ProductListingFacade;
import entities.ProductListing;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Joakim
 * returns image of product listing by id, 
 * usage example: <h:graphicImage value="/image/productListing/5" />
 */
@WebServlet(urlPatterns = "/image/productListing/*")
public class ImageServlet extends HttpServlet {

    @Inject
    ProductListingFacade productListingFacade;
    
   public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String productListingId = URLDecoder.decode(req.getPathInfo().substring(1), "UTF-8");
    ProductListing pl = productListingFacade.find(Integer.parseInt(productListingId));
   
    byte[] bytes = null;
    if(pl != null){
        bytes = pl.getImage();
    }
    resp.setContentType("image/jpeg");    
    OutputStream out = resp.getOutputStream();
    out.write(bytes);
}

}
