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
public enum Suit {
    CLUBS(1),
    DIAMONDS(2),
    HEARTS(3),
    SPADES(4);
    
    int value;
    
    Suit(int value){
        this.value = value;
    }
}
