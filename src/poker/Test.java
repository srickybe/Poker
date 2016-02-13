/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author john
 */
public class Test {

    private void promptPossibleActions(Gamer gamer, ArrayList<Action> actions) {
        String message = "" + gamer.getName();

        for (int i = 0; i < actions.size(); ++i) {
            message += "\nType " + i + " TO " + actions.get(i).toString() + "\n";
        }

        System.out.println(message);
    }

    class ALLInException extends Exception {

        public ALLInException(String message) {
            super(message);
        }

    }

    class Gamer {

        private final String name;
        private int chips;
        private int currentBet = 0;

        Gamer(String name) {
            this.name = name;
            this.chips = 1000;
        }

        public int getChips() {
            return chips;
        }

        public int getCurrentBet() {
            return currentBet;
        }

        void setCurrentBet(int bet) {
            currentBet = bet;
        }

        Decision act(ArrayList<Action> actions, int highestBet, int bigBlind) {
            Action action = choseAction(actions);
            int removedChips = 0;

            switch (action) {
                case RAISE:
                    return new Decision(action, raise(highestBet, bigBlind));

                case CALL:
                    return new Decision(action, call(highestBet));

                case ALL_IN:
                    return new Decision(action, allIn());

                default:
                    return new Decision(action, removedChips);
            }
        }

        int allIn() {
            int chipsToAddToPot = chips;
            substract(chipsToAddToPot);
            setCurrentBet(getCurrentBet() + chipsToAddToPot);

            return chipsToAddToPot;
        }

        int bet(int highestBet, int bigBlind) {
            Integer amount = null;

            while (amount == null
                    || amount < highestBet + bigBlind
                    || amount > chips) {
                try {
                    System.out.println("Bet!");
                    Scanner input = new Scanner(System.in);
                    amount = input.nextInt();
                } catch (Exception e) {
                    amount = null;
                }
            }

            return amount;
        }

        int betBigBlind(int smallBlind) {
            return smallBlind * 2;
        }

        int betSmallBlind() {
            return (int) (50 * Math.random());
        }

        int call(int highestBet) {
            int chipsToAddToPot = highestBet - getCurrentBet();
            substract(chipsToAddToPot);
            setCurrentBet(highestBet);

            return chipsToAddToPot;
        }

        boolean canRaise(int highestBet, int bigBlind) {
            return highestBet + bigBlind < chips;
        }

        Action choseAction(ArrayList<Action> actions) {
            Integer choice = null;

            while (choice == null || choice < 0 || choice >= actions.size()) {
                try {
                    promptPossibleActions(this, actions);
                    Scanner input = new Scanner(System.in);
                    choice = input.nextInt();
                } catch (Exception e) {
                    choice = null;
                }
            }

            return actions.get(choice);
        }

        int raise(int highestBet, int bigBlind) {
            int bet = bet(highestBet, bigBlind);
            int chipsToAddToPot = bet - getCurrentBet();
            substract(chipsToAddToPot);
            setCurrentBet(bet);

            return chipsToAddToPot;
        }

        void fold() {
        }

        void substract(int bet) {
            chips -= bet;
        }

        @Override
        public String toString() {
            return "Gamer{" + "name=" + name + ", chips=" + chips + ", currentBet=" + currentBet + '}';
        }

        private String getName() {
            return name;
        }
    }

    class Pot {

        private int value;
        private ArrayList<Gamer> gamers;

        public Pot(int value, ArrayList<Gamer> gamers) {
            this.value = value;
            this.gamers = gamers;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public ArrayList<Gamer> getGamers() {
            return gamers;
        }

        public void setGamers(ArrayList<Gamer> gamers) {
            this.gamers = gamers;
        }
    }

    ArrayList<Gamer> gamers;
    Gamer whoRaised = null;
    ArrayList<Pot> pots = new ArrayList<>();

    Test() {
        gamers = new ArrayList<>();
        gamers.add(new Gamer("Ricky"));
        gamers.add(new Gamer("Gery"));
        gamers.add(new Gamer("Audry"));
        gamers.add(new Gamer("Flora"));
    }

    void bettingRound() {
        int smallBlind = (int) (100 * Math.random());
        System.out.println("smallblind = " + smallBlind);
        int bigBlind = 2 * smallBlind;
        System.out.println("bigBlind = " + bigBlind);
        whoRaised = null;
        int highestBet = 0;
        boolean end = false;
        int pot = 0;

        for (int i = 0; !end;) {
            System.out.println("Highest bet = " + highestBet);
            Gamer gamer = gamers.get(i);
            ArrayList<Action> actions
                    = possibleActions(gamer, highestBet, bigBlind);

            Decision decision = gamer.act(actions, highestBet, bigBlind);
            System.out.println("decision = " + decision);

            switch (decision.getAction()) {
                case ALL_IN:
                    pot += decision.getBet();

                    if (gamer.getCurrentBet() > highestBet) {
                        highestBet = gamer.getCurrentBet();
                        whoRaised = gamer;
                    }
                    else{
                        //IMPLEMENT POT SHARING
                    }

                    break;

                case RAISE:
                    pot += decision.getBet();
                    highestBet = gamer.getCurrentBet();
                    whoRaised = gamer;
                    break;

                case CALL:
                    pot += decision.getBet();
                    break;

                case CHECK:
                    end = isRaiseConfirmed(gamer) || hasEveryGamerChecked(i);
                    break;

                case FOLD:
                    gamers.remove(gamer);
                    --i;
                    end = oneGamerLeft();
                    break;

                default:
                    break;
            }

            System.out.println("gamer = " + gamer);
            System.out.println("pot = " + pot);
            ++i;

            if (i >= gamers.size()) {
                i = 0;
            }
        }
    }

    private boolean oneGamerLeft() {
        return gamers.size() == 1;
    }

    private boolean hasEveryGamerChecked(int index) {
        return index == gamers.size() - 1;
    }

    private boolean isRaiseConfirmed(Gamer gamer) {
        return gamer == whoRaised;
    }

    private ArrayList<Action> possibleActions(
            Gamer gamer,
            int highestBet,
            int bigBlind) {
        ArrayList<Action> possibleActions = new ArrayList<>();

        possibleActions.add(Action.FOLD);

        if (gamer.getChips() > 0) {
            possibleActions.add(Action.ALL_IN);
        }

        if (gamer.canRaise(highestBet, bigBlind)) {
            possibleActions.add(Action.RAISE);
        }

        if (whoRaised == null || gamer == whoRaised) {
            possibleActions.add(Action.CHECK);
        } else {
            if (highestBet - gamer.getCurrentBet() < gamer.getChips()) {
                possibleActions.add(Action.CALL);
            }
        }

        return possibleActions;
    }

    public static void main(String args[]) {
        Test test = new Test();
        test.bettingRound();
    }
}
