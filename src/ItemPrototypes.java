package lh.koneke.games.uDungeons;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ItemPrototypes {
	static HashMap<String, Item> items;
	public static Item get(String name) { return items.get(name).clone(); }
	
	static {
		items = new HashMap<String, Item>();
		load();
	}

	static void load() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("res/items")); 
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";");
				Item newItem = new Item();

				newItem.setName(parts[0]);
				newItem.setTexture(Graphics.getTexture(parts[1]));

				int i = 2;
				switch(parts[i]) {
					case "ce":
						newItem.getEffects().add(new CombatEffect(
							Integer.parseInt(parts[i+1]),
							Integer.parseInt(parts[i+2]),
							Integer.parseInt(parts[i+3]),
							parts[i+4].equals("true") ? true : false));
					break;
					default: break;
				}

				items.put(newItem.getName(), newItem);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
}
