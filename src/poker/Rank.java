/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

/**
 *
 * @author ricky
 */
public enum Rank {

    ACE(15),
    KING(14),
    QUEEN(12),
    JACK(11),
    TEN(10),
    NINE(9),
    EIGHT(8),
    SEVEN(7),
    SIX(6),
    FIVE(5),
    FOUR(4),
    THREE(3),
    TWO(2);

    private final int numeric;

    Rank(int value) {
        this.numeric = value;
    }

    int getNumeric() {
        return numeric;
    }
    
    public static Rank random(){
        return Rank.values()[(int)(Rank.values().length * Math.random())];
    }
}
