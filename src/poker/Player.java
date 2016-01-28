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
public class Player {

    private final Action ACTIONS[] = {
        Action.CALL, 
        Action.RAISE, 
        Action.FOLD, 
        Action.CHECK
    };
    private String actPrompt = 
            "Type\n"
            + "1, to raise\n"
            + "2, to call\n"
            + "3, to fold\n"
            + "4, to check\n";
    
    private final String name;
    private final ArrayList<Card> pocket;

    public Player(String name) {
        this.name = name;
        pocket = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean addCard(Card card) {
        if (pocket.size() < 2) {
            return pocket.add(card);
        }

        return false;
    }

    public int bet(){
        Scanner input = new Scanner(System.in);
        
        return input.nextInt();
    }
    
    public Action act(){
        System.out.println(actPrompt);
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        
        while (choice < 1 || choice > 4) {
            System.out.println(actPrompt);
            choice = input.nextInt();
        }
        
        return ACTIONS[choice];
    }
    
    @Override
    public String toString() {
        return "Player{" + "name=" + name + ", pocket=" + pocket + '}';
    }

}
