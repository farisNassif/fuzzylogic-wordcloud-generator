package ie.gmit.sw.ai.categorical;

import java.util.Map;

import ie.gmit.sw.ai.neural_network.NeuralNetwork;
import ie.gmit.sw.ai.util.MapSort;

public class Categorize {
	public static String category;

	public static void findCategory(Map<String, Integer> frequencies) {

		/* Used to normalize data before entry into NN */
		final int NORMALIZER = 3;
		/* Frequency values returned from search */
		Map<String, Integer> sort_freq = MapSort.crunchifySortMap(frequencies);
		/* For use of local NN */
		NeuralNetwork NN = new NeuralNetwork();

		/* Eight categorical words */
		double location = 0, food = 0, person = 0, technology = 0, movie = 0, book = 0, animal = 0, music = 0;

		/* If any of the categorical keywords are found, increment */
		for (String word : sort_freq.keySet()) {
			/* Will be incremented by 1 * frequency of that keyword */
			if (CategoricalWords.LocationWords().contains(word)) {
				location += sort_freq.get(word);
			} else if (CategoricalWords.FoodWords().contains(word)) {
				food += sort_freq.get(word);
			} else if (CategoricalWords.PersonWords().contains(word)) {
				person += sort_freq.get(word);
			} else if (CategoricalWords.TechnologyWords().contains(word)) {
				technology += sort_freq.get(word);
			} else if (CategoricalWords.MovieWords().contains(word)) {
				movie += sort_freq.get(word);
			} else if (CategoricalWords.BookWords().contains(word)) {
				book += sort_freq.get(word);
			} else if (CategoricalWords.AnimalWords().contains(word)) {
				animal += sort_freq.get(word);
			} else if (CategoricalWords.MusicWords().contains(word)) {
				music += sort_freq.get(word);
			}
		}

		/* Final total, needed to squish down individual values for NN */
		double total = location + food + person + technology + movie + book + animal + music;

		/* Preparing squished data for neural network */
		double[] result = { (scale(location, 1, total) * NORMALIZER), (scale(food, 1, total) * NORMALIZER),
				(scale(person, 1, total) * NORMALIZER), (scale(technology, 1, total) * NORMALIZER),
				(scale(movie, 1, total) * NORMALIZER), (scale(book, 1, total) * NORMALIZER),
				(scale(animal, 1, total) * NORMALIZER), (scale(music, 1, total) * NORMALIZER) };

		// System.out.println(Categories.CategoryMap().get(NN.Process(result)));
		/* Get the category with the result returned from NN */
		category = Categories.CategoryMap().get(NN.Process(result));
	}

	/* Will scale all category results so they represent a value between 0 - 1 */
	private static double scale(double category, double minVal, double categoricalMax) {
		return (((category - minVal) / (categoricalMax - minVal)) * 1.1);
	}
}
