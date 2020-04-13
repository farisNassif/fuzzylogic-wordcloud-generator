package encog;

import encog.activator.Activator;

public class ZooRunner {
	public ZooRunner() throws Exception {
		NeuralNetwork nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 24, 6, 8);
		BackpropagationTrainer trainer = new BackpropagationTrainer(nn);
		trainer.train(data, expected, 0.05, 8000);
		double[] test = { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 };
		double[] result = nn.process(test);

		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
		System.out.println(Utils.getMaxIndex(result) + 1);
	}

	public static void main(String[] args) throws Exception {
		new ZooRunner();
	}
	
	/*
	 * 
     *    Set of categories:
     *    =====================================================================
   	 *    1. Location        Boolean
     *    2. Food            Boolean
     *    3. Person          Boolean
     *    4. Technology      Boolean
     *    5. Movie           Boolean
     *    6. Book            Boolean
     *    7. Animal          Boolean
     *    8. Music 			 Boolean
	 * 
	 * The user's query word will be classified into one of the following categories
	 * The three words below each category are the words most associated with that category
	 * If one of those three words are located within a relevant url's contents, it would suggest 
	 * the query word would be related to the corresponding category
	 * 
	 *    		** Location ** 
	 *    1. Longitude		Boolean
	 *    2. Area     		Boolean
	 *    3. Place			Boolean
	 *    		** Food ** 
	 *    4. Health 		Boolean
	 *    5. Cooking     	Boolean
	 *    6. Nutrition		Boolean
	 *    		** Person ** 
	 *    7. Human			Boolean
	 *    8. Identity   	Boolean
	 *    9. Self			Boolean
	 *    		** Technology ** 
	 *   10. Science		Boolean
	 *   11. Engineeiring   Boolean
	 *   12. History		Boolean
	 *    		** Movie ** 
	 *   13. Watch 			Boolean
	 *   14. Marvel    		Boolean
	 *   15. Show			Boolean
	 *    		** Book ** 
	 *   16. Paper			Boolean
	 *   17. Print     		Boolean
	 *   18. Library		Boolean
	 *    		** Animal ** 
	 *   19. Planet			Boolean
	 *   20. Species    	Boolean
	 *   21. Biology		Boolean
	 *    	    ** Music ** 
	 *   22. Album			Boolean
	 *   23. Rock     		Boolean
	 *   24. Jazz			Boolean
	 */
	
	private double[][] data = { { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 } };

	private double[][] expected = { { 1, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1 } };
}