/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.Stack;

/**
 *
 * @author john
 */
public class Poker {

    private final Stack<Card> cards;

    public Poker() {
        cards = new Stack<>();

        for (int i = 0; i < 13; ++i) {
            for (int j = 0; j < 4; ++j) {
                cards.add(new Card(i, j));
            }
        }
    }

    public void shuffle() {
        for (int i = 0; i < 52; ++i) {
            int r = (int) (52.0 * Math.random());
            Card tmp = cards.get(i);
            cards.set(i, cards.get(r));
            cards.set(r, tmp);
        }
    }

    @Override
    public String toString() {
        String res = "";

        for (int i = 0; i < cards.size(); ++i) {
            res += i + ": " + cards.get(i).toString() + "\n";
        }

        return res;
    }

    public Card getTopCard() {
        return cards.pop();
    }

    public static void main(String args[]) {
        int maxValue = 0;
        int maxLevel = 0;
        
        for (int count = 0; count < 1e5; ++count) {
            Poker poker = new Poker();
            /*System.out.println("Game :");
            System.out.println(poker);*/
            poker.shuffle();
            /*System.out.println("\n\nGame :");
            System.out.println(poker);*/
            Card[] cards = new Card[5];

            for (int i = 0; i < 5; ++i) {
                cards[i] = poker.getTopCard();
            }

            Hand hand = new Hand(cards[0], cards[1], cards[2], cards[3], cards[4]);
            Eval eval = hand.computeRankAndValue();
            
            if (eval.getRank() > maxLevel){
                maxLevel = eval.getRank();
                maxValue = eval.getValue();
                System.out.println("hand = " + hand);
                System.out.println("eval = " + eval + "\n");
            }
            
            else if(eval.getRank() == maxLevel && eval.getValue() > maxValue) {
                maxValue = eval.getValue();
                System.out.println("hand = " + hand);
                System.out.println("eval = " + eval + "\n");
            }
        }
    }
}
