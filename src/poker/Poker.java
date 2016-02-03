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

            if (currentPlayer.isActive()) {
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
        return (action != Action.RAISE || player.canRaise(bigBlind, highestBet))
                && (action != Action.CHECK || player == whoRaised)
                && (action != Action.FOLD || player != whoRaised)
                && (action != Action.CALL || player != whoRaised);
    }

    public void makeAllPlayersBet() {
        CircularListIterator<Player> it = listIteratorToUnderTheGun();

        while (true) {
            Player currentPlayer = it.next();

            if (currentPlayer.isActive()) {
                Decision decision;

                while (true) {
                    decision = currentPlayer.act(bigBlind, highestBet);

                    if (isValidAction(decision.getAction(), currentPlayer)) {
                        break;
                    }
                }

                applyDecision(decision, currentPlayer);

                if (actionEndedRound(decision.getAction(), currentPlayer)) {
                    break;
                }

            }
        }
    }

    private void applyDecision(Decision decision, Player currentPlayer) {
        switch (decision.getAction()) {
            case CALL:

            case CHECK:
                currentPlayer.setCurrentBet(decision.getBet());
                break;

            case RAISE:
                setWhoRaised(currentPlayer);
                highestBet = decision.getBet();
                currentPlayer.setCurrentBet(highestBet);
                break;

            case FOLD:
                currentPlayer.fold();
                break;

            default:
                output("CASE." + decision.getAction() + "not treated");
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
        int count = firstPlayer.isActive() ? 1 : 0;

        while (true) {
            Player currentPlayer = it.next();

            if (currentPlayer == firstPlayer) {
                return count;
            }

            if (currentPlayer.isActive()) {
                ++count;
            }
        }
    }

    /**
     * deal a card to all the players starting form the small blind
     */
    public void dealOneCardToPlayers() {
        CircularListIterator<Player> it
                = listIteratorToPlayer(smallBlindPlayer);
        Player currentPlayer = it.next();

        while (true) {
            currentPlayer.addCard(removeTopCard());
            output("currentPlayer = " + currentPlayer.toString());
            currentPlayer = it.next();

            if (currentPlayer == smallBlindPlayer) {
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
