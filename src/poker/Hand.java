/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.ListIterator;

/**
 * Invariant: the cards forming the hand are sorted
 * cards[0] is the highest ranked card of the hand
 */
public class Hand {

    static private final int NUMBER_OF_CARDS = 5;
    private Card[] cards;
    
    public Hand() {
    }

    public Hand(Card c0, Card c1, Card c2, Card c3, Card c4) {
        setCards(c0, c1, c2, c3, c4);
        sortCards();
    }
    
    public final void setCards(Card c0, Card c1, Card c2, Card c3, Card c4) {
        if (cards == null) {
            cards = new Card[NUMBER_OF_CARDS];
        }

        cards[0] = c0;
        cards[1] = c1;
        cards[2] = c2;
        cards[3] = c3;
        cards[4] = c4;
    }

    public final void sortCards() {
        for(int i = 0; i < 5; ++i){
            int max = cards[i].getRank();
            int index = i;
            
            for(int j = i+1; j < 5; ++j){
                if(cards[j].getRank() > max){
                    index = j;
                    max = cards[j].getRank();
                }
            }
            
            if(index != i){
                Card tmp = cards[i];
                cards[i] = cards[index];
                cards[index] = tmp;
            }
        }
    }

    public boolean isStraightFlush(){
        return isStraight() && isFlush();
    }
    
    public boolean isFourOfAKind(){
        return  (cards[0].hasEqualRank(cards[1]) &&
                cards[0].hasEqualRank(cards[2]) &&
                cards[0].hasEqualRank(cards[3])) 
                ||
                (cards[1].hasEqualRank(cards[2]) &&
                cards[1].hasEqualRank(cards[3]) &&
                cards[1].hasEqualRank(cards[4]));
    }
    
    public boolean isFullHouse(){
        return  (cards[0].hasEqualRank(cards[1]) && 
                cards[1].hasEqualRank(cards[2]) &&
                cards[3].hasEqualRank(cards[4])) 
                ||
                (cards[0].hasEqualRank(cards[1]) && 
                cards[2].hasEqualRank(cards[3]) &&
                cards[3].hasEqualRank(cards[4])); 
    }
    
    public boolean isFlush(){
        return  cards[0].hasSameSuit(cards[1]) && 
                cards[1].hasSameSuit(cards[2]) &&
                cards[2].hasSameSuit(cards[3]) &&
                cards[3].hasSameSuit(cards[4]);
    }
    
    public boolean isStraight(){
        return  (cards[0].isOneRankHigher(cards[1]) &&
                cards[1].isOneRankHigher(cards[2]) &&
                cards[2].isOneRankHigher(cards[3]) &&
                cards[3].isOneRankHigher(cards[4])) 
                    ||
                (cards[0].getRank() == 12 &&
                cards[1].getRank() == 3 &&
                cards[2].getRank() == 2 && 
                cards[3].getRank() == 1 &&
                cards[4].getRank() == 0);
    }
    
    public boolean isThreeOfAKind(){
        return  
        (cards[0].hasEqualRank(cards[1]) && cards[1].hasEqualRank(cards[2])) ||
        (cards[1].hasEqualRank(cards[2]) && cards[2].hasEqualRank(cards[3])) ||
        (cards[2].hasEqualRank(cards[3]) && cards[3].hasEqualRank(cards[4]));
    }
    
    public boolean isTwoPairs(){
        if(cards[0].hasEqualRank(cards[1]) && cards[2].hasEqualRank(cards[3]))
            return true;
        
        if(cards[0].hasEqualRank(cards[1]) && cards[3].hasEqualRank(cards[4]))
            return true;
        
        return 
            cards[1].hasEqualRank(cards[2]) && cards[3].hasEqualRank(cards[4]);
    }
    
    public boolean isOnePair(){
        return  cards[0].hasEqualRank(cards[1]) ||
                cards[1].hasEqualRank(cards[2]) ||
                cards[2].hasEqualRank(cards[3]) ||
                cards[3].hasEqualRank(cards[4]);
    }
    
    public Eval computeRankAndValue(){
        if(isStraightFlush())
            return new Eval(9,computeStraightFlushValue());
        
        if(isFourOfAKind())
            return new Eval(8,computeFourOfAKindValue());
        
        if(isFullHouse())
            return new Eval(7,computeFullHouseValue());
        
        if(isFlush())
            return new Eval(6,computeFlushValue());
        
        if(isStraight())
            return new Eval(5,computeStraightValue());
        
        if(isThreeOfAKind())
            return new Eval(4,computeThreeOfAKindValue());
        
        if(isTwoPairs())
            return new Eval(3,computeTwoPairsValue());
        
        if(isOnePair())
            return new Eval(2,computeOnePairValue());
        
        if(cards != null)
            return new Eval(1,computeDefaultValue());
        
        return new Eval(0,-1);
    }
    
    int computeStraightFlushValue(){
        return cards[0].getRank();
    }
    
    int computeFourOfAKindValue(){
        if( cards[0].hasEqualRank(cards[1]) &&
            cards[0].hasEqualRank(cards[2]) &&
            cards[0].hasEqualRank(cards[3])){
            return cards[0].getRank() * 16 + cards[4].getRank();
        } 
        
        if( cards[1].hasEqualRank(cards[2]) &&
            cards[1].hasEqualRank(cards[3]) &&
            cards[1].hasEqualRank(cards[4])){
            return cards[1].getRank() * 16 + cards[0].getRank();
        }
        
        return -1;
    }
    
    int computeFullHouseValue(){
        if( cards[0].hasEqualRank(cards[1]) && 
            cards[1].hasEqualRank(cards[2]) &&
            cards[3].hasEqualRank(cards[4])){
            return cards[0].getRank() * 16 + cards[3].getRank();
        } 
        
        if( cards[0].hasEqualRank(cards[1]) && 
            cards[2].hasEqualRank(cards[3]) &&
            cards[3].hasEqualRank(cards[4])){
            return cards[2].getRank() * 16 + cards[0].getRank();
        }
        
        return -1;
    }
    
    int computeFlushValue(){
        return cards[0].getRank();
    }
    
    int computeStraightValue(){
        if(cards[0].getRank() != 12 || cards[1].getRank() != 3)
            return cards[0].getRank();
        
        return 3;
    }
    
    int computeThreeOfAKindValue(){
        if(cards[0].hasEqualRank(cards[1]) && cards[1].hasEqualRank(cards[2])){
            return  cards[0].getRank() * 256 + cards[3].getRank() * 16 +
                    cards[4].getRank();
        }
        
        if(cards[1].hasEqualRank(cards[2]) && cards[2].hasEqualRank(cards[3])){
            return  cards[1].getRank() * 256 + cards[0].getRank() * 16 +
                    cards[4].getRank();            
        }
        
        if(cards[2].hasEqualRank(cards[3]) && cards[3].hasEqualRank(cards[4])){
            return  cards[2].getRank() * 256 + cards[0].getRank() * 16 +
                    cards[1].getRank(); 
        }
        
        return -1;
    }
    
    int computeTwoPairsValue(){
        if(cards[0].hasEqualRank(cards[1]) && cards[2].hasEqualRank(cards[3]))
            return  cards[0].getRank() * 256 + cards[2].getRank() * 16 +
                    cards[4].getRank();
        
        if(cards[0].hasEqualRank(cards[1]) && cards[3].hasEqualRank(cards[4]))
            return  cards[0].getRank() * 256 + cards[3].getRank() * 16 +
                    cards[2].getRank();
        
        if(cards[1].hasEqualRank(cards[2]) && cards[3].hasEqualRank(cards[4]))
            return  cards[1].getRank() * 256 + cards[3].getRank() * 16 +
                    cards[0].getRank();            
        
        return -1;
    }
    
    int computeOnePairValue(){
        if(cards[0].hasEqualRank(cards[1]))
            return  cards[0].getRank() * 4096 + cards[2].getRank() * 256 +
                    cards[3].getRank() * 16 + cards[4].getRank();
        
        if(cards[1].hasEqualRank(cards[2]))
            return  cards[1].getRank() * 4096 + cards[0].getRank() * 256 +
                    cards[3].getRank() * 16 + cards[4].getRank();
        
        if(cards[2].hasEqualRank(cards[3]))
            return  cards[2].getRank() * 4096 + cards[0].getRank() * 256 +
                    cards[1].getRank() * 16 + cards[4].getRank();
        
        if(cards[3].hasEqualRank(cards[4]))
            return  cards[3].getRank() * 4096 + cards[0].getRank() * 256 +
                    cards[1].getRank() * 16 + cards[2].getRank();
        
        return -1;
    }
    
    int computeDefaultValue(){
        return  cards[0].getRank() * 65536 + cards[1].getRank() * 4096 +
                cards[2].getRank() * 256 + cards[3].getRank() * 16 +
                cards[4].getRank();
    }
    
    @Override
    public String toString(){
        String res = "Hand{cards={";
        
        for(int i = 0; i < cards.length-1; ++i){
            res += (i+1) + ": " + cards[i] + ", ";
        }
        
        return  res += cards.length + ": " + 
                cards[cards.length-1].toString() + "}}";
    }
    
    public static void main(String [] args){
        Hand hand = new Hand(
                new Card(12, (int) (4.0 * Math.random())),
                new Card(3, (int) (4.0 * Math.random())),
                new Card(2, (int) (4.0 * Math.random())),
                new Card(1, (int) (4.0 * Math.random())),
                new Card(0, (int) (4.0 * Math.random())));
        
        System.out.println("hand = " + hand);
        System.out.println("hand.isStraight()? " + hand.isStraight());
        System.out.println("hand.computeRankAndValue() = " 
                + hand.computeRankAndValue());
    }
}
