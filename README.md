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
You must also open the prosjects AuctionWebAppClientSOAP and AuctionWebAppClientREST which is located inside the AuctionWebApp folder. When you first open this prosject it will have some errors. Using build once should resolve these problems.

### Setting up JMS Destination Resources
To create the JMS Destination Recources you can run asadmin.bat in glassfish4/bin, this will open a command prompt. Start the glassfish server and write the following command:

add-resources "C:\ ... \AuctionWebApp\web\WEB-INF\glassfish-resources.xml"

This should create a queue called jms/myQueue and a Topic called jms/myTopic
Alternatively you can add these destination resources manually in glassfish

### Setting up jdbcRealm
You are required to run the glassfish server while doing this setup.
First you need to ensure that the correct database is in the JDBC pool on the glassfish server, in this case it's post-gre-sql_db17_g09_g09Pool. If you don't have this connection pool, you will have to create a new one with pool name post-gre-sql_db17_g09_g09Pool, then select java.sql.datasource as resources type, and last select postgres as database driver vendor. 
If you have to create the database you need to update some of the properties in the addtional properties button at the top.
The best solution is to delete all the properties that are there from before and creating new ones.  
  
The properties you need are:  
  
URL: jdbc:postgresql://data2.hib.no:5433/db17_g09  
driverClass: org.postgresql.Driver  
Password: r8W6QVB  
portNumber: 5433  
databaseName: db17_g09  
User: g09  
serverName: data2.hib.no  
  
When you have the right database, you need to create a JDBC resource that has jndi jdbc/db17_g09 and choose post-gre-sql_db17_g09_g09Pool as pool name and save it at the top.
When this is done you need to navigate to the server-config and go to security and realms. If you don't have a realm in JDBC then you need to create one and select the classname with "com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm" and name it jdbcRealm. When this is done you will get a list of properties that you need to insert data into. 
  
These are the properties you need to enter  
  
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
After all this is done you must restart the glassfish server in order for it to work.  

### RESTful service

##### XML
URL for getting a products productlistings: https://localhost:8181/AuctionWebApp/webresources/product/ + id

URL for getting biddable productlistings: https://localhost:8181/AuctionWebApp/webresources/productlisting

##### JSON
Run AuctionWebAppClientREST

If you get java.lang.NoClassDefFoundError: Could not initialize class org.eclipse.persistence.jaxb.BeanValidationHelper, 
it is a bug in Glassfish, you therefore have to update org.eclipse.persistence.moxy.jar in C:\glassfish4\glassfish\modules to version 2.6.1

