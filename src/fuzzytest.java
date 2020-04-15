import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class fuzzytest {
	public static void main(String[] args) {

		/* Get a handle on the fcl file */
		FIS fis = FIS.load("res/Beam_Fuzzy.fcl", true);
		FunctionBlock fb = fis.getFunctionBlock("urlrelevance");

		/* Set fuzzy variables */
		fis.setVariable("occurance", 9);
		fis.setVariable("depth",8);
		fis.setVariable("parent",1);

		// Evaluate
		fis.evaluate();
		Variable relevance = fb.getVariable("relevance");
		JFuzzyChart.get().chart(relevance, relevance.getDefuzzifier(), true);
		JFuzzyChart.get().chart(fb);
		//JFuzzyChart.get().chart(relevance, relevance.getDefuzzifier(), true);
		// display page relevancy
		System.out.println("Relevance:" + relevance.getLatestDefuzzifiedValue() + "\n");

	}
}	
