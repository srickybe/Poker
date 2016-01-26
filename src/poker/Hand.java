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
        return  cards[0].isOneRankHigher(cards[1]) &&
                cards[1].isOneRankHigher(cards[2]) &&
                cards[2].isOneRankHigher(cards[3]) &&
                cards[3].isOneRankHigher(cards[4]);
    }
    
    public boolean isThreeOfAKind(){
        return  (cards[0].hasEqualRank(cards[1]) && cards[1].hasEqualRank(cards[2])) ||
                (cards[1].hasEqualRank(cards[2]) && cards[2].hasEqualRank(cards[3])) ||
                (cards[2].hasEqualRank(cards[3]) && cards[3].hasEqualRank(cards[4]));
    }
    
    public boolean isTwoPairs(){
        if(cards[0].hasEqualRank(cards[1]) && cards[2].hasEqualRank(cards[3]))
            return true;
        
        if(cards[0].hasEqualRank(cards[1]) && cards[3].hasEqualRank(cards[4]))
            return true;
        
        return cards[1].hasEqualRank(cards[2]) && cards[3].hasEqualRank(cards[4]);
    }
    
    public boolean isOnePair(){
        return  cards[0].hasEqualRank(cards[1]) ||
                cards[1].hasEqualRank(cards[2]) ||
                cards[2].hasEqualRank(cards[3]) ||
                cards[3].hasEqualRank(cards[4]);
    }
    
    public Result computeRankAndValue(){
        if(isRoyalFlush())
            return new Result(10,0);
        
        if(isStraightFlush())
            return new Result(9,0);
        
        if(isFourOfAKind())
            return new Result(8,0);
        
        if(isFullHouse())
            return new Result(7,0);
        
        if(isFlush())
            return new Result(6,0);
        
        if(isStraight())
            return new Result(5,0);
        
        if(isThreeOfAKind())
            return new Result(4,0);
        
        if(isTwoPairs())
            return new Result(3,0);
        
        if(isOnePair())
            return new Result(2,0);
        
        if(cards != null)
            return new Result(1,0);
        
        return new Result(0,0);
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
