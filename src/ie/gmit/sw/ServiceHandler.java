package ie.gmit.sw;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.cloud.LogarithmicSpiralPlacer;
import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.search.Wordcloud;
import ie.gmit.sw.ai.search.WordcloudProcessor;

public class ServiceHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static File f;
	public static File fuz;
	private String ignoreWords = null;
	private String fuzzyFcl = null;

	public void init() throws ServletException {
		ServletContext ctx = getServletContext();

		// Reads the value from the <context-param> in web.xml
		ignoreWords = getServletContext().getRealPath(File.separator)
				+ ctx.getInitParameter("IGNORE_WORDS_FILE_LOCATION");
		fuzzyFcl = getServletContext().getRealPath(File.separator) + ctx.getInitParameter("FUZZY_FILE_LOCATION");

		fuz = new File(fuzzyFcl);
		f = new File(ignoreWords);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		/* Exercutor service thread pool */
		ExecutorService pool = Executors.newFixedThreadPool(4);

		/* Retrieve the user options */
		String option = req.getParameter("cmbOptions");
		String query = req.getParameter("query");

		out.print("<html><head><title>Artificial Intelligence Assignment</title>");
		out.print("<link rel=\"stylesheet\" href=\"includes/style.css\">");

		out.print("</head>");
		out.print("<body>");
		out.print(
				"<div style=\"font-size:48pt; font-family:arial; color:#990000; font-weight:bold\">Web Opinion Visualiser</div>");

		out.print("<p>The &quot;ignore words&quot; file is located at <font color=red><b>" + f.getAbsolutePath()
				+ "</b></font> and is <b><u>" + f.length() + "</u></b> bytes in size.");
		out.print("<p>The &quot;fuzzy&quot; file is located at <font color=red><b>" + fuz.getAbsolutePath()
				+ "</b></font> and is <b><u>" + fuz.length() + "</u></b> bytes in size.");
		out.print("<p><fieldset><legend><h3>Result</h3></legend>");

		/* Process the wordcloud */
		WordcloudProcessor wordcloudProcessor = new WordcloudProcessor(new Wordcloud(query, 3, 3), 1);

		/* Declare future */
		CompletableFuture<WordFrequency[]> future = CompletableFuture.supplyAsync(() -> wordcloudProcessor.process(),
				pool);

		/* Return the top 32 words from future call */
		WordFrequency[] words = null;
		try {
			words = future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		/* Spira Mirabilis */
		LogarithmicSpiralPlacer placer = new LogarithmicSpiralPlacer(800, 600);
		
		for (WordFrequency word : words) {
			/* Place each word on the canvas starting with the largest */
			placer.place(word);
		}

		/* Get a handle on the word cloud graphic */
		BufferedImage cloud = placer.getImage();
		out.print("<img src=\"data:image/png;base64," + encodeToString(cloud) + "\" alt=\"Word Cloud\">");

		out.print("</fieldset>");
		/* Print some stats */
		out.print("<p>Generating word cloud from query: <b>" + query + "</b></p>");
		out.print("<p>Search Algorithm: <b>" + option + "</b></p>");
		out.print("<a href=\"./\">Return to Start Page</a>");
		out.print("</body>");
		out.print("</html>");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	private String encodeToString(BufferedImage image) {
		String s = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, "png", bos);
			byte[] bytes = bos.toByteArray();

			Base64.Encoder encoder = Base64.getEncoder();
			s = encoder.encodeToString(bytes);
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}