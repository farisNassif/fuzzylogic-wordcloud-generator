package test.again;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Run {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService pool = Executors.newFixedThreadPool(5);
		WordcloudProcessor wordcloudProcessor = new WordcloudProcessor(new Wordcloud("gmit", 3, 3), 1);

		CompletableFuture<Map<String, Integer>> future = CompletableFuture
				.supplyAsync(() -> wordcloudProcessor.process(), pool);

		System.out.println(future.get());
	}
}
