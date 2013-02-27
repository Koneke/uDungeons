package lh.koneke.games.uDungeons;
import java.util.List;
import java.util.ArrayList;
public class Item extends Entity {
 public Item() {
  effects = new ArrayList<Effect>();
 }
 boolean used;
 public boolean getUsed() { return this.used; }
 public void setUsed(boolean b) { this.used = b; }
 String name;
 public String getName() { return this.name; }
 public void setName(String s) { this.name = s; }
 List<Effect> effects;
 public List<Effect> getEffects() { return this.effects; }
 public void setEffects(List<Effect> l) { this.effects = l; }
 public Item clone() {
  Item i = new Item();
  i.setUsed(getUsed());
  i.setName(getName());
  i.effects = new ArrayList<Effect>(getEffects());
  i.setTexture(getTexture());
  return i;
 }
}
