package ie.gmit.sw.ai.search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ie.gmit.sw.ServiceHandler;

public class IgnoreWords {
	public static Collection<String> ignoreWords() throws IOException {
		Set<String> ignoreWords = Collections.synchronizedSet(new HashSet<String>());

		try {
			// BufferedReader in = new BufferedReader(new
			// FileReader(ServiceHandler.f.getAbsolutePath()));
			BufferedReader in = new BufferedReader(new FileReader("res/ignorewords.txt"));

			String line;
			while ((line = in.readLine()) != null) {
				ignoreWords.add(line);
			}
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ignoreWords;
	}

	/* When generating freq table, need to ignore actual query word + plural */
	public static void ignoreQuery(String query) {
		// String filename = ServiceHandler.f.getAbsolutePath();
		String filename = "res/ignorewords.txt";
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename, true);
			fw.write("\n" + query);
			fw.write("\n" + query + "s");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
