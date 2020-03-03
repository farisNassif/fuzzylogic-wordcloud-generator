package ie.gmit.sw.ai.V3;

import java.util.Collection;

import ie.gmit.sw.ai.V3.Document_Weights.Weight_Names;

public class RunnerV6 {

	public static void main(String[] args) {
		Document_Weights value_map = new Document_Weights();
		Collection<Weight_Names> weight_keyset = Document_Weights.Weight_Value_Mapping().keySet();
		
		for (Object s : weight_keyset) {
			System.out.println(value_map.Weight_Value_Mapping().get(s));
		}
	}

}
