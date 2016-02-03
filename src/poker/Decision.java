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
public class Decision {
    
    private Action action;
    private int bet;

    public Decision(Action action, int bet) {
        this.action = action;
        this.bet = bet;
    }

    public Decision() {
    }
    

    /**
     * Get the value of bet
     *
     * @return the value of bet
     */
    public int getBet() {
        return bet;
    }

    /**
     * Set the value of bet
     *
     * @param bet new value of bet
     */
    public void setBet(int bet) {
        this.bet = bet;
    }


    /**
     * Get the value of action
     *
     * @return the value of action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Set the value of action
     *
     * @param action new value of action
     */
    public void setAction(Action action) {
        this.action = action;
    }

    
}
