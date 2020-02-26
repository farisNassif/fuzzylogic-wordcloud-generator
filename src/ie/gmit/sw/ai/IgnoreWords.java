package ie.gmit.sw.ai;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class IgnoreWords {
	public static Collection<String> ignoreWords() throws IOException {
		Collection<String> ignoreWords = new ArrayList<String>();

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
