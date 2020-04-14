package ie.gmit.sw.ai.neural_network;

import org.encog.neural.networks.BasicNetwork;

/* Contains the loaded NN, will allow input to process categories */
/* Credit: https://github.com/jeffheaton/encog-java-examples/blob/master/src/main/java/org/encog/examples/neural/persist/EncogPersistence.java#L73 */
public class NeuralNetwork {

	public NeuralNetwork() {
	}

	public int Process(double[] input) {
		LoadNeuralNetwork NN = new LoadNeuralNetwork();
		BasicNetwork neuralNetwork = NN.Load();

		/* Bare bones array to store result otherwise NN gives out */
		double[] result = { 0, 0, 0, 0, 0, 0, 0, 0, };

		/* Get the most likely category */
		neuralNetwork.compute(input, result);

		/* Used to go through array and find best index */
		double biggest = 0;
		int highest = 0;

		/* Theres better ways to do this, gets best index */
		for (int i = 0; i < 8; i++) {
			if (result[i] > biggest) {
				biggest = result[i];
				highest = i;
			}
		}

		/* Return the best index, each index indicating a category */
		return highest;
	}
}
