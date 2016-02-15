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
enum Action1 {

    BET,
    CALL,
    CALL_ALL_IN,
    CHECK,
    FOLD,
    RAISE,
    RAISE_ALL_IN,
    RAISE_INFERIOR_TO_LAST_RAISE;

    public boolean isBet() {
        return this.equals(BET);
    }

    public boolean isCall() {
        return this.equals(CALL);
    }

    public boolean isCallAllIn() {
        return this.equals(CALL_ALL_IN);
    }

    public boolean isCheck() {
        return this.equals(CHECK);
    }

    public boolean isFold() {
        return this.equals(FOLD);
    }

    public boolean isRaise() {
        return this.equals(RAISE);
    }

    public boolean isRaiseAllIn() {
        return this.equals(RAISE_ALL_IN);
    }

    public boolean isRaiseInferiorToLastRaise() {
        return this.equals(RAISE_INFERIOR_TO_LAST_RAISE);
    }
}

class Decision1 {

    private Action1 action;
    private int bet;

    public Decision1(Action1 action, int bet) {
        this.action = action;
        this.bet = bet;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public Action1 getAction1() {
        return action;
    }

    public void setAction1(Action1 action) {
        this.action = action;
    }

}

class Gamer {

    private final String name;
    private int stack;
    private int currentBet = 0;
    private Action1 latestAction1 = null;

    Gamer(String name) {
        this.name = name;
        this.stack = (int) (250 + 751 * Math.random());
    }

    public int getStack() {
        return stack;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public Action1 getLatestAction1() {
        return latestAction1;
    }

    void setCurrentBet(int bet) {
        currentBet = bet;
    }

    public void setLatestAction1(Action1 latestAction1) {
        this.latestAction1 = latestAction1;
    }

    Decision1 act(ArrayList<Action1> actions, int highestBet, int bigBlind) {
        Action1 action = choseAction1(actions);
        setLatestAction1(action);
        int removedChips = 0;

        switch (action) {
            case BET:
                return new Decision1(action, bet(bigBlind));

            case RAISE:
                return new Decision1(action, raiseFully(highestBet, bigBlind));

            case RAISE_ALL_IN:
                return new Decision1(action, raiseAllIn(highestBet));

            case RAISE_INFERIOR_TO_LAST_RAISE:
                return new Decision1(
                        action,
                        raiseInferiorToLastRaise(highestBet, bigBlind));

            case CALL:
                return new Decision1(action, call(highestBet));

            case CALL_ALL_IN:
                return new Decision1(action, allIn());

            default:
                return new Decision1(action, removedChips);
        }
    }

    int allIn() {
        int chipsToAddToPot1 = stack;
        substract(chipsToAddToPot1);
        setCurrentBet(getCurrentBet() + chipsToAddToPot1);

        return chipsToAddToPot1;
    }

    int bet(int bigBlind) {
        Integer chipsToAddToPot1 = 0;

        while (chipsToAddToPot1 < bigBlind || chipsToAddToPot1 >= getStack()) {
            System.out.println("Bet any amount between 0 and "
                    + getStack() + " excluded");
            chipsToAddToPot1 = getInterInput();
        }

        substract(chipsToAddToPot1);
        setCurrentBet(chipsToAddToPot1);

        return chipsToAddToPot1;
    }

    int betBigBlind(int smallBlind) {
        return smallBlind * 2;
    }

    int betSmallBlind() {
        return (int) (50 * Math.random());
    }

    int call(int highestBet) {
        int chipsToAddToPot1 = highestBet - getCurrentBet();
        substract(chipsToAddToPot1);
        setCurrentBet(highestBet);

        return chipsToAddToPot1;
    }

    int callAllIn(int highestBet) {
        if (!canCall(highestBet)) {
            int chipsToAddToPot1 = getStack();
            substract(chipsToAddToPot1);
            setCurrentBet(getCurrentBet() + chipsToAddToPot1);

            return chipsToAddToPot1;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    boolean canCall(int highestBet) {
        return getCurrentBet() + getStack() > highestBet;
    }

    boolean canFullyRaise(int highestBet, int lastRaise) {
        return highestBet + lastRaise < stack;
    }

    boolean canRaise(int highestBet, int lastRaise) {
        if (!canFullyRaise(highestBet, lastRaise)) {
            return getCurrentBet() + getStack() > highestBet;
        }

        throw new UnsupportedOperationException();
    }

    Action1 choseAction1(ArrayList<Action1> actions) {
        Integer choice = null;

        while (choice == null || choice < 0 || choice >= actions.size()) {
            try {
                promptPossibleActions(actions);
                Scanner input = new Scanner(System.in);
                choice = input.nextInt();
            } catch (Exception e) {
                choice = null;
            }
        }

        return actions.get(choice);
    }

    int getInterInput() {
        Integer amount = null;

        while (amount == null) {
            try {
                Scanner input = new Scanner(System.in);
                amount = input.nextInt();
            } catch (Exception e) {
                amount = null;
            }
        }

        return amount;
    }

    int raiseAllIn(int highestBet) {
        int bet = getCurrentBet() + getStack();
        int chipsToAddToPot1 = getStack();
        substract(chipsToAddToPot1);
        setCurrentBet(bet);

        return chipsToAddToPot1;
    }

    int raiseFully(int highestBet, int lastRaise) {

        int bet = -1;

        while (bet < highestBet + lastRaise || bet > getStack()) {
            System.out.println("Bet higher than "
                    + (highestBet + lastRaise));
            bet = getInterInput();
        }

        int chipsToAddToPot1 = bet - getCurrentBet();
        substract(chipsToAddToPot1);
        setCurrentBet(bet);

        return chipsToAddToPot1;
    }

    int raiseInferiorToLastRaise(int highestBet, int lastRaise) {
        if (getStack() >= lastRaise) {
            throw new UnsupportedOperationException();
        }

        int bet = getCurrentBet() + getStack();
        int chipsToAddToPot1 = getStack();
        substract(chipsToAddToPot1);
        setCurrentBet(bet);

        return chipsToAddToPot1;
    }

    void fold() {
    }

    void substract(int bet) {
        stack -= bet;
    }

    @Override
    public String toString() {
        return "Gamer{" + "name=" + name + ", chips=" + stack + ", currentBet=" + currentBet + '}';
    }

    private String getName() {
        return name;
    }

    private void promptPossibleActions(ArrayList<Action1> actions) {
        String message = "" + this.getName();
        for (int i = 0; i < actions.size(); ++i) {
            message += "\nType " + i + " TO " + actions.get(i).toString() + "\n";
        }
        System.out.println(message);
    }
}

class Pot1 {

    private int value;
    private ArrayList<Gamer> gamers;

    public Pot1(int value, ArrayList<Gamer> gamers) {
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

public class Test {

    private boolean noBet(ArrayList<Gamer> gamers) {
        for (int i = 0; i < gamers.size(); ++i) {
            Gamer gamer = gamers.get(i);
            Action1 action = gamer.getLatestAction1();

            if (action != null && !action.isCheck() && !action.isFold()) {
                return false;
            }
        }

        return true;
    }

    private boolean betCalledByEveryOne(ArrayList<Gamer> gamers) {
        if (!everyOneActed(gamers)) {
            return false;
        }

        if (raiseCount(gamers) != 1) {
            return false;
        }

        for (Gamer g : gamers) {
            Action1 action = g.getLatestAction1();

            if (!action.isCall() || !action.isCallAllIn() || !action.isFold()) {
                return false;
            }
        }

        return true;
    }

    private int raiseCount(ArrayList<Gamer> gamers1) throws UnsupportedOperationException {
        int result = 0;

        for (Gamer g : gamers1) {
            Action1 action = g.getLatestAction1();

            if (action.isRaise()
                    || action.isRaiseAllIn()
                    || action.isRaiseInferiorToLastRaise()
                    || action.isBet()) {
                ++result;
            }
        }

        return result;
    }

    private boolean everyOneActed(ArrayList<Gamer> gamers1) {
        for (Gamer g : gamers1) {
            Action1 action = g.getLatestAction1();
            if (action == null) {
                return false;
            }
        }

        return true;
    }

    private boolean previousRaiseOrBet(ArrayList<Gamer> gamers) {
        for (Gamer g : gamers) {
            Action1 action = g.getLatestAction1();

            if (action != null) {
                if (action.isBet()
                        || action.isRaise()
                        || action.isRaiseAllIn()
                        || action.isRaiseInferiorToLastRaise()) {
                    return true;
                }
            }
        }

        return false;
    }

    class ALLInException extends Exception {

        public ALLInException(String message) {
            super(message);
        }

    }

    ArrayList<Gamer> gamers;

    Gamer whoRaised = null;
    ArrayList<Pot1> pots = new ArrayList<>();

    Test() {
        gamers = new ArrayList<>();
        gamers.add(new Gamer("Ricky"));
        gamers.add(new Gamer("Gery"));
        gamers.add(new Gamer("Audry"));
        gamers.add(new Gamer("Flora"));
    }

    void setGamers(ArrayList<Gamer> gamers) {
        this.gamers = gamers;
    }

    void bettingRound() {
        int smallBlind = (int) (100 * Math.random());
        System.out.println("smallblind = " + smallBlind);
        int bigBlind = 2 * smallBlind;
        System.out.println("bigBlind = " + bigBlind);
        whoRaised = null;
        int highestBet = 0;
        int lastRaise = bigBlind;
        boolean end = false;
        int pot = 0;

        for (int i = 0; !end;) {
            Gamer gamer = gamers.get(i);
            System.err.println("gamer = " + gamer);
            System.out.println("Highest bet = " + highestBet);
            ArrayList<Action1> actions
                    = possibleActions(gamer, highestBet, lastRaise);

            if (actions.isEmpty()) {
                break;
            }

            Decision1 decision = gamer.act(actions, highestBet, lastRaise);
            System.out.println("decision = " + decision);

            switch (decision.getAction1()) {
                case BET:
                    lastRaise = decision.getBet();
                    pot += lastRaise;
                    highestBet = gamer.getCurrentBet();
                    whoRaised = gamer;
                    break;

                case CALL:
                    pot += decision.getBet();
                    break;

                case CALL_ALL_IN:
                    pot += decision.getBet();
                    throw new UnsupportedOperationException();
                //break;

                case CHECK:
                    end = isRaiseConfirmed(gamer) || hasEveryGamerChecked(i);
                    break;

                case FOLD:
                    gamers.remove(gamer);
                    --i;
                    end = oneGamerLeft();
                    break;

                case RAISE:
                    lastRaise = decision.getBet();
                    pot += lastRaise;
                    highestBet = gamer.getCurrentBet();
                    whoRaised = gamer;
                    break;

                case RAISE_ALL_IN:
                    lastRaise = decision.getBet();
                    pot += lastRaise;
                    highestBet = gamer.getCurrentBet();
                    whoRaised = gamer;
                    throw new UnsupportedOperationException();
                //break;

                case RAISE_INFERIOR_TO_LAST_RAISE:
                    pot += decision.getBet();
                    highestBet = gamer.getCurrentBet();
                    whoRaised = gamer;
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

    private ArrayList<Action1> possibleActions(
            Gamer gamer,
            int highestBet,
            int lastRaise) {
        ArrayList<Action1> possibleActions = new ArrayList<>();

        //bet if no one has bet yet
        //raise if you want to bet higher than a previous raiseFully or bet
        //check if no one has bet yet or you've raised and everyone called
        //call to match a previous bet or raiseFully
        if (noBet(gamers)) {
            possibleActions.add(Action1.CHECK);
            possibleActions.add(Action1.BET);
            possibleActions.add(Action1.RAISE_ALL_IN);
            possibleActions.add(Action1.FOLD);

            return possibleActions;
        }

        if (betCalledByEveryOne(gamers)) {
            possibleActions.add(Action1.CHECK);

            return possibleActions;
        }

        if (previousRaiseOrBet(gamers) && gamer == whoRaised) {
            possibleActions.add(Action1.CHECK);

            if (gamer.canFullyRaise(highestBet, lastRaise)) {
                possibleActions.add(Action1.RAISE);
                possibleActions.add(Action1.RAISE_ALL_IN);
            } else {
                if (gamer.canRaise(highestBet, lastRaise)) {
                    possibleActions.add(Action1.RAISE_INFERIOR_TO_LAST_RAISE);
                }
            }

            return possibleActions;
        }

        if (previousRaiseOrBet(gamers)) {
            if (gamer.canFullyRaise(highestBet, lastRaise)) {
                possibleActions.add(Action1.RAISE);
                possibleActions.add(Action1.RAISE_ALL_IN);
            } else {
                if (gamer.canRaise(highestBet, lastRaise)) {
                    possibleActions.add(Action1.RAISE_INFERIOR_TO_LAST_RAISE);
                }
            }

            if (gamer.canCall(highestBet)) {
                possibleActions.add(Action1.CALL);

            } else {
                possibleActions.add(Action1.CALL_ALL_IN);
            }

            possibleActions.add(Action1.FOLD);

            return possibleActions;
        }

        /**
         * bet, all in, call, check, fold, raise bet : nobody has raised all in:
         * you can raise or you want to call a bet higher than your stack check:
         * nobody has bet yet
         */
        return possibleActions;
    }

    /*private ArrayList<Action1> possibleActions(
     Gamer gamer,
     int highestBet,
     int lastRaise) {
     ArrayList<Action1> possibleActions = new ArrayList<>();

     possibleActions.add(Action1.FOLD);

     if (gamer.canFullyRaise(highestBet, lastRaise)) {
     possibleActions.add(Action1.RAISE);
     possibleActions.add(Action1.ALL_IN);
     }

     if (whoRaised == null || gamer == whoRaised) {
     if (gamer.getLatestAction1() != Action1.ALL_IN) {
     possibleActions.add(Action1.CHECK);
     } else {
     possibleActions.remove(Action1.FOLD);
     }
     } else {
     if (gamer.canCall(highestBet)) {
     possibleActions.add(Action1.CALL);
     } else {
     if (gamer.getStack() > 0) {
     possibleActions.add(Action1.ALL_IN);
     }
     }
     }

     return possibleActions;
     }*/
    public static void main(String args[]) {
        /*Test test = new Test();
         test.bettingRound();*/

        testNoBet();
    }

    public static void testNoBet() {
        ArrayList<Gamer> gamers = new ArrayList<>();
        Gamer Ricky = new Gamer("Ricky");
        Gamer Gery = new Gamer("Gery");
        Gamer Audry = new Gamer("Audry");
        Gamer Flora = new Gamer("Flora");

        gamers.add(Ricky);
        gamers.add(Gery);
        gamers.add(Audry);
        gamers.add(Flora);

        Test test = new Test();
        test.setGamers(gamers);

        System.out.println("Test.noBet() == " + test.noBet(gamers));

        test.bettingRound();

    }
}
