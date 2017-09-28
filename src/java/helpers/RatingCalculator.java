/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.List;

/**
 *
 * @author Tomas
 */
public class RatingCalculator {

    public RatingCalculator() {

    }

    public Double calcuatedRating(List<Double> ratings) {
        int size = ratings.size();
        if (size != 0) {
            double sum = 0;
            for (int i = 0; i < size; i++) {
                sum += ratings.get(i);
            }
            sum = sum / size;

            return sum;
        }
            return 0.0;
    }
}
