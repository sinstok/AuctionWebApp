/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.objects;

import entities.AuctionUser;

/**
 *
 * @author Ragnhild
 */
public class RegisterUserObject {
    AuctionUser user;
    String password;

    public RegisterUserObject(AuctionUser user, String password) {
        this.user = user;
        this.password = password;
    }

    public AuctionUser getUser() {
        return user;
    }

    public void setUser(AuctionUser user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
