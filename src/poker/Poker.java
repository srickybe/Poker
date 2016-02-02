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
            + "4, TO " + ACTIONS[4].toString() + "\n";

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
        setSmallBlind();
        smallBlindPlayer.setCurrentBet(smallBlind);
        setBigBlind();
        bigBlindPlayer.setCurrentBet(bigBlind);
        setWhoRaised(bigBlindPlayer);
        highestBet = bigBlind;
        dealOneCardToPlayers();
        dealOneCardToPlayers();
        preFlop();
        pot = collectAllBets();
        output("POT = " + pot);
        decrementAllPlayersChips();
    }

    private void setBigBlind() {
        bigBlind = bigBlindPlayer.betBigBlind(smallBlind);
    }

    private void setSmallBlind() {
        smallBlind = smallBlindPlayer.betSmallBlind();
    }
    
    /*
     * returns the first active player after "player" 
     * or null if no active player has been found after "player"  
     */
    public Player firstActivePlayerAfter(Player player) {
        CircularListIterator<Player> it = listIteratorToPlayer(player);
        Player currentPlayer = it.next();

        while (true) {
            currentPlayer = it.next();

            if (currentPlayer == player) {
                return null;
            }

            if (currentPlayer.canPlay()) {
                return currentPlayer;
            }
        }
    }

    public boolean setSmallBlindPlayer() {
        Player player = firstActivePlayerAfter(buttonPlayer);

        if (player != null) {
            smallBlindPlayer = player;
            return true;
        } else {
            return false;
        }
    }

    public boolean setBigBlindPlayer() {
        Player player = firstActivePlayerAfter(smallBlindPlayer);

        if (player != null) {
            bigBlindPlayer = player;
            return true;
        } else {
            return false;
        }
    }

    public boolean isValidAction(Action action, Player player) {
        return (action != Action.RAISE || canPlayerRaise(player))
                && (action != Action.CHECK || player == whoRaised)
                && (action != Action.FOLD || player != whoRaised)
                && (action != Action.CALL || player != whoRaised);
    }

    public Action getPlayerAction(Player player) {
        Action action;
        String promptMessage = player.getName() + ": " + ACT_PROMPT_MESSAGE;

        while (true) {
            action = player.act(promptMessage, betInfo());

            if (isValidAction(action, player)) {
                return action;
            }
        }
    }

    public void makeAllPlayersBet() {
        CircularListIterator<Player> it = listIteratorToUnderTheGun();

        while (true) {
            Player currentPlayer = it.next();

            if (currentPlayer.canPlay()) {
                Action action = getPlayerAction(currentPlayer);
                updatePlayer(currentPlayer, action);

                if (action == Action.RAISE) {
                    setWhoRaised(currentPlayer);
                } else {
                    if (actionEndedRound(action, currentPlayer)) {
                        break;
                    }
                }
            }
        }
    }

    private void setWhoRaised(Player currentPlayer) {
        whoRaised = currentPlayer;
    }

    private boolean actionEndedRound(Action action, Player currentPlayer) {
        if ((action == Action.CHECK) && (currentPlayer == whoRaised)) {
            return true;
        } else {
            if ((action == Action.FOLD) && (countActivePlayer() == 1)) {
                return true;
            }
        }
        return false;
    }
    
    private void preFlop() {
        makeAllPlayersBet();
    }
    
    private int collectAllBets() {
        CircularListIterator<Player> it = listIteratorToPlayer(buttonPlayer);
        Player currentPlayer = it.next();
        int result = currentPlayer.getCurrentBet();

        while (true) {
            currentPlayer = it.next();

            if (currentPlayer == buttonPlayer) {
                return result;
            }

            result += currentPlayer.getCurrentBet();
        }
    }

    private void decrementAllPlayersChips() {
        CircularListIterator<Player> it = listIteratorToPlayer(buttonPlayer);
        Player currentPlayer = it.next();
        currentPlayer.removeBetChips();

        while (true) {
            currentPlayer = it.next();

            if (currentPlayer == buttonPlayer) {
                return;
            }
            
            currentPlayer.removeBetChips();
        }
    }

    private CircularListIterator<Player> listIteratorToUnderTheGun() {
        CircularListIterator<Player> it = listIteratorToPlayer(bigBlindPlayer);

        if (it != null) {
            output("it.next().getName() = " + it.next().getName());
            output("bigBlindPlayer.getName() = " + bigBlindPlayer.getName());
            return it;
        }

        return null;
    }

    private CircularListIterator<Player> listIteratorToPlayer(Player player) {
        return players.listIterator(players.indexOf(player));
    }

    private int countActivePlayer() {

        if (players.isEmpty()) {
            return 0;
        }

        CircularListIterator<Player> it = players.listIterator();
        Player firstPlayer = it.next();
        int count = firstPlayer.canPlay() ? 1 : 0;

        while (true) {
            Player currentPlayer = it.next();

            if (currentPlayer == firstPlayer) {
                return count;
            }

            if (currentPlayer.canPlay()) {
                ++count;
            }
        }
    }

    private Action act(Player player) {
        Integer choice = null;

        while (true) {
            player.output(player.getName() + ": " + ACT_PROMPT_MESSAGE);
            player.output(betInfo());
            choice = player.getIntInput();

            if ((choice != null)
                    && (choice >= 0 && choice <= 4)
                    && (ACTIONS[choice] != Action.RAISE || canPlayerRaise(player))
                    && (ACTIONS[choice] != Action.CHECK || player == whoRaised)
                    && (ACTIONS[choice] != Action.FOLD || player != whoRaised)
                    && (ACTIONS[choice] != Action.CALL || player != whoRaised)) {
                break;
            }
        }

        return ACTIONS[choice];
    }

    private void updatePlayer(Player player, Action action) {
        switch (action) {
            case ALL_IN:
                output("NOT DONE YET");
                break;

            case CALL:
                player.setCurrentBet(highestBet);
                break;

            case CHECK:
                player.setCurrentBet(highestBet);
                break;

            case FOLD:
                player.fold();
                break;

            case RAISE:
                highestBet += getPlayerRaise(player);
                player.setCurrentBet(highestBet);
                break;

            default:
        }
    }

    private int getPlayerRaise(Player player) {
        Integer raise;
        int maxRaise = player.getChips() - highestBet;
        
        while(true){
            player.output(player.getName() + ": " + ACT_PROMPT_MESSAGE);
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

    private String betInfo() {
        return "The highest bet is " + highestBet + " chips " + "and the big "
                + "blind equals " + bigBlind + "\n";
    }

    /**
     * deal a card to all the players starting form the small blind
     */
    public void dealOneCardToPlayers() {
        CircularListIterator<Player> it 
                = listIteratorToPlayer(smallBlindPlayer);
        Player currentPlayer = it.next();
        
        while(true){
            currentPlayer.addCard(removeTopCard());
            output("currentPlayer = " + currentPlayer.toString());
            currentPlayer = it.next();
            
            if(currentPlayer == smallBlindPlayer){
                break;
            }
        }
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
            output("Coose a number of players between maximal 2 and "
                    + MAX_NUMBER_OF_PLAYERS);
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
        Poker game = new Poker();
    }
}
