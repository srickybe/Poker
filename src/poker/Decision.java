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
    
    private final Action action;
    private final Integer chips;

    public Decision(Action action, Integer chips) {
        this.action = action;
        this.chips = chips;
    }

    /**
     * Get the value of chips
     *
     * @return the value of chips
     */
    public Integer getChips() {
        return chips;
    }


    /**
     * Get the value of action
     *
     * @return the value of action
     */
    public Action getAction() {
        return action;
    }

}
