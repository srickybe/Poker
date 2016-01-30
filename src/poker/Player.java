/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Invariant: the number of cards in the pocket must be inferior or equal to two
 * @author john
 */
public class Player {

    private final Action ACTIONS[] = {
        Action.CALL, 
        Action.CHECK,
        Action.FOLD,
        Action.RAISE
    };
    private final String ACT_PROMPT_MESSAGE = 
            "Type\n"
            + "0, TO " + ACTIONS[0].toString() + "\n"
            + "1, TO " + ACTIONS[1].toString() + "\n"
            + "2, TO " + ACTIONS[2].toString() + "\n"
            + "3, TO " + ACTIONS[3].toString() + "\n";
    
    private final String name;
    private final ArrayList<Card> holeCards;
    private Integer chips;

    /**
     * Construct a player with the name "name"
     * @param name name of the player
     * @param chips number of player's chips
     */
    public Player(String name, int chips) {
        this.name = name;
        holeCards = new ArrayList<>();
        this.chips = chips;
    }
    
    public Card getCard(int index){
        return holeCards.get(index);
    }
    
    public Integer getChips(){
        return chips;
    }
    
    public String getName() {
        return name;
    }

    public int getNumberOfCards(){
        return holeCards.size();
    }
    
    public Decision actGivenBigBlindAndHighestBet(int bigBlind, int highestBet){
        
        return decideGivenBigBlindAndHighestBet(bigBlind, highestBet);
    }
    
    /**
     * Adds a card to the player's pocket if the number of cards of the 
     * player's pocket is strictly inferior to two
     * @param card card to be added to the player's pocket
     * @return true if the card has been added, false otherwise
     */
    public boolean addCard(Card card) {
        if(holeCards.isEmpty())
            return holeCards.add(card);
 
        if(holeCards.size() == 1){
            if(holeCards.get(0).compareTo(card) > 0)
                return holeCards.add(card);
          
            holeCards.add(0, card);
            return true;
        }

        return false;
    }

    public int bet(){
        return getIntInput();
    }
    
    private Integer getChoice(String errorMessage) {
        Integer choice = getIntInput();
        
        while(choice == null || choice > ACTIONS.length || choice < 0) {
            output(errorMessage);
            output(ACT_PROMPT_MESSAGE);
            choice = getIntInput();
        }
        
        return choice;
    }
    
    private Integer getIntInput() {
        Scanner input = new Scanner(System.in);
        Integer entry;

        try {
            entry = input.nextInt();
        } 
        catch (Exception e) {
            return null;
        }

        return entry;
    }
    
    private Decision decideGivenBigBlindAndHighestBet(
            int bigBlind, 
            int highestBet){
        String errorMessage = "Wrong choice!";
        output("" + "Highest bet: " + highestBet + "\n" + ACT_PROMPT_MESSAGE);
        Action chosenAction = ACTIONS[getChoice(errorMessage)];
        
        switch (chosenAction){
            case CALL:
                return new Decision(Action.CALL, highestBet);
                
            case RAISE:
                output( name + ", please enter your raise (smallest: " +
                        bigBlind + ")\n");
                
                return new Decision(
                        Action.RAISE, 
                        raiseGivenBigBlindAndHighestBet(bigBlind, highestBet));
                
            case FOLD:
                return new Decision(Action.FOLD, null);
                
            case CHECK:
                return new Decision(Action.CHECK, highestBet);
                
            default:
                return new Decision(null, null);
        }
    }

    private int raiseGivenBigBlindAndHighestBet(
            int bigBlind, 
            int highestBet) {
        Integer raise = getIntInput();
        
        while(raise == null || raise < bigBlind || (raise % bigBlind != 0)){
            int multiple = (1 + (int)(3 * Math.random()));
            int raiseExample = multiple * bigBlind;
            String message = 
                    "\nThe raise must be a multiple of " + bigBlind +
                    "\nExample: a raise might be " + raiseExample +
                    "\nYour total bet would be then: " + highestBet + " + " +
                    raiseExample + " = " + (highestBet + raiseExample) + "\n";
            output(message);
            raise = getIntInput();
        }
        
        return raise + highestBet;
    }
    
    public void output(String message) {
        System.out.println(message);
    }
    
    public void outputError(String message){
        System.err.println(message);
    }
    
    @Override
    public String toString() {
        return "Player{" + "name=" + name + ", pocket=" + holeCards + ", chips =" +
                chips + "}";
    }
    
    public static void main(String args[]){
        Player player = new Player("Ricky", 500);
        
        while(true){
            int rank = (int)(13.0 * Math.random());
            int suit = (int)(4.0 * Math.random());
            Card card = new Card(rank, suit);
            
            if(player.getNumberOfCards() == 0)
                player.addCard(card);
            
            if(player.getNumberOfCards() == 1){
                rank = (int)(13.0 * Math.random());
                
                while(player.getCard(0).getRank() != rank){
                    rank = (int)(13.0 * Math.random());
                }
                
                suit = (int)(4.0 * Math.random());
                
                while(player.getCard(0).getSuit() == suit){
                    suit = (int)(4.0 * Math.random());
                }
                
                player.addCard(new Card(rank, suit));
                break;
            }
        }
        
        player.output(player.toString());
        int bigBlind = 25;
        int highestBet = 100 + (int)(10 * Math.random()) * 25;
        player.output("This is the highest bet:" + highestBet);
        
        
        for (int i = 0; i < 4; ++i) {
            Decision decision = 
                    player.actGivenBigBlindAndHighestBet(bigBlind, highestBet);
            System.out.println("Player's bet = " + decision.getChips());
            
            if(decision.getAction() == Action.RAISE){
                highestBet = decision.getChips();
            }
        }
        
    }
}
