# Assignment 3 - Auction Web Application

## Synopsis
In this assignment we have created an Auction Web Application.
The aim of this assignment is to get familiar with:
 - SOAP-based web services as supported by JAX-WS
 – RESTful web services as supported by JAX-RS
 – Java Message Service (JMS)
 – Basic Java EE declarative security mechanisms


## Configuration
The application is connected to a database with some dummy data, so you should only need to build the application and run it.
You must also open the prosject AuctionWebAppClientSOAP which is located inside the AuctionWebApp folder. When you first open this prosject it will have some errors. Using build once should resolve these problems.

To create the JMS Destination Recources you can run asadmin.bat in glassfish4/bin, this will open a command prompt. Start the glassfish server and write the following command:

add-resources "C:\...\AuctionWebApp\web\WEB-INF\glassfish-resources.xml"

This should create a queue called jms/myQueue and a Topic called jms/myTopic
Alternatively you can add these destination resources manually in glassfish

