/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.Objects;

/**
 *
 * @author john
 */
public class Card {

    private static final String SUITS[] = {"CLUB", "DIAMOND", "HEART", "SPADE"};
    private static final String RANKS[] = {
        "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
        "JACK", "QUEEN", "KING", "ACE"};
    
    private Integer rank;
    private Integer suit;

    public Card() {
    }

    public Card(int rank, int suit) {
        set(rank, suit);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        if (!Objects.equals(this.rank, other.rank)) {
            return false;
        }
        if (!Objects.equals(this.suit, other.suit)) {
            return false;
        }
        return true;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    
    
    public int getRank() {
        return rank;
    }

    public int getSuit() {
        return suit;
    }

    public final void set(int rank, int suit) {
        if (isValidRankAndSuit(rank, suit)) {
            this.rank = rank;
            this.suit = suit;
        }
    }

    public boolean hasEqualRank(Card card){
        return Objects.equals(rank, card.rank);
    }
    
    public boolean hasSameSuit(Card card){
        return Objects.equals(suit, card.suit);
    }
    
    public boolean isOneRankHigher(Card card){
        return rank == card.rank + 1;
    }
    
    public boolean isValidSuit(int suit) {
        return suit >= 0 && suit < 4;
    }

    public boolean isValidRank(int rank) {
        return rank >= 0 && rank < 13;
    }

    public boolean isValidRankAndSuit(int rank, int suit) {
        return isValidRank(rank) && isValidSuit(suit);
    }

    public boolean isHigher(Card card) {
        return rank > card.rank;
    }
    
    @Override
    public String toString(){
        return "" + RANKS[rank] + " " + SUITS[suit];
    }
    
    public static void main(String args[]){
        Card c1 = new Card(7, 0);
        Card c2 = new Card(7, 0);
        
        System.out.println("c1.equals(c2)? " + c1.equals(c2));
    }
}
