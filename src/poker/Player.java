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

    private final String name;
    private final ArrayList<Card> holeCards;
    private Integer chips;
    private int currentBet;
    private boolean hasFolded;

    /**
     * Construct a player with the name "name"
     * @param name name of the player
     * @param chips number of player's chips
     */
    public Player(String name, int chips) {
        this.name = name;
        holeCards = new ArrayList<>();
        this.chips = chips;
        currentBet = 0;
        hasFolded = false;
    }
    
    public Card getCard(int index){
        return holeCards.get(index);
    }
    
    public Integer getChips(){
        return chips;
    }

    public Integer getCurrentBet() {
        return currentBet;
    }
    
    public String getName() {
        return name;
    }

    public int getNumberOfCards(){
        return holeCards.size();
    }

    public boolean hasFolded() {
        return hasFolded;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
        output(name + "\tcurrent bet = " + currentBet);
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

    /*public int bet(){
        return getIntInput();
    }*/
    
    public boolean canPlay(){
        return !hasFolded();
    }
    
    public Integer getIntInput() {
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

    public void fold(){
        hasFolded = true;
    }
    
    public void output(String message) {
        System.out.println(message);
    }
    
    public void outputError(String message){
        System.err.println(message);
    }
    
    public void removeBetChips(){
        chips -= currentBet;
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
        
        
        /*for (int i = 0; i < 4; ++i) {
            Decision decision = 
                    player.actGivenBigBlindAndHighestBet(bigBlind, highestBet);
            System.out.println("Player's bet = " + decision.getChips());
            
            if(decision.getAction() == Action.RAISE){
                highestBet = decision.getChips();
            }
        }*/
    }
}
