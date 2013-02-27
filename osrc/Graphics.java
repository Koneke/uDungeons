package lh.koneke.games.uDungeons;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import java.util.HashMap;
import java.io.IOException;
public class Graphics {
 static HashMap<String, Texture> textures = new HashMap<String, Texture>();
 public static Texture getTexture(String path) {
  if(!textures.keySet().contains(path)) {
   try {
    Texture texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    textures.put(path, texture);
    return texture;
   } catch (IOException e) {
    e.printStackTrace();
    System.exit(0);
    return null;
   }
  } else {
   return textures.get(path);
  }
 }
}
