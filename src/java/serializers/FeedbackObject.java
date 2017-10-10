/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializers;

import entities.AuctionUser;
import entities.Feedback;

/**
 *
 * @author Joakim
 */
public class FeedbackObject {
    private Long id;
    private double rating;
    private String feedback;
    private AuctionUserObject rater;
    
    public FeedbackObject(Feedback fb){
        id = fb.getId();
        rating = fb.getRating();
        feedback = fb.getFeedback();
        rater = new AuctionUserObject(fb.getRater());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public AuctionUserObject getRater() {
        return rater;
    }

    public void setRater(AuctionUserObject rater) {
        this.rater = rater;
    }
    
    
}
