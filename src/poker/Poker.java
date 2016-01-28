/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author john
 */
public class Poker {

    private final int MAX_NUMBER_OF_PLAYERS = 10;
    private final int MINIMAL_STARTING_CHIPS_PER_PLAYER = 1000;
    private final Stack<Card> cards;
    private final ArrayList<Player> players;
    private Integer buttonIndex;
    private Integer smallBlindIndex;
    private Integer bigBlindIndex;
    private Integer bet;

    public Poker() {
        cards = createCardDeck();
        players = createPlayers();
        game();
    }
    
    /**
     * returns a 52-card deck. The cards are ordered from the ranks 2 to ace, 
     * and the suits in alphabetic order. The card at the top is Ace Spade and
     * at the bottom, Two Club.
     * @return a stack of cards. 
     */
    private Stack<Card> createCardDeck(){
        Stack<Card> result = new Stack<>();
        
        for (int i = 0; i < 13; ++i) {
            for (int j = 0; j < 4; ++j) {
                result.add(new Card(i, j));
            }
        }
        
        return result;
    }

    /**
     * Shuffles a stack of cards
     * @param cards 
     */
    private void shuffle(Stack<Card> cards) {
        int n = cards.size();
        
        for (int i = 0; i < n; ++i) {
            int r = (int) (n * Math.random());
            Card tmp = cards.get(i);
            cards.set(i, cards.get(r));
            cards.set(r, tmp);
        }
    }

    private void game() {
        shuffle(cards);
        setPlayersRole();
        bet = betSmallBlind();
        bet = betBigBlind(bet);
        distributeCards();
        distributeCards();
        preFlop();
    }

    public void preFlop(){
        int startIndex = bigBlindIndex + 1;
        
        if(startIndex == players.size()){
            startIndex = 0;
        }
        
        for(int i = startIndex; i < players.size(); ++i){
            output("Please, " + players.get(i).getName() + ":");
            output(players.get(i).act().toString());
        }
    }
    
    public int betSmallBlind(){
        Player player = players.get(smallBlindIndex);
        String message = player.getName() + ", bet the small blind, please:\n";
        player.output(message);
        return player.bet();
    }
    
    public int betBigBlind(int smallBlind){
        Player player = players.get(bigBlindIndex);
        String message = player.getName() +
                ", bet the big blind, please. The small blind is " +
                smallBlind + "\n";
        player.output(message);
        int choice = player.bet();
        
        while(choice != 2 * bet){
            player.output("Bet twice the small blind, please.");
            choice = player.bet();
        }
        
        return choice;
    }
    
    public void distributeCards(){
        for(int i = smallBlindIndex; i < players.size(); ++i){
            output("Giving a card to player" + i);
            
            if(!players.get(i).addCard(getTopCard())){
                
            }
        }
        
        if(smallBlindIndex > 0){
            for(int i = 0; i < smallBlindIndex; ++i){
                output("Giving a card to player" + i);
                
                if(!players.get(i).addCard(getTopCard())){
                    
                }
            }
        }
    }

    public Player getPlayers(int index) {
        return players.get(index);
    }
    
    
    public void setPlayersRole() {
        if (players.size() > 2) {
            buttonIndex = 0;
            smallBlindIndex = 1;
            bigBlindIndex = 2;
        } 
        else if (players.size() == 2) {
            buttonIndex = 0;
            smallBlindIndex = 0;
            bigBlindIndex = 1;
        } 
        else if (players.size() == 1) {
        }
    }

    private ArrayList<Player> createPlayers() {
        int nPlayers = promptNumberOfPlayers();
        ArrayList<String> names = promptPlayersName(nPlayers);
        
        return createPlayersFromNames(names);
    }

    public ArrayList<String> promptPlayersName(int nPlayers) {
        ArrayList<String> names = new ArrayList<>();
        
        for (int i = 0; i < nPlayers; ++i) {
            String name = promptPlayerName();
            
            while (names.contains(name)) {
                output("There's already a player named " + name);
                name = promptPlayerName();
            }
            
            names.add(name);
        }

        return names;
    }

    public int promptNumberOfPlayers() {
        output("Enter the number of players");
        Scanner input = new Scanner(System.in);
        int nPlayers = input.nextInt();

        while (nPlayers > MAX_NUMBER_OF_PLAYERS) {
            System.out.print("Too high! The maximal number of ");
            output("players is " + MAX_NUMBER_OF_PLAYERS);
            nPlayers = input.nextInt();
        }
        
        return nPlayers;
    }

    public ArrayList<Player> createPlayersFromNames(ArrayList<String> names) {
        ArrayList<Player> result = new ArrayList<>();
        
        for (int i = 0; i < names.size(); ++i) {
            result.add(new Player(names.get(i), 
                    MINIMAL_STARTING_CHIPS_PER_PLAYER));
        }
        
        return result;
    }

    public String promptPlayerName() {
        Scanner input = new Scanner(System.in);
        output("Enter a player's name");
        
        return input.next();
    }

    public boolean addPlayer(Player player) {
        if (players.size() < MAX_NUMBER_OF_PLAYERS) {
            if (!isPlayer(player)) {
                return players.add(player);
            } else {
                return false;
            }
        }
        
        return false;
    }

    public boolean isPlayer(Player player) {
        for (int i = 0; i < players.size(); ++i) {
            if (players.get(i).getName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String res = "Poker{" + "\nMAX_NUMBER_OF_PLAYERS=" 
                + MAX_NUMBER_OF_PLAYERS;
        res += ",\ncards=[";
        
        for(int i = 0; i < cards.size(); ++i){
            res += "\n" + i + ":" + cards.get(i);
        }
        
        res += "],\nplayers=[";
        
        for(int i = 0; i < players.size(); ++i){
            res += "\n" + i + ":" + players.get(i);
        }
        
        res += "],\nbuttonIndex=[" + buttonIndex;
        res += "],\nsmallBlindIndex=[" + smallBlindIndex;
        res += "],\nbigBlindIndex=[" + bigBlindIndex;
        res += "}";
        
        return res;
    }

    
    public Card getTopCard() {
        return cards.pop();
    }
    
    public static void output(String message){
        System.out.println(message);
    }

    public static void main(String args[]) {
        int maxValue = 0;
        int maxLevel = 0;
        
        for (int count = 0; count < 1; ++count) {
            Poker poker = new Poker();
            output("poker = " + poker);
            Card[] cards = new Card[5];

            for (int i = 0; i < 5; ++i) {
                cards[i] = poker.getTopCard();
            }

            Hand hand = new Hand(cards[0], cards[1], cards[2], cards[3], cards[4]);
            Eval eval = hand.computeRankAndValue();
            
            if (eval.getRank() > maxLevel){
                maxLevel = eval.getRank();
                maxValue = eval.getValue();
                output("hand = " + hand);
                output("eval = " + eval + "\n");
            }
            
            else if(eval.getRank() == maxLevel && eval.getValue() > maxValue) {
                maxValue = eval.getValue();
                output("hand = " + hand);
                output("eval = " + eval + "\n");
            }
        }
    }
}
