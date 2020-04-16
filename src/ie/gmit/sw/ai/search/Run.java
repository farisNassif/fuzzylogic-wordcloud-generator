package ie.gmit.sw.ai.search;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.categorical.Categorize;
import ie.gmit.sw.ai.cloud.WordFrequency;

/**
 * @deprecated: Old test runner before service handler was implemented
 */
public class Run {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int wcloudSize = 20;
		ExecutorService pool = Executors.newFixedThreadPool(5);
		WordcloudProcessor wordcloudProcessor = new WordcloudProcessor(new Wordcloud("software", wcloudSize), 2, 2, 3);

		CompletableFuture<WordFrequency[]> future = CompletableFuture.supplyAsync(() -> wordcloudProcessor.process(),
				pool);

		// future.get();

		for (WordFrequency word : future.get()) {
			System.out.println(word);
		}

		System.out.println(Categorize.getCategory());
	}
}
