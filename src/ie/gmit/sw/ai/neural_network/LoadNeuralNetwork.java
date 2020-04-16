package ie.gmit.sw.ai.neural_network;

import java.io.File;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import ie.gmit.sw.ServiceHandler;

/* Class that just loads and returns the saved NN */
/* Credit: https://github.com/jeffheaton/encog-java-examples/blob/master/src/main/java/org/encog/examples/neural/persist/EncogPersistence.java#L73 */
public class LoadNeuralNetwork {

	public LoadNeuralNetwork() {
		super();
	}

	/* Returns the loaded NN */
	protected BasicNetwork Load() {
		// String filename = "res/NeuralNetwork";

		BasicNetwork loadFromFileNetwork = (BasicNetwork) EncogDirectoryPersistence
				//.loadObject(new File(ServiceHandler.NeuralNetwork.getAbsolutePath()));
				.loadObject(new File("res/NeuralNetwork"));
		return loadFromFileNetwork;
	}
}
