package scrabble;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class AlphabetBag {
	//ConcurrentHashmap to save word/meaning pairs for dictionary
		ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<String,Integer>();
		public AlphabetBag() {
			createMap();
			
		}
		
		/** create hashmap with 10 tiles per alpabhet**/
		private void createMap() {
			map.put("a",10);
			map.put("b",10);
			map.put("c",10);
			map.put("d",10);
			map.put("e",10);
			map.put("f",10);
			map.put("g",10);
			map.put("h",10);
			map.put("i",10);
			map.put("j",10);
			map.put("k",10);
			map.put("l",10);
			map.put("m",10);
			map.put("n",10);
			map.put("o",10);
			map.put("p",10);
			map.put("q",10);
			map.put("r",10);
			map.put("s",10);
			map.put("t",10);
			map.put("u",10);
			map.put("v",10);
			map.put("w",10);
			map.put("x",10);
			map.put("y",10);
			map.put("z",10);
		}
		
		public ArrayList<String> firstDraw() {
			ArrayList<String> hand = new ArrayList<String>();
			//find a random key from map
			List<String> keysAsArray = new ArrayList<String>(map.keySet());
			Random r = new Random();
			//get random key(get an alphabet tile basically)
	
			for(int i=0;i<7;i++) {
				String letter = keysAsArray.get(r.nextInt(keysAsArray.size()));
				int num = map.get(letter);
				map.put(letter, num-1);
				hand.add(letter);

			}

			return hand;
			
		}
		
		public String getLetter() {
			//find a random key from map
			List<String> keysAsArray = new ArrayList<String>(map.keySet());
			Random r = new Random();
			//get random key(get an alphabet tile basically)
			String letter = keysAsArray.get(r.nextInt(keysAsArray.size()));
			// if the random letter chosen has no more left, try again until a letter with some left is found
			//Nothing written yet about if no more letters(probably end game).
			if (map.get(letter) == 0) {
				System.out.println("No more of "+letter+" left!Finding another");
				letter = getLetter();
				
			}
			int num = map.get(letter);
			map.put(letter, num-1);
			System.out.println(map);
			return letter;
		}
}
