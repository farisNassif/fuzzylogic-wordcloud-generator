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

import ie.gmit.sw.ai.categorical.Categorize;
import ie.gmit.sw.ai.cloud.LogarithmicSpiralPlacer;
import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.search.Wordcloud;
import ie.gmit.sw.ai.search.WordcloudProcessor;
import ie.gmit.sw.ai.util.Stopwatch;

public class ServiceHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static File IgnoreWords;
	public static File FuzzyBFSFcl;
	public static File FuzzyBeamFcl;
	public static File NeuralNetwork;

	private String Ignore = null;
	private String FuzzyBFS = null;
	private String FuzzyBeam = null;
	private String NN = null;

	public void init() throws ServletException {
		ServletContext ctx = getServletContext();

		// Reads the value from the <context-param> in web.xml
		Ignore = getServletContext().getRealPath(File.separator) + ctx.getInitParameter("IGNORE_WORDS_FILE_LOCATION");
		FuzzyBFS = getServletContext().getRealPath(File.separator) + ctx.getInitParameter("BFS_FUZZY_FILE_LOCATION");
		FuzzyBeam = getServletContext().getRealPath(File.separator) + ctx.getInitParameter("BEAM_FUZZY_FILE_LOCATION");
		NN = getServletContext().getRealPath(File.separator) + ctx.getInitParameter("NEURAL_NETWORK_LOCATION");

		FuzzyBFSFcl = new File(FuzzyBFS);
		IgnoreWords = new File(Ignore);
		FuzzyBeamFcl = new File(FuzzyBeam);
		NeuralNetwork = new File(NN);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Stopwatch stopwatch = new Stopwatch();
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		/* Exercutor service thread pool */
		ExecutorService pool = Executors.newFixedThreadPool(4);

		/* Retrieve the user options */
		String searchOption = req.getParameter("searchType");
		String query = req.getParameter("query");
		/* Just converting from string to int to get value can work with */
		int maxWords = Integer.valueOf(req.getParameter("maxWords"));
		int branchingFactor = Integer.valueOf(
				req.getParameter("branchingFactor").substring(req.getParameter("branchingFactor").length() - 1));
		int maxDepth = Integer
				.valueOf(req.getParameter("maxDepth").substring(req.getParameter("maxDepth").length() - 1));
		int searchType = 1; // Default

		out.print("<html><head><title>Artificial Intelligence Assignment</title>");
		out.print("<link rel=\"stylesheet\" href=\"includes/style.css\">");

		out.print("</head>");
		out.print("<body>");
		out.print(
				"<div style=\"font-size:48pt; font-family:arial; color:#990000; font-weight:bold\">Wordcloud Generator</div>");

		out.print(
				"<p>The &quot;ignore words&quot; file is located at <font color=red><b>" + IgnoreWords.getAbsolutePath()
						+ "</b></font> and is <b><u>" + IgnoreWords.length() + "</u></b> bytes in size.");

		out.print("<p>The &quot;fuzzy&quot; file is located at <font color=red><b>" + FuzzyBFSFcl.getAbsolutePath()
				+ "</b></font> and is <b><u>" + FuzzyBFSFcl.length() + "</u></b> bytes in size.");

		out.print("<p>The &quot;fuzzy&quot; file is located at <font color=red><b>" + FuzzyBeamFcl.getAbsolutePath()
				+ "</b></font> and is <b><u>" + FuzzyBeamFcl.length() + "</u></b> bytes in size.");

		out.print("<p>The &quot;fuzzy&quot; file is located at <font color=red><b>" + NeuralNetwork.getAbsolutePath()
				+ "</b></font> and is <b><u>" + NeuralNetwork.length() + "</u></b> bytes in size.");

		out.print("<p><fieldset><legend><h3>Result</h3></legend>");

		/* Get search type as int */
		if (searchOption.contains("Best")) {
			searchType = 1;
		} else {
			searchType = 2;
		}

		stopwatch.start(); // Start time
		
		/* New wordcloud processor with user input params */
		WordcloudProcessor wordcloudProcessor = new WordcloudProcessor(new Wordcloud(query, maxWords), searchType,
				branchingFactor, maxDepth);

		/* Future that returns frequency array */
		CompletableFuture<WordFrequency[]> future = CompletableFuture.supplyAsync(() -> wordcloudProcessor.process(),
				pool);

		/* Return the top x words from future call */
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
		
		
		stopwatch.stop(); // Stop timer 
		
		out.print("</fieldset>");
		/* Print some stats */
		out.print("<p>Wordcloud generated from query '<b>" + query
				+ "</b>'. The neural network attempted to categorize your query as '<b>" + Categorize.getCategory()
				+ "</b>'</p>");
		out.print("<p>A <b>" + searchOption + "</b> was performed and generated your wordcloud in <b>" + stopwatch.toString() + "</b>'</p>");
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