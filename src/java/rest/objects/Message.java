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
public class Message {
    String message;

    public Message(String message) {
        this.message = message;
    }

    public Message() {
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
