/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author john
 */
public class Function {

    private LinkedList<Card> hand;

    public Function() {
        hand = new LinkedList<>();
    }

    public boolean addCard(Card a) {
        if (!hand.isEmpty()) {
            ListIterator<Card> iter = hand.listIterator();

            while (iter.hasNext()) {
                Card c = iter.next();

                if (c.equals(a)) {
                    return false;
                }

                if (a.getRank() > c.getRank()) {
                    iter.previous();
                    break;
                }
            }

            iter.add(a);
            return true;
        } else {
            hand.add(a);
            return true;
        }
    }

    public Hand bestHand() {
        if (hand.size() == 7) {
            int maxLevel = 0;
            int maxValue = -1;

            for (int i = 0; i < hand.size(); ++i) {
                Card c1 = hand.remove(i);
                for (int j = i; j < hand.size(); ++j) {
                    Card c2 = hand.remove(j);
                    System.out.println("i = " + i + "  j = " + j);
                    /*System.out.println("hand = " + hand);
                    System.out.println("eval = " + eval);*/
                    Hand tmp = new Hand(
                            hand.get(0), 
                            hand.get(1), 
                            hand.get(2), 
                            hand.get(3),
                            hand.get(4));
                    //Hand tmp = new Hand(this);
                    Eval eval = tmp.computeRankAndValue();

                    if (eval.getRank() > maxLevel) {
                        maxLevel = eval.getRank();
                        maxValue = eval.getValue();
                        System.out.println("hand = " + hand);
                        System.out.println("eval = " + eval + "\n");
                    } else if (eval.getRank() == maxLevel && eval.getValue() > maxValue) {
                        maxValue = eval.getValue();
                        System.out.println("hand = " + hand);
                        System.out.println("eval = " + eval + "\n");
                    }

                    hand.add(j, c2);
                }
                hand.add(i, c1);
            }
        }

        return new Hand();
    }

    public int getHandSize() {
        return hand.size();
    }

    public ListIterator<Card> cardIterator() {
        return hand.listIterator();
    }

    @Override
    public String toString() {
        return "Function{" + "hand=" + hand + '}';
    }

    public static void main(String args[]) {
        Function f = new Function();

        for (; f.getHandSize() < 7;) {
            Card c = new Card(
                    (int) (13.0 * Math.random()),
                    (int) (4.0 * Math.random()));
            if (f.addCard(c)) {
                System.out.println("f = " + f + "\n");
            }
        }

        System.out.println("f = " + f);

        f.bestHand();
    }
}
