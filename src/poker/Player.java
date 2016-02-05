/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.Scanner;

/**
 * Invariant: the number of cards in the pocket must be inferior or equal to two
 *
 * @author john
 */
public class Player {

    private final Action ACTIONS[] = {
        Action.ALL_IN,
        Action.CALL,
        Action.CHECK,
        Action.FOLD,
        Action.RAISE
    };

    private final String ACT_PROMPT_MESSAGE
            = "Type\n"
            + "0, TO " + ACTIONS[0].toString() + "\n"
            + "1, TO " + ACTIONS[1].toString() + "\n"
            + "2, TO " + ACTIONS[2].toString() + "\n"
            + "3, TO " + ACTIONS[3].toString() + "\n"
            + "4, TO " + ACTIONS[4].toString() + "\n";

    private final String name;
    private final Hand hole;
    private final Hand hand;
    //private final Action latestAction;
    private Integer chips;
    private int currentBet;
    private boolean hasFolded;

    /**
     * Construct a player with the name "name"
     *
     * @param name name of the player
     * @param chips number of player's chips
     */
    public Player(String name, int chips) {
        this.name = name;
        hole = new Hand();
        hand = new Hand();
        //latestAction = null;
        this.chips = chips;
        currentBet = 0;
        hasFolded = false;
    }

    public Card getCard(int index) {
        return hole.getCard(index);
    }

    public Integer getChips() {
        return chips;
    }

    public Integer getCurrentBet() {
        return currentBet;
    }

    public String getName() {
        return name;
    }

    public boolean hasFolded() {
        return hasFolded;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
        output(name + "\tcurrent bet = " + currentBet);
    }

    public int getActionChoice() {
        while (true) {
            output(getName() + ": " + ACT_PROMPT_MESSAGE);

            Integer choice = getIntInput();

            if (choice != null && choice >= 0 && choice <= 4) {
                return choice;
            }
        }
    }

    public Decision act(int bigBlind, int highestBet) {
        Action chosenAction = ACTIONS[this.getActionChoice()];
        int bet = this.bet(chosenAction, bigBlind, highestBet);

        return new Decision(chosenAction, bet);
    }

    int bet(Action action, int bigBlind, int highestBet) {
        switch (action) {
            case ALL_IN:
                return getChips();

            case CALL:
                return highestBet;

            case CHECK:
                return highestBet;

            case FOLD:
                return currentBet;

            case RAISE:
                return highestBet + raise(bigBlind, highestBet);

            default:
                return -1;
        }
    }

    private void addToHand(Card card) {
        hand.addCard(card);
    }

    public void addCommunity(Card card) {
        addToHand(card);
    }

    public void addToHole(Card card){
        if(hole.size() < 2){
            if(hole.addCard(card)){
                addToHand(card);
            }
            else{
                throw new UnsupportedOperationException();
            }
        }
    }
    
    public int betSmallBlind() {
        String message = this.getName() + ", bet the small blind, please:\n";
        Integer bet;

        while (true) {
            this.output(message);
            bet = this.getIntInput();

            if (bet != null) {
                return bet;
            }
        }
    }

    public int betBigBlind(int smallBlind) {
        String promptMessage = this.getName()
                + ", pay the big blind, please. The small blind is "
                + smallBlind + "\n";
        String errorMessage = "Bet twice the small blind, please.";
        Integer bet;

        while (true) {
            this.output(promptMessage);
            bet = this.getIntInput();

            if (bet != null && bet == 2 * smallBlind) {
                return bet;
            }

            this.output(errorMessage);
        }
    }

    public boolean isActive() {
        return !hasFolded();
    }

    public Integer getIntInput() {
        Scanner input = new Scanner(System.in);
        Integer entry;

        try {
            entry = input.nextInt();
        } catch (Exception e) {
            return null;
        }

        return entry;
    }

    public void fold() {
        hasFolded = true;
    }

    public void output(String message) {
        System.out.println(message);
    }

    public void removeBetChips() {
        chips -= currentBet;
    }

    @Override
    public String toString() {
        return "Player{" + "name=" + name + ", hole=" + hole + 
                ", cards=" + hand + ", chips=" + chips + 
                ", currentBet=" + currentBet + ", hasFolded=" + hasFolded + '}';
    }

    public static void main(String args[]) {
        /*ArrayList<Card> cards = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            Card card = Card.random();
            System.out.println("card = " + card);

            while (cards.contains(card)) {
                card = Card.random();
            }

            cards.add(card);
        }

        cards.sort(new DecreasingSense());

        for (int i = 0; i < 10; ++i) {
            System.out.println("card" + i + cards.get(i));
        }*/
        
        Player gery = new Player("Gery", 500);
        
        for(int i = 0; i < 5; ++i){
            Card card = Card.random();
            gery.addToHole(card);
            System.out.println("card" + i + " = " + card);
        }
        
        System.out.println("gery = " + gery);
        
        for(int i = 0; i < 10; ++i){
            Card card = Card.random();
            gery.addCommunity(card);
            System.out.println("gery = " + gery);
        }
    }

    private int raise(int bigBlind, int highestBet) {
        Integer raise;
        int maxRaise = getChips() - highestBet;

        while (true) {
            output(getName() + ": " + ACT_PROMPT_MESSAGE);
            output(betInfo(bigBlind, highestBet));
            output(raiseExample(bigBlind, highestBet));
            raise = this.getIntInput();

            if (raise != null && raise >= bigBlind
                    && (raise % bigBlind == 0) && raise < maxRaise) {
                break;
            }
        }
        return raise;
    }

    private String betInfo(int bigBlind, int highestBet) {
        return "The highest bet is " + highestBet + " chips " + "and the big "
                + "blind equals " + bigBlind + "\n";
    }

    private String raiseExample(int bigBlind, int highestBet) {
        int multiple = (1 + (int) (3 * Math.random()));
        int raiseExample = multiple * bigBlind;
        String message
                = "\nThe raise must be a multiple of " + bigBlind
                + "\nExample: a raise might be " + raiseExample
                + "\nYour total bet would be then: " + highestBet + " + "
                + raiseExample + " = " + (highestBet + raiseExample) + "\n";

        return message;
    }

    boolean canRaise(int bigBlind, int highestBet) {
        return ((this.getChips() - highestBet) / bigBlind) >= 1;
    }
}
