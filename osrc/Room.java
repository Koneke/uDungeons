package lh.koneke.games.uDungeons;
import org.newdawn.slick.opengl.Texture;
import java.util.List;
import java.util.ArrayList;
public class Room {
 public Room() {
  this.entities = new ArrayList<Entity>();
  this.texture = Graphics.getTexture("res/room.png");
 }
 Texture texture;
 public Texture getTexture() { return this.texture; }
 public void setTexture(Texture texture) { this.texture = texture; }
 pint position;
 boolean explored;
 public boolean getExplored() { return explored; }
 public void setExplored(boolean b) { this.explored = b; }
 boolean seen;
 public boolean getSeen() { return seen; }
 public void setSeen(boolean b) { this.seen = b; }
 List<Entity> entities;
 public List<Entity> getEntities() { return this.entities; }
 public void setEntities(List<Entity> l) { this.entities = l; }
}
