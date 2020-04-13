package ie.gmit.sw.ai.neural_network;

import java.io.File;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

/* Class that just loads and returns the saved NN */
public class LoadNeuralNetwork {

	public LoadNeuralNetwork() {
		super();
	}

	/* Returns the loaded NN */
	protected BasicNetwork Load() {
		String filename = "res/NeuralNetwork";

		BasicNetwork loadFromFileNetwork = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(filename));

		return loadFromFileNetwork;
	}

}
