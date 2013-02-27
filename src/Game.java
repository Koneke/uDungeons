package lh.koneke.games.uDungeons;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Version;
import java.util.List;

public class Game {
	public static void main(String argv[]) {
		Game g = new Game();
		g.start();
	}
	
	Room[][] map;	
	public static final int gridSize = 16;
	AtomController gamepad;
	GameState gameState;

	public void setup() {
		try {
			Display.setDisplayMode(new DisplayMode(640, 512));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 320, 256, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glClearColor(1,1,1,1);

		AtomController.setup();
		gamepad = AtomController.getFirst("Gamepad");
		//for(Component c : gamepad.getController().getComponents()) {
		//	System.out.println(c.getIdentifier());
		//}
	}
	
	public void init() {
		gameState = new GameState();
		gameState.setItems(new Item[6]);
		gameState.getItems()[0] = ItemPrototypes.get("Really Impressive Title");
	
		map = new Room[20][15];
		map[4][8] = new Room();	
		map[4][8].setExplored(true);
		map[5][8] = new Room();
		map[6][8] = new Room();
		map[6][7] = new Room();
		map[6][6] = new Room();
		map[5][6] = new Room();
		map[7][6] = new Room();
		map[8][6] = new Room();
		map[8][7] = new Room();

		gameState.setPlayer(new Character());
		gameState.getPlayer().setTexture(Graphics.getTexture("res/knight.png"));
		gameState.getPlayer().setxy(4, 8);
		gameState.getPlayer().setBase(new CombatEffect(1,0,0,true));
		gameState.getPlayer().getItems().add(ItemPrototypes.get("Really Impressive Title"));
		map[4][8].getEntities().add(gameState.getPlayer());
		
		Character goblin = new Character();
		goblin.setTexture(Graphics.getTexture("res/goblin.png"));
		goblin.setxy(6, 8);
		goblin.setBase(new CombatEffect(0,1,0,true));
		goblin.getItems().add(ItemPrototypes.get("Coward Boots"));
		map[6][8].getEntities().add(goblin);

		goblin = new Character();
		goblin.setTexture(Graphics.getTexture("res/goblin.png"));
		goblin.setxy(8, 7);
		goblin.setBase(new CombatEffect(0,1,3, true));
		map[8][7].getEntities().add(goblin);

		see();
	}	
	
	public void drawAt(int x, int y) {
		GL11.glTexCoord2f(0,0);GL11.glVertex2f(x*gridSize,			y*gridSize);
		GL11.glTexCoord2f(1,0);GL11.glVertex2f(x*gridSize+gridSize,	y*gridSize);
		GL11.glTexCoord2f(1,1);GL11.glVertex2f(x*gridSize+gridSize,	y*gridSize+gridSize);
		GL11.glTexCoord2f(0,1);GL11.glVertex2f(x*gridSize,			y*gridSize+gridSize);
	}
	public void drawRect(int x, int y, int w, int h, float scalar) {
		GL11.glTexCoord2f(0,0);GL11.glVertex2f(x*scalar,	y*scalar);
		GL11.glTexCoord2f(1,0);GL11.glVertex2f(w*scalar,	y*scalar);
		GL11.glTexCoord2f(1,1);GL11.glVertex2f(w*scalar,	h*scalar);
		GL11.glTexCoord2f(0,1);GL11.glVertex2f(x*scalar,	h*scalar);
	}

	public void draw() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 
		Graphics.getTexture("res/backgroundx.png").bind();
		Color.white.bind();
		GL11.glBegin(GL11.GL_QUADS);
		drawRect(0,0,512,256,1);
		GL11.glEnd();
	
		for(int x = 0; x < 20; x++) {
		for(int y = 0; y < 15; y++) {
			if(map[x][y] != null) {
				if(map[x][y].getSeen()) {
					map[x][y].getTexture().bind();
					(new Color(
						map[x][y].getExplored() ? 1f : 0.5f,
						map[x][y].getExplored() ? 1f : 0.5f,
						map[x][y].getExplored() ? 1f : 0.5f)).bind();
					GL11.glBegin(GL11.GL_QUADS);
					Graphics.getTexture("res/room.png").bind();
					drawAt(x, y);
					GL11.glEnd();
				}
			}
		}}
		for(int x = 0; x < 20; x++) {
		for(int y = 0; y < 15; y++) {
			if(map[x][y] != null) {
				if(map[x][y].getExplored()) {
					for(Entity e : map[x][y].getEntities()) {
						e.getTexture().bind();
						(new Color(
							map[x][y].getExplored() ? 1f : 0.5f,
							map[x][y].getExplored() ? 1f : 0.5f,
							map[x][y].getExplored() ? 1f : 0.5f)).bind();
						GL11.glBegin(GL11.GL_QUADS);
						drawAt(x,y);
						GL11.glEnd();
					}
				}
			}
		}}

		if(gameState.getEnemy() != null) {
			//draw combat ui
			updateEffects();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

			Color.red.bind();
			int playerpower = //gameState.getPlayer().getBase().getPlayerBonus()+
				gameState.getPlayer().getCombatBonus();
			int enemypower = //gameState.getEnemy().getBase().getEnemyBonus()+
				gameState.getEnemy().getCombatBonus();
			//TODO: devcode, use actual power instead
			Graphics.getTexture("res/numbers/"+(playerpower)+".png").bind();
			GL11.glBegin(GL11.GL_QUADS);
				drawRect(1,1,6,6,gridSize);
			GL11.glEnd();
			Graphics.getTexture("res/numbers/"+(enemypower)+".png").bind();
			GL11.glBegin(GL11.GL_QUADS);
				drawRect(14,1,19,6,gridSize);
			GL11.glEnd();
	
			for(int i = 0; i < 6; i++) {
				Item item = null;
				if(i < gameState.getPlayer().getItems().size()) {
					item = gameState.getPlayer().getItems().get(i);
					(item.used ? Color.green : Color.blue).bind();
				} else {
					Color.black.bind();
				}

				int x = 1+3*i;
				if(i > 2) { x += 1; }
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
				GL11.glBegin(GL11.GL_QUADS);
					drawRect(x,13,x+2,15,gridSize);
				GL11.glEnd();

				Color.black.bind();
				if(item != null) {
					item.getTexture().bind();
					Color.white.bind();
				} else {
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
				}
				GL11.glBegin(GL11.GL_QUADS);
					drawRect(x,13,x+2,15,gridSize);
				GL11.glEnd();
			}
			
			Graphics.getTexture("res/arrow.png").bind();
			Color.white.bind();
			
			int ax = 2+6*gameState.getSelector();
			if(gameState.getSelector() > 2) { ax += 2; }
			GL11.glBegin(GL11.GL_QUADS);
				drawRect(ax,22,ax+4,26,gridSize/2);
			GL11.glEnd();	
		}
		Display.update();
	}

	public void see() {
		int px = gameState.getPlayer().getx();
		int py = gameState.getPlayer().gety();
		for(int x = -1;x<2;x++) {
			if(px+x > 0 && px+x < 20) {
				if(map[px+x][py] != null) {
					map[px+x][py].setSeen(true); }}}
		for(int y = -1;y<2;y++) {
			if(px+y > 0 && py+y < 20) {
				if(map[px][py+y] != null) {
					map[px][py+y].setSeen(true); }}}
	}

	public void move(int x, int y) {
		int nx = gameState.getPlayer().getx()+x;
		int ny = gameState.getPlayer().gety()+y;
		if(nx < 0 || nx > 19 || ny < 0 || ny > 14) {
			//TODO: can't move that way
			return;
		}
		if(map[nx][ny] != null) {
			boolean domove = true;
			map[nx][ny].setExplored(true);
			for(Entity e : map[nx][ny].getEntities()) {
				if(e instanceof Character) {
					domove = false;	
					gameState.setEnemy((Character)e);
				}
			}
			if(domove) {
				map[gameState.getPlayer().getx()][gameState.getPlayer().gety()]
					.getEntities().remove(gameState.getPlayer());
				map[nx][ny].getEntities().add(gameState.getPlayer());
				gameState.getPlayer().setxy(nx, ny);
				see();
			}
			return;
		}
		
	}
	
	public void update() {
		gamepad.update();

		if(gameState.getEnemy() == null) {
			if(gamepad.getValue("pov") != gamepad.getLast("pov")) {
				int pov = (int)(8*gamepad.getValue("pov"));
				switch(pov) {
					case 8: move(-1, 0); break;
					case 6: move(0, 1);  break;
					case 4: move(1, 0);  break;
					case 2: move(0, -1); break;
					default: break;
				}	
			}
			if(gamepad.getValue("A") == 1 ? gamepad.getLast("A") < 1 : false) {
				//act on current tile
				int playerx = gameState.getPlayer().getx();
				int playery = gameState.getPlayer().gety();
				List<Entity> entitiesCopy = map[playerx][playery].getEntities();
				for(Entity e : map[playerx][playery].getEntities()) {
					if(e instanceof Item) {
						gameState.getPlayer().getItems().add((Item)e);
						entitiesCopy.remove(e);
					}
				}
				map[playerx][playery].setEntities(entitiesCopy);
			}
		} else {
			if(gamepad.getValue("pov") != gamepad.getLast("pov")) {
				int pov = (int)(8*gamepad.getValue("pov"));
				switch(pov) {
					case 8: gameState.setSelector(Math.abs((gameState.getSelector() - 1) %
						gameState.getPlayer().getItems().size())); break;
					case 4: gameState.setSelector(Math.abs((gameState.getSelector() + 1) %
						gameState.getPlayer().getItems().size())); break;
					default: break;
				}
			}
			if(gamepad.getValue("A") == 1 ? gamepad.getLast("A") < 1 : false) {
				if(gameState.getSelector() < gameState.getPlayer().getItems().size()) {
					gameState.getPlayer().getItems().get(gameState.getSelector())
						.setUsed(!gameState.getPlayer().getItems().get(gameState.getSelector()).getUsed());
				}
			}

			if(gamepad.getValue("B") == 1 ? gamepad.getLast("B") < 1 : false) {
				updateEffects();

				int playerLevel =
					//gameState.getPlayer().getBase().getPlayerBonus()+
					gameState.getPlayer().getCombatBonus();
				int enemyLevel =
					//gameState.getEnemy().getBase().getEnemyBonus()+
					gameState.getEnemy().getCombatBonus();

				if(playerLevel <= enemyLevel) {
					System.out.println("Player died");
				} else {
					System.out.println("Enemy died");
					int enemyx = gameState.getEnemy().getx();
					int enemyy = gameState.getEnemy().gety();
					map[enemyx][enemyy]
						.getEntities().remove(gameState.getEnemy());
					for(Item i : gameState.getEnemy().getItems()) {
						map[enemyx][enemyy].getEntities().add(i);
					}
					
					gameState.setEnemy(null);
				}
			}

			if(gamepad.getValue("X") == 1 ? gamepad.getLast("X") < 1 : false) {
				//run away
				updateEffects();

				int roll = 1+(int)(Math.random() * 6);
				roll += gameState.getRunModifier();
				if(roll >= 5) {
					System.out.println("Player ran away. ("+roll+")");
					gameState.setEnemy(null);
				} else {
					System.out.println("Player tried to run away... ("+roll+")");
					System.out.println("Player died");
				}
			}
		}

		gamepad.postupdate();
	}

	public void updateEffects() {
		gameState.getPlayer().setCombatBonus(0);
		gameState.getEnemy().setCombatBonus(0);
		gameState.setRunModifier(0);

		for(Item i : gameState.getPlayer().getItems()) {
			if(i.getUsed()) {
				for(Effect e : i.getEffects()) { e.affect(gameState); } } }

		for(Effect e : gameState.getPlayer().getEffects()) { e.affect(gameState); }
		for(Effect e : gameState.getEnemy().getEffects()) { e.affect(gameState); }
		gameState.getPlayer().getBase().affect(gameState);
		gameState.getEnemy().getBase().affect(gameState);
	}

	public void close() {
		Display.destroy();
	}

	public void start() {
		setup();
		init();
		while(!Display.isCloseRequested()) {
			update();
			draw();
		}		
		close();
	}
}
