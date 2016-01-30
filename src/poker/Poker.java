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
            + "4, TO " + ACTIONS[3].toString() + "\n";

    private final int MAX_NUMBER_OF_PLAYERS = 10;
    private final int MINIMAL_STARTING_CHIPS_PER_PLAYER = 1000;
    private final CardDeck cards;
    private final Stack<Card> muck;
    private final CircularList<Player> players;
    private Player buttonPlayer;
    private Player smallBlindPlayer;
    private Player bigBlindPlayer;
    private Player whoRaised;
    private int smallBlind;
    private int bigBlind;
    private Integer pot;
    private Integer highestBet;

    public Poker() {
        muck = new Stack();
        cards = new CardDeck();
        players = createPlayers();
        game();
    }

    private void game() {
        cards.shuffle();
        setPlayersRole();
        smallBlind = makePlayerPaySmallBlind(smallBlindPlayer);
        bigBlind = makePlayerPayBigBlind(bigBlindPlayer, smallBlind);
        whoRaised = bigBlindPlayer;
        highestBet = bigBlind;
        pot = bigBlind;
        dealOneCardToPlayers();
        dealOneCardToPlayers();
        preFlop();
    }
        
    public void preFlop() {
        Player currentPlayer;
        CircularListIterator<Player> it = players.listIterator();
        
        if(it != null && it.moveTo(bigBlindPlayer)){
            currentPlayer = it.next();
        }
        else{
            return;
        }

        while (true) {
            if (currentPlayer.canPlay()) {
                Action action = act(currentPlayer);
                
                if (action == Action.RAISE) {
                    whoRaised = currentPlayer;
                }
                else{
                    if(action == Action.CHECK && currentPlayer == whoRaised){
                        break;
                    }
                    else{
                        if(countActivePlayer() == 1){
                            break;
                        }
                    }
                }
            }

            currentPlayer = it.next();
        }
    }

    int countActivePlayer(){
        CircularListIterator<Player> it = players.listIterator();
        
        if(it == null){
            return 0;
        }

        Player firstPlayer = it.next();
        int count = firstPlayer.canPlay()? 1: 0;
        
        while(true){
            Player currentPlayer = it.next();
            
            if(currentPlayer == firstPlayer){
                return count;
            }
            
            ++count;
        }
    }
    
    public Action act(Player player) {
        Integer choice = null;

        while (true) {
            player.output(player.getName() + ": " + ACT_PROMPT_MESSAGE);
            player.output(betInfo());
            choice = player.getIntInput();

            if ((choice != null)
                    && (ACTIONS[choice] != Action.RAISE || canPlayerRaise(player))
                    && (ACTIONS[choice] != Action.CHECK || player == whoRaised)) {
                break;
            }
        }

        return ACTIONS[choice];
    }

    public int betGivenAction(Player player, Action action) {

        switch (action) {
            case ALL_IN:
                output("NOT DONE YET");
                break;

            case CALL:
                player.removeFromTotalChips(highestBet);
                pot += highestBet;
                break;

            case CHECK:
                break;

            case FOLD:
                player.fold();
                break;

            case RAISE:
                highestBet += getRaise(player);
                player.removeFromTotalChips(highestBet);
                break;

            default:

        }

        return 0;
    }

    private Integer getRaise(Player player) {
        Integer raise = null;
        int maxRaise = player.getChips() - highestBet;

        while (true) {
            player.output(betInfo());
            player.output(raiseExample());
            raise = player.getIntInput();

            if (raise != null
                    && raise >= bigBlind
                    && (raise % bigBlind == 0)
                    && raise < maxRaise) {
                break;
            }
        }

        return raise;
    }

    private boolean canPlayerRaise(Player player) {

        return ((player.getChips() - highestBet) / bigBlind) >= 1;
    }

    private String raiseExample() {
        int multiple = (1 + (int) (3 * Math.random()));
        int raiseExample = multiple * bigBlind;
        String message
                = "\nThe raise must be a multiple of " + bigBlind
                + "\nExample: a raise might be " + raiseExample
                + "\nYour total bet would be then: " + highestBet + " + "
                + raiseExample + " = " + (highestBet + raiseExample) + "\n";

        return message;
    }

    public String betInfo() {
        return "The highest bet is " + highestBet + " chips " + "and the big "
                + "blind equals " + bigBlind + "\n";
    }

    public int makePlayerPaySmallBlind(Player player) {
        String message = player.getName() + ", bet the small blind, please:\n";
        player.output(message);
        return player.bet();
    }

    public int makePlayerPayBigBlind(Player player, int smallBlind) {
        String message = player.getName()
                + ", pay the big blind, please. The small blind is "
                + smallBlind + "\n";
        player.output(message);
        int choice = player.bet();

        while (choice != 2 * smallBlind) {
            player.output("Bet twice the small blind, please.");
            choice = player.bet();
        }

        return choice;
    }

    /**
     * deal a card to all the players starting form the small blind
     */
    public void dealOneCardToPlayers() {
        CircularListIterator<Player> i1
                = players.listIterator(smallBlindPlayer);

        Player current = i1.next();
        current.addCard(removeTopCard());
        System.out.println("current = " + current);

        CircularListIterator<Player> i2
                = players.listIterator(smallBlindPlayer);

        while (!i1.equals(i2)) {
            current = i1.next();
            current.addCard(removeTopCard());
            System.out.println("current = " + current);
        }
    }

    public Player getPlayers(int index) {
        return players.get(index);
    }

    private void setPlayersRole() {
        if (players.size() > 2) {
            CircularListIterator<Player> it = players.listIterator();
            buttonPlayer = it.next();
            smallBlindPlayer = it.next();
            bigBlindPlayer = it.next();
        } else if (players.size() == 2) {
            CircularListIterator<Player> it = players.listIterator();
            buttonPlayer = it.next();
            smallBlindPlayer = buttonPlayer;
            bigBlindPlayer = it.next();
        } else if (players.size() == 1) {
        }
    }

    private CircularList<Player> createPlayers() {
        int nPlayers = promptNumberOfPlayers();
        ArrayList<String> names = promptPlayersName(nPlayers);

        return createPlayersFromNames(names);
    }

    private ArrayList<String> promptPlayersName(int nPlayers) {
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

    private int promptNumberOfPlayers() {
        output("Enter the number of players");
        Integer nPlayers = getIntInput();

        while (nPlayers == null || nPlayers > MAX_NUMBER_OF_PLAYERS) {
            output("Too high! The maximal number of "
                    + "players is " + MAX_NUMBER_OF_PLAYERS);
            nPlayers = getIntInput();
        }

        return nPlayers;
    }

    private Integer getIntInput() {
        Scanner input = new Scanner(System.in);
        Integer entry;

        try {
            entry = input.nextInt();
        } catch (Exception e) {
            return null;
        }

        return entry;
    }

    private CircularList<Player> createPlayersFromNames(ArrayList<String> names) {
        CircularList<Player> result = new CircularList<>();

        for (int i = 0; i < names.size(); ++i) {
            result.add(new Player(names.get(i),
                    MINIMAL_STARTING_CHIPS_PER_PLAYER));
        }

        return result;
    }

    private String promptPlayerName() {
        Scanner input = new Scanner(System.in);
        output("Enter a player's name");

        return input.next();
    }

    private boolean addPlayer(Player player) {
        if (players.size() < MAX_NUMBER_OF_PLAYERS) {
            if (!isPlayer(player)) {
                return players.add(player);
            } else {
                return false;
            }
        }

        return false;
    }

    private boolean isPlayer(Player player) {
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
        res += ",\ncards=" + cards.toString();
        res += ",\nplayers=[";

        for (int i = 0; i < players.size(); ++i) {
            res += "\n" + i + ":" + players.get(i);
        }

        res += "],\nbuttonPlayer=" + buttonPlayer;
        res += "],\nsmallBlindPlayer=" + smallBlindPlayer;
        res += "],\nbigBlindPlayer=" + bigBlindPlayer;
        res += "}";

        return res;
    }

    private Card removeTopCard() {
        return cards.pop();
    }

    private static void output(String message) {
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
                cards[i] = poker.removeTopCard();
            }

            Hand hand = new Hand(cards[0], cards[1], cards[2], cards[3], cards[4]);
            Eval eval = hand.computeRankAndValue();

            if (eval.getRank() > maxLevel) {
                maxLevel = eval.getRank();
                maxValue = eval.getValue();
                output("hand = " + hand);
                output("eval = " + eval + "\n");
            } else if (eval.getRank() == maxLevel && eval.getValue() > maxValue) {
                maxValue = eval.getValue();
                output("hand = " + hand);
                output("eval = " + eval + "\n");
            }
        }
    }
}
