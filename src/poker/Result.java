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
public class Result {
    private final int rank;
    private final int value;

    public Result(int rank, int value) {
        this.rank = rank;
        this.value = value;
    }

    public int getRank() {
        return rank;
    }

    public int getValue() {
        return value;
    }

}
