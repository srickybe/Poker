/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.ArrayList;

/**
 * Invariant: the cards forming the hand are sorted cards.get(0) is the highest
 * ranked card of the hand
 */
public class Hand {

    static private final DecreasingSense COMPARATOR = new DecreasingSense();
    private final ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public boolean addCard(Card card) {
        if (!cards.contains(card)) {
            cards.add(card);
            sortCards();

            return true;
        }

        return false;
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public int size() {
        return cards.size();
    }

    public void sortCards() {
        cards.sort(COMPARATOR);
    }

    private boolean isStraightFlush() {
        return isStraight() && isFlush();
    }

    private boolean isFourOfAKind() {
        return (cards.get(0).sameRank(cards.get(1))
                && cards.get(0).sameRank(cards.get(2))
                && cards.get(0).sameRank(cards.get(3)))
                || (cards.get(1).sameRank(cards.get(2))
                && cards.get(1).sameRank(cards.get(3))
                && cards.get(1).sameRank(cards.get(4)));
    }

    private boolean isFullHouse() {
        return (cards.get(0).sameRank(cards.get(1))
                && cards.get(1).sameRank(cards.get(2))
                && cards.get(3).sameRank(cards.get(4)))
                || (cards.get(0).sameRank(cards.get(1))
                && cards.get(2).sameRank(cards.get(3))
                && cards.get(3).sameRank(cards.get(4)));
    }

    private boolean isFlush() {
        return cards.get(0).sameSuit(cards.get(1))
                && cards.get(1).sameSuit(cards.get(2))
                && cards.get(2).sameSuit(cards.get(3))
                && cards.get(3).sameSuit(cards.get(4));
    }

    private boolean isStraight() {
        return (cards.get(0).oneRankHigher(cards.get(1))
                && cards.get(1).oneRankHigher(cards.get(2))
                && cards.get(2).oneRankHigher(cards.get(3))
                && cards.get(3).oneRankHigher(cards.get(4)))
                || (cards.get(0).getRank().equals(Rank.ACE)
                && cards.get(1).getRank().equals(Rank.FIVE)
                && cards.get(2).getRank().equals(Rank.FOUR)
                && cards.get(3).getRank().equals(Rank.THREE)
                && cards.get(4).getRank().equals(Rank.TWO));
    }

    private boolean isThreeOfAKind() {
        return (cards.get(0).sameRank(cards.get(1))
                && cards.get(1).sameRank(cards.get(2)))
                || (cards.get(1).sameRank(cards.get(2))
                && cards.get(2).sameRank(cards.get(3)))
                || (cards.get(2).sameRank(cards.get(3))
                && cards.get(3).sameRank(cards.get(4)));
    }

    private boolean isTwoPairs() {
        if (cards.get(0).sameRank(cards.get(1))
                && cards.get(2).sameRank(cards.get(3))) {
            return true;
        }

        if (cards.get(0).sameRank(cards.get(1))
                && cards.get(3).sameRank(cards.get(4))) {
            return true;
        }

        return cards.get(1).sameRank(cards.get(2))
                && cards.get(3).sameRank(cards.get(4));
    }

    private boolean isOnePair() {
        return cards.get(0).sameRank(cards.get(1))
                || cards.get(1).sameRank(cards.get(2))
                || cards.get(2).sameRank(cards.get(3))
                || cards.get(3).sameRank(cards.get(4));
    }

    public Evaluation computeTypeAndValue() {
        if (cards.size() != 5) {
            return null;
        }

        if (isStraightFlush()) {
            if (cards.get(0).getRank().equals(Rank.ACE)) {
                return new Evaluation(
                        HandType.ROYAL_FLUSH,
                        computeStraightFlushValue());
            }

            return new Evaluation(
                    HandType.STRAIGHT_FLUSH,
                    computeStraightFlushValue());
        }

        if (isFourOfAKind()) {
            return new Evaluation(
                    HandType.FOUR_OF_A_KIND,
                    computeFourOfAKindValue());
        }

        if (isFullHouse()) {
            return new Evaluation(HandType.FULL_HOUSE, computeFullHouseValue());
        }

        if (isFlush()) {
            return new Evaluation(HandType.FLUSH, computeFlushValue());
        }

        if (isStraight()) {
            return new Evaluation(HandType.STRAIGHT, computeStraightValue());
        }

        if (isThreeOfAKind()) {
            return new Evaluation(
                    HandType.THREE_OF_A_KIND,
                    computeThreeOfAKindValue());
        }

        if (isTwoPairs()) {
            return new Evaluation(HandType.TWO_PAIRS, computeTwoPairsValue());
        }

        if (isOnePair()) {
            return new Evaluation(HandType.ONE_PAIR, computeOnePairValue());
        }

        if (cards != null) {
            return new Evaluation(HandType.HIGH_CARD, computeHighCardValue());
        }

        return null;
    }

    private int computeStraightFlushValue() {
        return cards.get(0).getRank().getCardinal();
    }

    private int computeFourOfAKindValue() {
        if (cards.get(0).sameRank(cards.get(1))
                && cards.get(0).sameRank(cards.get(2))
                && cards.get(0).sameRank(cards.get(3))) {
            return cards.get(0).getRank().getCardinal() * 16
                    + cards.get(4).getRank().getCardinal();
        }

        if (cards.get(1).sameRank(cards.get(2))
                && cards.get(1).sameRank(cards.get(3))
                && cards.get(1).sameRank(cards.get(4))) {
            return cards.get(1).getRank().getCardinal() * 16
                    + cards.get(0).getRank().getCardinal();
        }

        return -1;
    }

    private int computeFullHouseValue() {
        if (cards.get(0).sameRank(cards.get(1))
                && cards.get(1).sameRank(cards.get(2))
                && cards.get(3).sameRank(cards.get(4))) {
            return cards.get(0).getRank().getCardinal() * 16
                    + cards.get(3).getRank().getCardinal();
        }

        if (cards.get(0).sameRank(cards.get(1))
                && cards.get(2).sameRank(cards.get(3))
                && cards.get(3).sameRank(cards.get(4))) {
            return cards.get(2).getRank().getCardinal() * 16
                    + cards.get(0).getRank().getCardinal();
        }

        return -1;
    }

    private int computeFlushValue() {
        return cards.get(0).getRank().getCardinal();
    }

    private int computeStraightValue() {
        if (!cards.get(0).getRank().equals(Rank.ACE) || 
                !cards.get(1).getRank().equals(Rank.FIVE)) {
            return cards.get(0).getRank().getCardinal();
        }

        return Rank.FIVE.getCardinal();
    }

    private int computeThreeOfAKindValue() {
        if (cards.get(0).sameRank(cards.get(1))
                && cards.get(1).sameRank(cards.get(2))) {
            return cards.get(0).getRank().getCardinal() * 256 + 
                    cards.get(3).getRank().getCardinal() * 16
                    + cards.get(4).getRank().getCardinal();
        }

        if (cards.get(1).sameRank(cards.get(2))
                && cards.get(2).sameRank(cards.get(3))) {
            return cards.get(1).getRank().getCardinal() * 256 + 
                    cards.get(0).getRank().getCardinal() * 16
                    + cards.get(4).getRank().getCardinal();
        }

        if (cards.get(2).sameRank(cards.get(3))
                && cards.get(3).sameRank(cards.get(4))) {
            return cards.get(2).getRank().getCardinal() * 256 + 
                    cards.get(0).getRank().getCardinal() * 16
                    + cards.get(1).getRank().getCardinal();
        }

        return -1;
    }

    private int computeTwoPairsValue() {
        if (cards.get(0).sameRank(cards.get(1))
                && cards.get(2).sameRank(cards.get(3))) {
            return cards.get(0).getRank().getCardinal() * 256 + 
                    cards.get(2).getRank().getCardinal() * 16
                    + cards.get(4).getRank().getCardinal();
        }

        if (cards.get(0).sameRank(cards.get(1))
                && cards.get(3).sameRank(cards.get(4))) {
            return cards.get(0).getRank().getCardinal() * 256 + 
                    cards.get(3).getRank().getCardinal() * 16
                    + cards.get(2).getRank().getCardinal();
        }

        if (cards.get(1).sameRank(cards.get(2))
                && cards.get(3).sameRank(cards.get(4))) {
            return cards.get(1).getRank().getCardinal() * 256 + 
                    cards.get(3).getRank().getCardinal() * 16
                    + cards.get(0).getRank().getCardinal();
        }

        return -1;
    }

    private int computeOnePairValue() {
        if (cards.get(0).sameRank(cards.get(1))) {
            return cards.get(0).getRank().getCardinal() * 4096 
                    + cards.get(2).getRank().getCardinal() * 256
                    + cards.get(3).getRank().getCardinal() * 16 
                    + cards.get(4).getRank().getCardinal();
        }

        if (cards.get(1).sameRank(cards.get(2))) {
            return cards.get(1).getRank().getCardinal() * 4096 
                    + cards.get(0).getRank().getCardinal() * 256
                    + cards.get(3).getRank().getCardinal() * 16 
                    + cards.get(4).getRank().getCardinal();
        }

        if (cards.get(2).sameRank(cards.get(3))) {
            return cards.get(2).getRank().getCardinal() * 4096 
                    + cards.get(0).getRank().getCardinal() * 256
                    + cards.get(1).getRank().getCardinal() * 16 
                    + cards.get(4).getRank().getCardinal();
        }

        if (cards.get(3).sameRank(cards.get(4))) {
            return cards.get(3).getRank().getCardinal() * 4096 
                    + cards.get(0).getRank().getCardinal() * 256
                    + cards.get(1).getRank().getCardinal() * 16 
                    + cards.get(2).getRank().getCardinal();
        }

        return -1;
    }

    private int computeHighCardValue() {
        return cards.get(0).getRank().getCardinal() * 65536 
                + cards.get(1).getRank().getCardinal() * 4096
                + cards.get(2).getRank().getCardinal() * 256 
                + cards.get(3).getRank().getCardinal() * 16
                + cards.get(4).getRank().getCardinal();
    }

    public static Hand bestHandFrom7Cards(Hand hnd) {
        if (hnd.cards.size() != 7) {
            return null;
        }

        Hand result = null;
        int maxRank = 0;
        int maxValue = -1;

        for (int i = 0; i < hnd.cards.size(); ++i) {
            Card c1 = hnd.cards.remove(i);

            for (int j = i; j < hnd.size(); ++j) {
                Card c2 = hnd.cards.remove(j);
                Hand tmp = new Hand();
                tmp.addCard(hnd.cards.get(0));
                tmp.addCard(hnd.cards.get(1));
                tmp.addCard(hnd.cards.get(2));
                tmp.addCard(hnd.cards.get(3));
                tmp.addCard(hnd.cards.get(4));
                Evaluation eval = tmp.computeTypeAndValue();

                if (eval.getRank() > maxRank) {
                    maxRank = eval.getRank();
                    maxValue = eval.getValue();
                    result = tmp;
                } else if (eval.getRank() == maxRank
                        && eval.getValue() > maxValue) {
                    maxValue = eval.getValue();
                    result = tmp;
                }

                hnd.cards.add(j, c2);
            }

            hnd.cards.add(i, c1);
        }

        return result;
    }

    public static Hand bestHandFrom6Cards(Hand hnd) {
        if (hnd.cards.size() != 6) {
            return null;
        }

        Hand result = null;
        int maxRank = 0;
        int maxValue = -1;

        for (int i = 0; i < hnd.size(); ++i) {
            Card c2 = hnd.cards.remove(i);
            Hand tmp = new Hand();
            tmp.addCard(hnd.cards.get(0));
            tmp.addCard(hnd.cards.get(1));
            tmp.addCard(hnd.cards.get(2));
            tmp.addCard(hnd.cards.get(3));
            tmp.addCard(hnd.cards.get(4));
            Evaluation eval = tmp.computeTypeAndValue();

            if (eval.getRank() > maxRank) {
                maxRank = eval.getRank();
                maxValue = eval.getValue();
                result = tmp;
            } else if (eval.getRank() == maxRank
                    && eval.getValue() > maxValue) {
                maxValue = eval.getValue();
                result = tmp;
            }

            hnd.cards.add(i, c2);
        }

        return result;
    }

    @Override
    public String toString() {
        return "Hand1{" + "cards=" + cards + '}';
    }

    public static void main(String[] args) {
        for (int j = 0; j < 100; ++j) {
            Hand hand = new Hand();

            for (int i = 0; i < 7; ++i) {
                while (!hand.addCard(Card.random())) {
                }
            }

            System.out.println("hand = " + hand);
            System.out.println("hand.computeTypeAndValue() = "
                    + hand.computeTypeAndValue());
            Hand bestHand = Hand.bestHandFrom7Cards(hand);
            System.out.println("best hand = " + bestHand);
            System.out.println("best hand type and value = "
                    + bestHand.computeTypeAndValue());
            System.out.println("hand = " + hand + "\n\n");
        }
    }
}
