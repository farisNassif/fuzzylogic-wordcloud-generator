package ie.gmit.sw.ai.V4;

import java.util.Arrays;
import java.util.EnumMap;

public class Document_Weights {
	public enum Weight_Names {
		meta, title, h1, h2, h3, body
	}

	public static String[] Weight_Name_Array() {
		String valuesStr = Arrays.toString(Document_Weights.Weight_Names.values());
		return valuesStr.substring(1, valuesStr.length() - 1).replace(" ", "").split(",");
	}

	// Maps integer values to their corresponding String values
	public static EnumMap<Weight_Names, Integer> Weight_Value_Mapping() {
		EnumMap<Weight_Names, Integer> weight_value_map = new EnumMap<Weight_Names, Integer>(Weight_Names.class);
		weight_value_map.put(Weight_Names.meta, 20);
		weight_value_map.put(Weight_Names.title, 12);
		weight_value_map.put(Weight_Names.h1, 9);
		weight_value_map.put(Weight_Names.h2, 7);
		weight_value_map.put(Weight_Names.h3, 5);
		weight_value_map.put(Weight_Names.body, 3);
		return weight_value_map;
	}
}
