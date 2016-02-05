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

public class Card implements Comparable<Card> {

    public static Card random() {
        return new Card(Rank.random(), Suit.random());

    }

    private Rank rank;
    private Suit suit;

    public Card() {
    }

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.rank);
        hash = 79 * hash + Objects.hashCode(this.suit);
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

        return Objects.equals(this.suit, other.suit);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean hasEqualRank(Card card) {
        return Objects.equals(rank, card.rank);
    }

    public boolean hasSameSuit(Card card) {
        return Objects.equals(suit, card.suit);
    }

    public boolean isOneRankHigher(Card card) {
        return rank.getNumeric() == card.rank.getNumeric() + 1;
    }

    public boolean isHigher(Card card) {
        return rank.getNumeric() > card.rank.getNumeric();
    }

    @Override
    public String toString() {
        return "" + rank + " " + suit;
    }

    @Override
    public int compareTo(Card other) {
        if (other == null) {
            return 1;
        }
        
        int res = rank.getNumeric() - other.rank.getNumeric();

        if (res == 0) {
            return suit.getNumeric() - other.suit.getNumeric();
        }

        return res;
    }

    public static void main(String args[]) {
        Card c1 = new Card(Rank.SEVEN, Suit.CLUBS);
        Card c2 = new Card(Rank.SEVEN, Suit.DIAMONDS);
        Card c3 = new Card(Rank.SIX, Suit.HEARTS);

        System.out.println("c1.equals(c2)? " + c1.equals(c2));
        System.out.println(c1.compareTo(c2));
        System.out.println(c1.compareTo(c3));
    }
}

