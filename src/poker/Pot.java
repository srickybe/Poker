/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author ricky
 */
public class Pot {

    private final int chips;
    private final ArrayList<Player> involved;

    Pot(int chips, Player... players) {
        this.chips = chips;
        this.involved = new ArrayList<>();
        this.involved.addAll(Arrays.asList(players));
    }

    public int getChips() {
        return chips;
    }

    public Player getInvolvedPlayer(int index) {
        return involved.get(index);
    }

    public int getNumberOfInvolvedPlayers() {
        return involved.size();
    }
}
