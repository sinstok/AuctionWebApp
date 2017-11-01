/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionwebappclientrest;

/**
 *
 * @author Ragnhild
 */
public class RESTClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ProductListingRESTClient pl = new ProductListingRESTClient();
        ProductRESTClient p = new ProductRESTClient();
        pl.getJSONBiddables();
        //p.getAuctions(653);
    }
    
}
