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
            + "1, to raise\n"
            + "2, to call\n"
            + "3, to fold\n"
            + "4, to check\n";
    
    private final String name;
    private final ArrayList<Card> pocket;
    private Integer chips;

    /**
     * Construct a player with the name "name"
     * @param name name of the player
     * @param chips number of player's chips
     */
    public Player(String name, int chips) {
        this.name = name;
        pocket = new ArrayList<>();
        this.chips = chips;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfCards(){
        return pocket.size();
    }
    
    public Card getCard(int index){
        return pocket.get(index);
    }
    
    public Integer getChips(){
        return chips;
    }
    
    /**
     * Adds a card to the player's pocket if the number of cards of the 
     * player's pocket is strictly inferior to two
     * @param card card to be added to the player's pocket
     * @return true if the card has been added, false otherwise
     */
    public boolean addCard(Card card) {
        if(pocket.isEmpty())
            return pocket.add(card);
 
        
        if(pocket.size() == 1){
            if(pocket.get(0).compareTo(card) > 0)
                return pocket.add(card);
          
            pocket.add(0, card);
            return true;
        }

        return false;
    }

    public int bet(){
        return getIntInput();
    }
    
    public Action act(){
        output(ACT_PROMPT_MESSAGE);
        Integer choice = getIntInput();
        
        while (choice == null || choice < 1 || choice > 4) {
            output("Wrong choice!\n");
            output(ACT_PROMPT_MESSAGE);
            choice = getIntInput();
        }
        
        return ACTIONS[choice];
    }
    
    @Override
    public String toString() {
        return "Player{" + "name=" + name + ", pocket=" + pocket + ", chips =" +
                chips + "}";
    }
    
    public void output(String message) {
        System.out.println(message);
    }
    
    public void outputError(String message){
        System.err.println(message);
    }

    public Integer getIntInput() {
        Scanner input = new Scanner(System.in);
        Integer entry;

        try {
            entry = input.nextInt();
        } catch (Exception e) {
            //outputError(e.getMessage());
            return null;
        }

        return entry;
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
        
        
        for (int i = 0; i < 3; ++i) {
            Action action = player.act();

            switch (action) {
                case RAISE:
                    player.output("Bet raised!");
                    break;

                case CHECK:
                    player.output("Bet checked!");
                    break;

                case CALL:
                    player.output("Bet called!");
                    break;

                case FOLD:
                    player.output("Bet folded!");
                    break;
            }
        }
        
    }
}
