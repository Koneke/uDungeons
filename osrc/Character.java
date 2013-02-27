package lh.koneke.games.uDungeons;
import java.util.List;
import java.util.ArrayList;
public class Character extends Entity {
 public Character() {
  this.type = Type.CHARACTER;
  base = new CombatEffect(0,0,0,true);
  combatBonus = 0;
  this.effects = new ArrayList<Effect>();
  this.items = new ArrayList<Item>();
 }
 CombatEffect base;
 public CombatEffect getBase() { return this.base; }
 public void setBase(CombatEffect ce) { this.base = ce; }
 int combatBonus;
 public int getCombatBonus() { return this.combatBonus; }
 public void setCombatBonus(int i) { this.combatBonus = i; }
 public void increaseCombatBonus(int i) { this.combatBonus += i; }
 List<Effect> effects;
 public List<Effect> getEffects() { return this.effects; }
 public void setEffects(List<Effect> l) { this.effects = l; }
 List<Item> items;
 public List<Item> getItems() { return this.items; }
 public void setItems(List<Item> l) { this.items = l; }
}
