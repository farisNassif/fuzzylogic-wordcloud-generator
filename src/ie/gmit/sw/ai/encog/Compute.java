package ie.gmit.sw.ai.encog;

import org.encog.neural.networks.BasicNetwork;

public class Compute {
	public int Compute(double[] input) {
		/* Bare array to store res otherwise NN gives out */
		double[] result = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		BasicNetwork NN = Load();

		/* Get the most likely category */
		NN.compute(input, result);

		/* Go through array and find best index */
		double biggest = 0;
		int highest = 0;

		/* Theres better ways to do this */
		for (int i = 0; i < 8; i++) {
			System.out.println(result[i]);
			if (result[i] > biggest) {
				biggest = result[i];
				highest = i;
			}
		}

		/* Return the best index, each index indicating a category */
		System.out.println(highest);

		return highest;
	}
}
