/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionwebappclientrest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ragnhild
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        WebTarget base = client.target("https://localhost:8181/AuctionWebApp/webresources/auctionuser/name");
        String name = base.request(MediaType.TEXT_PLAIN)
                .get(String.class);
        System.out.println("Navn: " + name);
        System.out.print("Hei");
    }
    
}
