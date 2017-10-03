/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Tomas
 */
public class TimeManger {
    
 public TimeManger() {

    }

    /**
     * Finds the remaining time before a productlistings bidding closes
     * @param closing
     * @param now
     * @return String
     */
    public String getTimeRemaining(Date closing, Date now) {
        String time;
        if (closing.after(now)) {
            long diff = closing.getTime() - now.getTime();
            long min = 60000L;
            long hour = 3600000L;
            long day = 86400000L;
            if (diff < min) {
                long diffSecs = diff / (1000);
                time = "There are " + Objects.toString(diffSecs, null) + " seconds left";
            } else if (diff < hour) {
                long diffMins = diff / (60 * 1000);
                time = "There are " + Objects.toString(diffMins, null) + " minutes left";
            } else if (diff < day) {
                long diffHours = diff / (60 * 60 * 1000);
                time = "There are " + Objects.toString(diffHours, null) + " hours left";
            } else {
                long diffDays = diff / (24 * 60 * 60 * 1000);
                time = "There are " + Objects.toString(diffDays, null) + " days left";
            }
        } else {
            time = "Biding is closed";
        }
        return time;
    }
}
