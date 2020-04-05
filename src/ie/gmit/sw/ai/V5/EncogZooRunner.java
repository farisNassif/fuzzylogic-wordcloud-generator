package ie.gmit.sw.ai.V5;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class EncogZooRunner {

	public static void main(String[] args) throws Exception {
		new EncogZooRunner().go();

	}

	public void go() throws Exception {
		// Step 1 declare a network topology
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, 16)); // in
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 3)); // hidden
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 7)); // out
		network.getStructure().finalizeStructure();
		network.reset();

		// Step 2: Create the Training Data Set
		MLDataSet trainingSet = new BasicMLDataSet(data, expected);

		// Step 3: Train the neural network
		ResilientPropagation train = new ResilientPropagation(network, trainingSet);
		double minError = 0.09; // Change and see the effect on the result... :)
		int epoch = 1;
		System.out.println("Info: Training");
		do {
			train.iteration();
			epoch++;
		} while (train.getError() > minError);
		train.finishTraining();
		System.out.println("Info: Training Finished in " + epoch + " epochs with error rate e = " + train.getError());

		// Step 4: Test the NN
		double correct = 0;
		double total = 0;
		for (MLDataPair pair : trainingSet) {
			total ++;
			MLData output = network.compute(pair.getInput());
			int y = (int) Math.round(output.getData(0));
			int yd = (int) pair.getIdeal().getData(0);
			if(y == yd) {
				correct ++;
			}	
		}
		System.out.println("Info: Testing complete, accuracy = " + ((correct / total) *100));
	}

	private double[][] data = { { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 0, 0, 1 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 }, { 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 0, 0, 1 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 }, { 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 1, 1 },
			{ 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0 }, { 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 0, 1, 0 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 },
			{ 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0.25, 1, 1, 0 }, { 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0.5, 0, 0, 0 },
			{ 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0.75, 0, 0, 0 },
			{ 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 },
			{ 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1 }, { 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1 },
			{ 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0.25, 1, 1, 0 },
			{ 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 },
			{ 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0.25, 1, 0, 1 },
			{ 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0.75, 0, 0, 0 }, { 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0.5, 0, 0, 0 },
			{ 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0.5, 0, 0, 0 }, { 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0.25, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.25, 0, 1, 1 },
			{ 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0.75, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 1, 1 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.25, 0, 0, 1 },
			{ 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 }, { 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 1, 0 }, { 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 0, 0 },
			{ 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 }, { 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0 },
			{ 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0.75, 0, 1, 0 },
			{ 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0.75, 0, 0, 0 },
			{ 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 },
			{ 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0.75, 0, 0, 0 },
			{ 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 },
			{ 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 }, { 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0.75, 0, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 }, { 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 },
			{ 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 },
			{ 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0.75, 0, 0, 0 }, { 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 0 },
			{ 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 }, { 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0.25, 1, 0, 1 },
			{ 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0.25, 1, 1, 0 },
			{ 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0.25, 1, 0, 1 },
			{ 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 }, { 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1 },
			{ 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0 },
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0.5, 1, 0, 1 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 1, 1 }, { 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1 },
			{ 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 1, 1 },
			{ 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 }, { 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 1, 1 },
			{ 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0.25, 1, 0, 1 }, { 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0 },
			{ 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0.25, 1, 0, 1 }, { 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0 }, { 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 },
			{ 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 }, { 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0 },
			{ 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.25, 1, 0, 0 },
			{ 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0.625, 0, 0, 0 }, { 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1 },
			{ 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0.25, 1, 0, 1 },
			{ 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0.75, 0, 0, 0 }, { 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0.5, 0, 0, 0 },
			{ 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0.5, 1, 0, 1 }, { 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 0 },
			{ 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1 }, { 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0.25, 1, 0, 0 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.5, 1, 0, 0 }, { 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0.25, 1, 0, 1 },
			{ 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0.25, 1, 0, 1 },
			{ 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0.75, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0.5, 1, 0, 1 },
			{ 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0.25, 1, 0, 0 } };

	private double[][] expected = { { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 1 }, { 0, 1, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 1, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 1, 0 }, { 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0 },
			{ 0, 1, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1 }, { 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 1, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0 }, { 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 1 }, { 0, 1, 0, 0, 0, 0, 0 } };
}
