package ie.gmit.gh;

import java.io.File;

import org.encog.Encog;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;
import org.encog.util.simple.EncogUtility;

public class EncogIrisRunner {

	public static void main(String[] args) throws Exception {
		new EncogIrisRunner().go("./iris.txt");

	}

	public void go(String file) throws Exception {
		// Step 1: Prepare the Data for the Neural Network
		VersatileDataSource source = new CSVDataSource(new File(file), false, CSVFormat.DECIMAL_POINT);
		VersatileMLDataSet data = new VersatileMLDataSet(source);
		data.defineSourceColumn("sepal-length", 0, ColumnType.continuous);
		data.defineSourceColumn("sepal-width", 1, ColumnType.continuous);
		data.defineSourceColumn("petal-length", 2, ColumnType.continuous);
		data.defineSourceColumn("petal-width", 3, ColumnType.continuous);

		ColumnDefinition out = data.defineSourceColumn("species", 4, ColumnType.nominal);
		data.analyze();
		data.defineSingleOutputOthersInput(out);

		// Step 2: Create a Machine Learning Model
		EncogModel model = new EncogModel(data);
		model.selectMethod(data, MLMethodFactory.TYPE_SVM);
		data.normalize();

		// Step 3: Train the Model
		model.holdBackValidation(0.3, true, 1001); // 30% used for testing, rest training
		model.selectTrainingType(data);
		MLRegression bestMethod = (MLRegression) model.crossvalidate(10, true);
		System.out.println(
				"Training error: " + EncogUtility.calculateRegressionError(bestMethod, model.getTrainingDataset()));
		System.out.println(
				"Validation error: " + EncogUtility.calculateRegressionError(bestMethod, model.getValidationDataset()));

		// Step 4: Test the Model
		NormalizationHelper helper = data.getNormHelper();
		ReadCSV csv = new ReadCSV(new File(file), false, CSVFormat.DECIMAL_POINT);
		String[] line = new String[4];
		MLData input = helper.allocateInputVector();
		while (csv.next()) {
			line[0] = csv.get(0);
			line[1] = csv.get(1);
			line[2] = csv.get(2);
			line[3] = csv.get(3);
			String expected = csv.get(4); // The expected result is the last column on each row
			helper.normalizeInputVector(line, input.getData(), false);
			MLData output = bestMethod.compute(input); // Ask the network to classify the input
			String actual = helper.denormalizeOutputVectorToString(output)[0]; // The actual result…
			System.out.println("Expected: " + actual + " Actual: " + expected);
		}
		
		// Step 5: Shutdown the Model
		Encog.getInstance().shutdown();
	}

}
