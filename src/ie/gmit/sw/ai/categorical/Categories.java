package ie.gmit.sw.ai.categorical;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Categories {
	Location, Food, Person, Technology, TV_or_Movie, Book, Animal, Music;

	public static Map<Integer, String> CategoryMap() {
		Map<Integer, String> categorical_map = new ConcurrentHashMap<Integer, String>() {
			private static final long serialVersionUID = 1L;

			{
				put(0, Categories.Location.name());
				put(1, Categories.Food.name());
				put(2, Categories.Person.name());
				put(3, Categories.Technology.name());
				put(4, Categories.TV_or_Movie.name());
				put(5, Categories.Book.name());
				put(6, Categories.Animal.name());
				put(7, Categories.Music.name());
			}
		};
		return categorical_map;
	}
}
