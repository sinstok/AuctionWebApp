/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.objects;

/**
 *
 * @author Ragnhild
 */
public class UserObject {
    String username;
    String password;

    public UserObject(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String password) {
        this.password = password;
    }
}
