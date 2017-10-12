/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionwebappclient;

import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import services.AuctionService_Service;
import services.BidObject;
import services.ProductListingObject;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

/**
 *
 * @author Joakim
 */
public class Main {
    
    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(lookup = "jms/dest")
    private static Queue queue;
    
    static final Logger logger = Logger.getLogger("Main");
    
    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/AuctionWebApp/AuctionService.wsdl")
    private static AuctionService_Service service;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ProductListingObject sax = getBiddables().get(0);
        
        String text;
        try (JMSContext context = connectionFactory.createContext();) {

            text = "---- START EMAIL to customer Sean Bean ----\n"
                    + "Dear Sean Bean,\n"
                    + "Congratulations! You have won in bidding for product Saxophone.\n"
                    + "You can access the product using the following link:\n"
                    + "URL=<LINK>\n"
                    + "---- END EMAIL to customer Sean Bean ----";
            context.createProducer().send(queue, text);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred: {0}", e.toString());
        }
        
        System.out.println(makeBid(667.0, sax.getId()));
    
    }

    private static java.util.List<services.ProductListingObject> getBiddables() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        services.AuctionService port = service.getAuctionServicePort();
        return port.getBiddables();
    }
    
    private static String makeBid(double amount, long productListingId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        services.AuctionService port = service.getAuctionServicePort();
        BidObject bid = new BidObject();
        bid.setAmount(amount);
        bid.setUserId(101);
        bid.setBidDate(null);
      
        return port.makeBid(bid,productListingId);
    }
}
