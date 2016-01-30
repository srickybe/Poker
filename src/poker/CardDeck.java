/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.Stack;

/**
 *
 * @author ricky
 */

public class CardDeck {
    private final Stack<Card> cards;
    
    /**
     * Create a 52-card deck. The cards are ordered from the ranks 2 to ace, 
     * and the suits in alphabetic order. The card at the top is Ace Spade and
     * at the bottom, Two Club. 
     */
    
    public CardDeck() {
        cards = new Stack<>();
        
        for (int i = 0; i < 13; ++i) {
            for (int j = 0; j < 4; ++j) {
                cards.add(new Card(i, j));
            }
        }
    }
    
    public Card pop() {
        return cards.pop();
    }
    
    /**
     * Shuffles the stack of cards
     */
    public void shuffle() {
        int n = cards.size();
        
        for (int i = 0; i < n; ++i) {
            int r = (int) (n * Math.random());
            Card tmp = cards.get(i);
            cards.set(i, cards.get(r));
            cards.set(r, tmp);
        }
    }

    @Override
    public String toString() {
        String res = "CardDeck{" + "cards=[";
        
        for(int i = 0; i < cards.size(); ++i){
            res += "\n" + i + ":" + cards.get(i);
        }
        
        res += "\n]}";
        
        return res;
    }
}
