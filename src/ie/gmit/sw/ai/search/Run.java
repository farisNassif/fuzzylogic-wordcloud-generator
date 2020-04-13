package ie.gmit.sw.ai.search;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.neural_network.NeuralNetwork;

public class Run {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int wcloudSize = 20;
		double[] result = { 0.1, 0.6, 0, 0.3, 0, 0, 0, 0};
		NeuralNetwork n = new NeuralNetwork();
		
		n.Process(result);
		
		ExecutorService pool = Executors.newFixedThreadPool(5);
		WordcloudProcessor wordcloudProcessor = new WordcloudProcessor(new Wordcloud("pizza", wcloudSize), 1, 4, 3);

		CompletableFuture<WordFrequency[]> future = CompletableFuture.supplyAsync(() -> wordcloudProcessor.process(),
				pool);

		for (WordFrequency word : future.get()) {
			System.out.println(word);
		}
	}
}
