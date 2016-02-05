/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.Comparator;

/**
 *
 * @author ricky
 */
public class DecreasingSense implements Comparator<Card> {

    @Override
    public int compare(Card c1, Card c2) {
        if (c1 == null) {
            return c2 == null ? 0 : 1;
        }

        if (c2 == null) {
            return -1;
        }

        return c2.ordinal() - c1.ordinal();
    }

}
