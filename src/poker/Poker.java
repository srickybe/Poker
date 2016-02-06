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
    private final ArrayList<Player> players;
    private final CardDeck cards;
    private final Stack<Card> board;
    private final CardDeck muck;
    private int buttonIndex;
    private int smallBlindIndex;
    private int bigBlindIndex;
    private Player whoRaised;
    private int pot;
    private int smallBlind;
    private int bigBlind;
    private int highestBet;

    public Poker() {
        players = createPlayers();
        cards = CardDeck.createFiftyTwoCardDeck();
        board = new Stack<>();
        muck = CardDeck.createEmptyCardDeck();
        pot = 0;
    }

    @Override
    public String toString() {
        return "Poker{" + "MAX_NUMBER_OF_PLAYERS=" + MAX_NUMBER_OF_PLAYERS
                + ", \nplayers=" + players
                + ", \ncards=" + cards
                + ", \nboard=" + board
                + ", \nmuck=" + muck
                + ", \nbuttonIndex=" + buttonIndex
                + ", \nsmallBlindIndex=" + smallBlindIndex
                + ", \nbigBlindIndex=" + bigBlindIndex
                + ", \nwhoRaised=" + whoRaised
                + ", \npot=" + pot
                + ", \nsmallBlind=" + smallBlind
                + ", \nbigBlind=" + bigBlind
                + ", \nhighestBet=" + highestBet
                + "\n}";
    }

    public void start() {
        shuffleCards();
        resetPlayersRole();
        setSmallBlind();
        setBigBlind();
        setWhoRaised(players.get(bigBlindIndex));
        setHighestBet(bigBlind);
        dealOneCardToPlayers();
        dealOneCardToPlayers();
        preFlop();
        output(this.toString());
        burnOneCard();
        flop();
        addFlopToPlayersHand();
        output(this.toString());
        bettingRound2();
        closeBettingRound();
        burnOneCard();
        turn();
        output("board = " + board);
        addCardToPlayersHand(board.lastElement());
        output(this.toString());
        bettingRound2();
        closeBettingRound();
    }

    private void turn() {
        board.add(cards.pop());
    }

    private void bettingRound2() {
        int index = indexOfFirstActivePlayerAfter(buttonIndex);

        if (index == players.size()) {
            throw new UnsupportedOperationException();
        }

        Player firstPlayer = players.get(index);
        Player player = firstPlayer;
        Decision decision = getDecision2(player);
        apply(decision, player);

        while (true) {
            index = nextIndex(index);
            player = players.get(index);

            if (player.isActive()) {
                decision = getDecision2(player);

                apply(decision, player);
                Action action = decision.getAction();

                if (actionEndedBettingRound2(action, player, firstPlayer)) {
                    break;
                }
            }
        }
    }

    private boolean actionEndedBettingRound2(
            Action action,
            Player player,
            Player firstPlayer) {
        if ((action == Action.CHECK)
                && (player == whoRaised
                || (allCheckedFromTo(firstPlayer, player)
                && player == firstPlayer))) {
            return true;
        }

        return (action == Action.FOLD) && (countActivePlayer() == 1);
    }

    private boolean allCheckedFromTo(
            Player firstPlayer,
            Player lastPlayer) {
        int first = players.indexOf(firstPlayer);
        int last = players.indexOf(lastPlayer);

        if (first < last) {
            return allCheckedFromFirstToLast(first, last);
        } else {
            if (first > last) {
                return allCheckedFromLastToEnd(last, first);
            } else {
                return allCheckedFromLastToEnd(last, first);
            }
        }
    }

    private boolean allCheckedFromLastToEnd(int last, int first) {
        for (int i = last; i < players.size(); ++i) {
            Player player = players.get(i);

            if (player.isActive() && player.getLatestAction() != Action.CHECK) {
                return false;
            }
        }

        return allCheckedFromFirstToLast(0, first);
    }

    private boolean allCheckedFromFirstToLast(int first, int last) {
        for (int i = first; i <= last; ++i) {
            Player player = players.get(i);

            if (player.isActive() && player.getLatestAction() != Action.CHECK) {
                return false;
            }
        }

        return true;
    }

    private int indexOfFirstActivePlayerAfter(int index) {
        int pos = players.size();

        if (index >= 0 && index < players.size()) {
            int start = index + 1;

            if (start < players.size()) {
                for (pos = start; pos < players.size(); ++pos) {
                    if (players.get(pos).isActive()) {
                        return pos;
                    }
                }

                for (pos = 0; pos < index; ++pos) {
                    if (players.get(pos).isActive()) {
                        return pos;
                    }
                }

                return players.size();

            } else {
                start = 0;

                for (pos = start; pos < players.size(); ++pos) {
                    if (pos != index && players.get(pos).isActive()) {
                        return pos;
                    }
                }

                return players.size();
            }
        }

        return pos;
    }

    private Decision getDecision2(Player player) {
        Decision decision;

        while (true) {
            decision = player.act(bigBlind, highestBet);

            if (isValidAction2(decision.getAction(), player)) {
                break;
            }
        }

        return decision;
    }

    public boolean isValidAction2(Action action, Player player) {
        return (action != Action.RAISE || player.canRaise(bigBlind, highestBet))
                && (action != Action.CHECK || whoRaised == null || player == whoRaised)
                && (action != Action.FOLD || player != whoRaised)
                && (action != Action.CALL || whoRaised != null);
    }

    private void addFlopToPlayersHand() {
        for (int i = 0; i < 3; ++i) {
            addCardToPlayersHand(board.get(i));
        }
    }

    private void addCardToPlayersHand(Card card) {
        for (int j = 0; j < players.size(); ++j) {
            Player player = players.get(j);

            if (player.isActive()) {
                player.addCommunity(card);
            }
        }
    }

    private void flop() {
        for (int i = 0; i < 3; ++i) {
            board.add(cards.pop());
        }
    }

    private void burnOneCard() {
        muck.add(cards.pop());
    }

    public void preFlop() {
        makeAllPlayersBet();
        //bettingRound2();
        closeBettingRound();
    }

    private void closeBettingRound() {
        pot += collectAllBets();
        decrementAllPlayersChips();
        resetAllPlayersBet();
        setHighestBet(0);
        setSmallBlindIndex(-1);
        setBigBlindIndex(-1);
        setWhoRaised(null);
    }

    private void setSmallBlindIndex(int index) {
        smallBlindIndex = index;
    }

    private void setBigBlindIndex(int index) {
        bigBlindIndex = index;
    }

    private void resetAllPlayersBet() {
        for (int i = 0; i < players.size(); ++i) {
            players.get(i).setCurrentBet(0);
        }
    }

    private void decrementAllPlayersChips() {
        for (int i = 0; i < players.size(); ++i) {
            players.get(i).removeBetChips();
        }
    }

    private int collectAllBets() {
        int result = 0;

        for (int i = 0; i < players.size(); ++i) {
            result += players.get(i).getCurrentBet();
        }

        return result;
    }

    public void makeAllPlayersBet() {
        int index = indexOfUnderTheGun();

        while (true) {
            Player player = players.get(index);

            if (player.isActive()) {
                Decision decision = getDecision(player);

                apply(decision, player);

                if (actionEndedPreFlop(decision.getAction(), player)) {
                    break;
                }
            }

            index = nextIndex(index);
        }
    }

    private int nextIndex(int index) {
        index += 1;

        if (index == players.size()) {
            index = 0;
        }

        return index;
    }

    private int indexOfUnderTheGun() {
        int index = bigBlindIndex + 1;

        if (index >= players.size()) {
            index = 0;
        }

        return index;
    }

    private void apply(Decision decision, Player player) {
        switch (decision.getAction()) {
            case CALL:
                player.setCurrentBet(decision.getBet());
                break;

            case CHECK:
                player.setCurrentBet(decision.getBet());
                break;

            case RAISE:
                setWhoRaised(player);
                highestBet = decision.getBet();
                player.setCurrentBet(highestBet);
                break;

            case FOLD:
                player.fold();
                break;

            default:
                output("CASE." + decision.getAction() + " not treated yet");
        }

        player.setLatestAction(decision.getAction());
    }

    private boolean actionEndedPreFlop(Action action, Player player) {
        if ((action == Action.CHECK) && (player == whoRaised)) {
            return true;
        } else {
            if ((action == Action.FOLD) && (countActivePlayer() == 1)) {
                return true;
            }
        }
        return false;
    }

    private int countActivePlayer() {
        int count = 0;

        for (Player player : players) {
            if (player.isActive()) {
                ++count;
            }
        }

        return count;
    }

    private Decision getDecision(Player player) {
        Decision decision;

        while (true) {
            decision = player.act(bigBlind, highestBet);

            if (isValidAction(decision.getAction(), player)) {
                break;
            }
        }

        return decision;
    }

    public boolean isValidAction(Action action, Player player) {

        return (action != Action.RAISE || player.canRaise(bigBlind, highestBet))
                && (action != Action.CHECK || player == whoRaised)
                && (action != Action.FOLD || player != whoRaised)
                && (action != Action.CALL || player != whoRaised);
    }

    public void dealOneCardToPlayers() {
        for (int i = smallBlindIndex; i < players.size(); ++i) {
            players.get(i).addToHole(cards.pop());
            System.out.println("current player = " + players.get(i));
        }

        if (smallBlindIndex > 0) {
            for (int i = 0; i < smallBlindIndex; ++i) {
                players.get(i).addToHole(cards.pop());
                System.out.println("current player = " + players.get(i));
            }
        }
    }

    private void setHighestBet(int bet) {
        highestBet = bet;
    }

    private void setWhoRaised(Player player) {
        whoRaised = player;
    }

    private void setBigBlind() {
        Player player = players.get(bigBlindIndex);
        bigBlind = player.betBigBlind(smallBlind);
        player.setCurrentBet(bigBlind);
        setWhoRaised(player);
    }

    private void setSmallBlind() {
        Player player = players.get(smallBlindIndex);
        smallBlind = player.betSmallBlind();
        player.setCurrentBet(smallBlind);
    }

    private void shuffleCards() {
        cards.shuffle();
    }

    private void resetPlayersRole() {
        if (players.size() > 2) {
            buttonIndex = 0;
            smallBlindIndex = buttonIndex + 1;
            bigBlindIndex = smallBlindIndex + 1;
        } else if (players.size() == 2) {
            buttonIndex = 0;
            smallBlindIndex = buttonIndex;
            bigBlindIndex = smallBlindIndex + 1;
        } else if (players.size() == 1) {
            throw new UnsupportedOperationException();
        }
    }

    private ArrayList<Player> createPlayers() {
        int nPlayers = inputNumberOfPlayers();
        ArrayList<Player> result = new ArrayList<>();

        for (int i = 0; i < nPlayers; ++i) {
            result.add(createPlayer());
        }

        return result;
    }

    private Player createPlayer() {
        String name = this.inputPlayerName();
        output("Enter the number of Chips for " + name);
        int chips = this.inputPlayerChips();

        return new Player(name, chips);
    }

    private int inputPlayerChips() {
        return this.getIntInput();
    }

    private String inputPlayerName() {
        String name = "";

        while ("".equals(name)) {
            output("Enter a name");
            Scanner input = new Scanner(System.in);

            try {
                name = input.next();
            } catch (Exception e) {
                name = "";
            }
        }

        return name;
    }

    private int inputNumberOfPlayers() {
        output("Enter the number of players");
        Integer nPlayers = getIntInput();

        while (nPlayers < 2 || nPlayers > MAX_NUMBER_OF_PLAYERS) {
            output("Choose a number of players between maximal 2 and "
                    + MAX_NUMBER_OF_PLAYERS);
            nPlayers = getIntInput();
        }

        return nPlayers;
    }

    private void output(String message) {
        System.out.println(message);
    }

    private Integer getIntInput() {
        Integer entry = null;

        while (entry == null) {
            Scanner input = new Scanner(System.in);

            try {
                entry = input.nextInt();
            } catch (Exception e) {
                entry = null;
            }
        }

        return entry;
    }

    public static void main(String args[]) {
        Poker poker = new Poker();
        poker.start();
    }
}
