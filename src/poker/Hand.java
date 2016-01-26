/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

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
                if(cards[j].getRank() > cards[index].getRank()){
                    index = j;
                    max = cards[j].getRank();
                }
            }
            
            Card tmp = cards[i];
            cards[i] = cards[index];
            cards[index] = tmp;
        }
    }

    public boolean isRoyalFlush() {
        return  cards[0].isAce() &&
                cards[1].isKing() &&
                cards[2].isQueen() &&
                cards[3].isJack() &&
                cards[4].isTen() &&
                isFlush();
    }
    
    public boolean isStraightFlush(){
        return isStraight() && isFlush();
    }
    
    public boolean isFourOfAKind(){
        return  (cards[0].equalRank(cards[1]) &&
                cards[0].equalRank(cards[2]) &&
                cards[0].equalRank(cards[3])) 
                ||
                (cards[1].equalRank(cards[2]) &&
                cards[1].equalRank(cards[3]) &&
                cards[1].equalRank(cards[4]));
    }
    
    public boolean isFullHouse(){
        return  (cards[0].equalRank(cards[1]) && 
                cards[1].equalRank(cards[2]) &&
                cards[3].equalRank(cards[4])) 
                ||
                (cards[0].equalRank(cards[1]) && 
                cards[2].equalRank(cards[3]) &&
                cards[3].equalRank(cards[4])); 
    }
    
    public boolean isFlush(){
        return  cards[0].sameSuit(cards[1]) && 
                cards[1].sameSuit(cards[2]) &&
                cards[2].sameSuit(cards[3]) &&
                cards[3].sameSuit(cards[4]);
    }
    
    public boolean isStraight(){
        return  cards[0].isOneRankHigher(cards[1]) &&
                cards[1].isOneRankHigher(cards[2]) &&
                cards[2].isOneRankHigher(cards[3]) &&
                cards[3].isOneRankHigher(cards[4]);
    }
    
    public boolean isThreeOfAKind(){
        return  (cards[0].equalRank(cards[1]) && cards[1].equalRank(cards[2])) ||
                (cards[1].equalRank(cards[2]) && cards[2].equalRank(cards[3])) ||
                (cards[2].equalRank(cards[3]) && cards[3].equalRank(cards[4]));
    }
    
    public boolean isTwoPairs(){
        if(cards[0].equalRank(cards[1]) && cards[2].equalRank(cards[3]))
            return true;
        
        if(cards[0].equalRank(cards[1]) && cards[3].equalRank(cards[4]))
            return true;
        
        return cards[1].equalRank(cards[2]) && cards[3].equalRank(cards[4]);
    }
    
    public boolean isOnePair(){
        return  cards[0].equalRank(cards[1]) ||
                cards[1].equalRank(cards[2]) ||
                cards[2].equalRank(cards[3]) ||
                cards[3].equalRank(cards[4]);
    }
    
    public int computeRank(){
        if(isRoyalFlush())
            return 10;
        
        if(isStraightFlush())
            return 9;
        
        if(isFourOfAKind())
            return 8;
        
        if(isFullHouse())
            return 7;
        
        if(isFlush())
            return 6;
        
        if(isStraight())
            return 5;
        
        if(isThreeOfAKind())
            return 4;
        
        if(isTwoPairs())
            return 3;
        
        if(isOnePair())
            return 2;
        
        if(cards != null)
            return 1;
        
        return 0;
    }
    
    @Override
    public String toString(){
        String res = "{";
        
        for(int i = 0; i < cards.length; ++i){
            res += i + ": " + cards[i] + ", ";
        }
        
        return res += "}";
    }
}
