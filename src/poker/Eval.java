/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package poker;

/**
 *
 * @author 1907riseyasenga
 */
public class Eval {
    private final int rank;
    private final int value;

    public Eval(int rank, int value) {
        this.rank = rank;
        this.value = value;
    }

    public int getRank() {
        return rank;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Eval{" + "rank=" + rank + ", value=" + value + '}';
    }
    
}
