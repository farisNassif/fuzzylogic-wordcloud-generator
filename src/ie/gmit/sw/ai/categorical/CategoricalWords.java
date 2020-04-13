package ie.gmit.sw.ai.categorical;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class CategoricalWords {

	/* Category 1: Location */
	protected static Set<String> LocationWords() {
		Set<String> location_words = Collections.synchronizedSortedSet(new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
		location_words.addAll(Arrays.asList("location", "area", "place", "longitude", "history"));

		return location_words;
	}

	/* Category 2: Food */
	protected static Set<String> FoodWords() {
		Set<String> food_words = Collections.synchronizedSortedSet(new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
		food_words.addAll(Arrays.asList("food", "health", "cook", "nutrition", "recipe"));

		return food_words;
	}

	/* Category 3: Person */
	protected static Set<String> PersonWords() {
		Set<String> person_words = Collections.synchronizedSortedSet(new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
		person_words.addAll(Arrays.asList("person", "human", "self", "identity"));

		return person_words;
	}

	/* Category 4: Technology */
	protected static Set<String> TechnologyWords() {
		Set<String> technology_words = Collections.synchronizedSortedSet(new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
		technology_words.addAll(Arrays.asList("technology", "engineer", "science", "computer"));

		return technology_words;
	}

	/* Category 5: Movie */
	protected static Set<String> MovieWords() {
		Set<String> movie_words = Collections.synchronizedSortedSet(new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
		movie_words.addAll(Arrays.asList("movie", "marvel", "series", "film"));

		return movie_words;
	}

	/* Category 6: Book */
	protected static Set<String> BookWords() {
		Set<String> book_words = Collections.synchronizedSortedSet(new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
		book_words.addAll(Arrays.asList("book", "paper", "print", "library"));

		return book_words;
	}

	/* Category 7: Animal */
	protected static Set<String> AnimalWords() {
		Set<String> animal_words = Collections.synchronizedSortedSet(new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
		animal_words.addAll(Arrays.asList("animal", "planet", "species", "biology"));

		return animal_words;
	}

	/* Category 8: Music */
	protected static Set<String> MusicWords() {
		Set<String> music_words = Collections.synchronizedSortedSet(new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
		music_words.addAll(Arrays.asList("music", "album", "jazz", "rock"));

		return music_words;
	}

}
