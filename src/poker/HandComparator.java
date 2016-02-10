/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.Comparator;

/**
 *
 * @author john
 */
public class HandComparator implements Comparator<Hand> {

    @Override
    public int compare(Hand h1, Hand h2) {
        if (h1 == null) {
            return h2 == null ? 0 : 1;
        }

        if (h2 == null) {
            return -1;
        }

        return h2.compareTo(h1);
    }

}
