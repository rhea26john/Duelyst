package structures.basic;

import java.util.List;
import java.util.ArrayList;
import utils.OrderedCardLoader;

import akka.actor.ActorRef;
import commands.BasicCommands;

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Player {

	private int health;
	private int mana;
	private int playerNumber;
	private int handSize = 6;
	private ArrayList<Card> hand = new ArrayList<Card>();
	private List<Card> deck = new ArrayList<Card>();
	private List<Card> createDeck(int playerNumber) {
		if (playerNumber == 1) {
			return OrderedCardLoader.getPlayer1Cards();
		}
		return OrderedCardLoader.getPlayer2Cards();
	}
	
	public Player() {
		super();
		this.health = 20;
		this.mana = 0;
		hand = null;
		deck = null;
	}
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
		hand = null;
		deck = null;
	}

	public Player(int health, int mana, int playerNumber) {
		this.health = health;
		this.mana = mana;
		this.playerNumber = playerNumber;
		this.hand = hand;
		this.deck = createDeck(playerNumber);
	}


	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public ArrayList<Card> getCardInhand() {
		return hand;
	}

	public void setgetCardInhand(ArrayList<Card> hand) {
		this.hand = hand;
	}


	//each time you can only draw one card, and show one card
	public void drawCard(ActorRef out){
		if(hand.size()<handSize){
			if (!deck.isEmpty()){
				hand.add(deck.get(0));
				deck.remove(0);
			}
		} else{
			deck.remove(0);
			if(playerNumber == 1){
				BasicCommands.addPlayer1Notification(out, "Hand Full, Card Lost", 3);
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}
}
