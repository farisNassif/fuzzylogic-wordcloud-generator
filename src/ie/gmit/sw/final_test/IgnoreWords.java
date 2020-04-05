package ie.gmit.sw.final_test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
}
