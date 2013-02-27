package lh.koneke.games.uDungeons;

public class GameState {
	Character player;
	public Character getPlayer() { return this.player; }
	public void setPlayer(Character c) { this.player = c; }

	Character enemy;
	public Character getEnemy() { return this.enemy; }
	public void setEnemy(Character c) { this.enemy = c; }

	Item[] items;
	public Item[] getItems() { return this.items; }	
	public void setItems(Item[] i) { this.items = i; }

	int itemSelector;
	public int getSelector() { return this.itemSelector; }
	public void setSelector(int i) { this.itemSelector = i; }

	int runModifier;
	public int getRunModifier() { return this.runModifier; }
	public void setRunModifier(int i) { this.runModifier = i; }
	public void increaseRunModifier(int i) { this.runModifier += i; }
}
