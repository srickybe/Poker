/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

/**
 *
 * @author john
 */
public enum Action {
    ALL_IN,
    CALL,
    CHECK,
    FOLD,
    NONE,
    RAISE;

    boolean isAllIn() {
        return this.equals(ALL_IN);
    }

    boolean isCall() {
        return this.equals(CALL);
    }

    boolean isCheck() {
        return this.equals(CHECK);
    }

    boolean isFold() {
        return this.equals(FOLD);
    }

    boolean isNone(){
        return this.equals(NONE);
    }
    
    boolean isRaise() {
        return this.equals(RAISE);
    }

}
