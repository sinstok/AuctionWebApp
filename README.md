# Assignment 3 - Auction Web Application

## Synopsis
In this assignment we have created an Auction Web Application.
The aim of this assignment is to get familiar with:
 - SOAP-based web services as supported by JAX-WS
 - RESTful web services as supported by JAX-RS
 - Java Message Service (JMS)
 - Basic Java EE declarative security mechanisms


## Configuration
The application is connected to a database with some dummy data, so you need to build the application and run it.
You must also open the prosject AuctionWebAppClientSOAP which is located inside the AuctionWebApp folder. When you first open this prosject it will have some errors. Using build once should resolve these problems.

### Setting up JMS Destination Resources
To create the JMS Destination Recources you can run asadmin.bat in glassfish4/bin, this will open a command prompt. Start the glassfish server and write the following command:

add-resources "C:\ ... \AuctionWebApp\web\WEB-INF\glassfish-resources.xml"

This should create a queue called jms/myQueue and a Topic called jms/myTopic
Alternatively you can add these destination resources manually in glassfish

### Setting up jdbcRealm
First you need to ensure that the correct database is in the JDBC pool on the glassfish server, in this case it's db17_g09. When you have the right database, you need to create a JDBC resource that has jndi jdbc/db17_g09 and connect it to the db17_g09 connection pool.
When this is done you need to navigate to the server-config and go to security and realms. If you don't have a realm in JDBC then you need to create one and select the classname with JDBCRealm and name it jdbcRealm! When this is done you will get a list of properties that you need to insert data into.   
  
These are the properties you need to enter:  
  
JAAS Context: jdbcRealm  
JNDI: jdbc/db17_g09  
User Table: auctionuser  
User Name Column: email  
Password Column: password  
Group Table: auctionuser  
Group Table User Name Column: email  
Group Name Column: userrole  
Password Encryption Algorithm: none  
Assign Groups: user  
Digest Algorithm: SHA-256 or empty field. It defaults to SHA-256  

