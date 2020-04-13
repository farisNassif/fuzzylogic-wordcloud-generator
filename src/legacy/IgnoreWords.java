package legacy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ie.gmit.sw.ServiceHandler;

public class IgnoreWords {
	public static Collection<String> ignoreWords() throws IOException {
		Set<String> ignoreWords = new HashSet<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader("ignorewords.txt"));

			String line;
			while ((line = in.readLine()) != null) {
				ignoreWords.add(line);
			}
			in.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ignoreWords;
	}

	/* When generating freq table, need to ignore actual query word + plural */
	public static void ignoreQuery(String query) {
		String filename = "ignorewords.txt";
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
