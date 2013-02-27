package lh.koneke.games.uDungeons;

public class CombatEffect implements Effect {
	int playerBonus;
	int enemyBonus;
	int runModifier;
	boolean solid; //can't be removed
	
	public CombatEffect(int p, int e, int r, boolean s) {
		this.playerBonus = p;
		this.enemyBonus = e;
		this.runModifier = r;
		this.solid = s;
	}

	public void affect(GameState gameState) {
		gameState.getPlayer().increaseCombatBonus(playerBonus);
		gameState.getEnemy().increaseCombatBonus(enemyBonus);
		gameState.increaseRunModifier(runModifier);
	}

	public int getPlayerBonus() { return this.playerBonus; }
	public int getEnemyBonus() { return this.enemyBonus; }
	public int getRunModifier() { return this.runModifier; }
	public boolean getSolid() { return this.solid; }
}
