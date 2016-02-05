/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.Scanner;

/**
 *
 * @author ricky
 */
class Gamer {

    int id;
    int bet;
    boolean active;
    Action latest;

    Gamer(int id) {
        this.id = id;
        this.bet = 0;
        active = true;
        latest = null;
    }

    public Action getLatest() {
        return latest;
    }

    public void setLatest(Action latest) {
        this.latest = latest;
    }

    int getId() {
        return id;
    }

    int act() {
        Integer res = null;

        while (res == null || res < 0 || res > 3) {
            String prompt = "Chose an action:";

            for (int i = 0; i < Choice.values().length; ++i) {
                prompt += "\n" + i + " " + Choice.values()[i];
            }

            System.out.println(prompt);
            Scanner input = new Scanner(System.in);

            try {
                res = input.nextInt();
            } catch (Exception e) {
                res = null;
            }
        }

        return res;
    }

    int raise(int highestBet) {
        Integer res = null;

        while (res == null || res < highestBet) {
            System.out.println("Bet higher than " + highestBet);
            Scanner input = new Scanner(System.in);

            try {
                res = input.nextInt();
            } catch (Exception e) {
                res = null;
            }
        }

        return res;
    }

    void bet(int highestBet) {
        this.bet = highestBet;
    }

    void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Gamer{" + "id=" + id + ", bet=" + bet + ", active=" + active + ", latest=" + latest + '}';
    }

}

enum Choice {

    CALL,
    CHECK,
    FOLD,
    RAISE
}


public class Game {

    public static void main(String[] args) {
        Gamer[] players = new Gamer[4];

        for (int i = 0; i < 4; ++i) {
            players[i] = new Gamer(i);
        }

        int highestBet = 0;
        Gamer whoRaised = null;

        boolean end = false;
        boolean oneCheck = false;
        int i = 0;

        while (!end) {
            if (players[i].isActive()) {
                boolean validAction = false;
                System.out.println("player" + i + " act");

                while (!validAction) {
                    int act = players[i].act();

                    switch (Choice.values()[act]) {
                        case CALL:
                            System.out.println("Players[" + i + "] CALLED!");
                            if (whoRaised != null && players[i] != whoRaised) {
                                players[i].bet(highestBet);
                                validAction = true;
                                players[i].setLatest(Action.CALL);
                            } else {
                                validAction = false;
                            }

                            break;

                        case CHECK:
                            System.out.println("Players[" + i + "] CHECKED!");
                            if (whoRaised != null) {
                                if (players[i] == whoRaised) {
                                    end = true;
                                    validAction = true;
                                    players[i].setLatest(Action.CHECK);
                                } else {
                                    validAction = false;
                                }
                            } else {
                                validAction = true;

                                if (players[i] == players[0]
                                        && players[i].getLatest() == Action.CHECK) {
                                    end = true;
                                }
                                players[i].setLatest(Action.CHECK);
                            }

                            break;

                        case FOLD:
                            System.out.println("Players[" + i + "] FOLDED!");
                            if (players[i] != whoRaised) {
                                players[i].setActive(false);
                                int count = 0;

                                for (int j = 0; j < 4; ++j) {
                                    if (players[j].isActive()) {
                                        ++count;
                                    }
                                }

                                if (count == 1) {
                                    end = true;
                                }

                                validAction = true;
                                players[i].setLatest(Action.FOLD);
                            } else {
                                validAction = false;
                            }
                            break;

                        case RAISE:
                            System.out.println("Players[" + i + "] RAISED!");
                            highestBet = players[i].raise(highestBet);
                            whoRaised = players[i];
                            validAction = true;
                            players[i].setLatest(Action.RAISE);
                            break;

                        default:

                    }
                }
            }

            System.out.println("players[" + i + "] = " + players[i]);
            ++i;

            if (i == 4) {
                i = 0;
            }
        }
    }
}
