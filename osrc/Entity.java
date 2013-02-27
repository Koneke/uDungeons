package lh.koneke.games.uDungeons;
import org.newdawn.slick.opengl.Texture;
public class Entity {
 public static enum Type {
  GENERIC, CHARACTER
 };
 public Entity() {
  this.type = Type.GENERIC;
 }
 Texture texture;
 public Texture getTexture() { return this.texture; }
 public void setTexture(Texture texture) { this.texture = texture; }
 String name;
 public String getName() { return this.name; }
 public void setName(String s) { this.name = s; }
 Type type;
 public Type getType() { return this.type; }
 int x;
 public int getx() { return this.x; }
 public void setx(int y) { this.x = x; }
 int y;
 public int gety() { return this.y; }
 public void sety(int y) { this.y = y; }
 public void setxy(int x, int y) { this.x = x; this.y = y; }
}
