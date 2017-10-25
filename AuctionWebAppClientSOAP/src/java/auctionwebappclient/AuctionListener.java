/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionwebappclient;

import static auctionwebappclient.Main.getHighestBid;
import java.util.ArrayList;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.xml.ws.WebServiceRef;
import services.AuctionService;
import services.BidObject;
import services.ProductListingObject;

/**
 *
 * @author Joakim
 */
public class AuctionListener implements MessageListener{
    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/AuctionWebApp/AuctionService.wsdl")
    private static AuctionService service;
    
    ArrayList<ProductListingObject> items;
    
    public AuctionListener(ArrayList<ProductListingObject> items){
        this.items = items;
    }
    
    @Override
    public void onMessage(Message message) {
       items = (ArrayList) Main.getBiddables();
       System.out.println();
       System.out.println("Product listings have been updated");
       Main.printItems(items);
    }
    

    
}
