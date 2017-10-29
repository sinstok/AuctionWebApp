/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionwebappclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import services.BidObject;
import services.ProductListingObject;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.QueueConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.xml.ws.BindingProvider;
import services.AuctionService;

/**
 *
 * @author Joakim
 */
public class Main {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/AuctionWebApp/AuctionService.wsdl")
    private static AuctionService service;

  

    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/myTopic")
    private static Topic topic;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JMSException {
        // TODO code application logic here        
        ArrayList<ProductListingObject> items = (ArrayList) getBiddables();

        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(topic);
        AuctionListener listener = new AuctionListener(items);
        consumer.setMessageListener(listener);

        System.out.println("fetching product listings...");
        printItems(items);
        while (true) {
            System.out.print("Enter item number of the product you want to bid on:");
            Scanner sc = new Scanner(System.in);
            int id = sc.nextInt();
            System.out.print("Enter your bid:");
            int bid = sc.nextInt();
            System.out.println(makeBid((double) bid, (long) id));
        }

    }

    public static void printItems(ArrayList<ProductListingObject> items) {
        for (ProductListingObject item : items) {
            BidObject highestBid = getHighestBid(item);
            System.out.println();
            System.out.println("item " + item.getId() + ": ");
            System.out.println("Product: " + item.getProductName());
            System.out.println("Description: " + item.getDescription());
            System.out.println("Current bid: " + highestBid.getAmount());
            System.out.println();
        }
    }

    public static BidObject getHighestBid(ProductListingObject pl) {
        List<BidObject> bids = pl.getBids();
        BidObject highestBid = new BidObject();
        highestBid.setAmount(pl.getBasePrice());

        if (!(bids.isEmpty())) {
            for (int i = 0; i < bids.size(); i++) {
                double current = bids.get(i).getAmount();
                if (current > highestBid.getAmount()) {
                    highestBid = bids.get(i);
                }
            }
        }
        return highestBid;
    }



    public static String makeBid(double amount, long productListingId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        services.AuctionServiceSOAP port = service.getAuctionServiceSOAPPort();
        BidObject bid = new BidObject();
        bid.setAmount(amount);
        bid.setUserId(1);
        bid.setBidDate(null);

        return port.makeBid(bid, productListingId);
    }

    public static java.util.List<services.ProductListingObject> getBiddables() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        services.AuctionServiceSOAP port = service.getAuctionServiceSOAPPort();
        Map<String,Object> requestContext = ((BindingProvider)port).getRequestContext();
        requestContext.put(BindingProvider.USERNAME_PROPERTY, "joakim@gmail.com");
        requestContext.put(BindingProvider.PASSWORD_PROPERTY, "123456");
        return port.getBiddables();
    }
}
