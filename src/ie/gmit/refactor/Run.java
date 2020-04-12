package ie.gmit.refactor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.cloud.WordFrequency;

public class Run {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int wcloudSize = 20;
		ExecutorService pool = Executors.newFixedThreadPool(5);
		WordcloudProcessor wordcloudProcessor = new WordcloudProcessor(new Wordcloud("software", wcloudSize), 1, 3, 3);

		CompletableFuture<WordFrequency[]> future = CompletableFuture
				.supplyAsync(() -> wordcloudProcessor.process(), pool);

		System.out.println(future.get().length);
	}
}
