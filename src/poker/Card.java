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

    public boolean equalRank(Card card){
        return Objects.equals(rank, card.rank);
    }
    
    public boolean sameSuit(Card card){
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

    public boolean isAce() {
        return rank == 12;
    }
    
    public boolean isKing() {
        return rank == 11;
    }

    public boolean isQueen() {
        return rank == 10;
    }

    public boolean isJack() {
        return rank == 9;
    }
    
    public boolean isTen(){
        return rank == 8;
    }
    
    @Override
    public String toString(){
        return "" + RANKS[rank] + " " + SUITS[suit];
    }
}
