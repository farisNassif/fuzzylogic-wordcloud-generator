package ie.gmit.sw.ai.V3;

import java.util.EnumMap;

public class Document_Weights {
	public enum Weight_Names {
		TITLE_WEIGHT, H1_WEIGHT, H2_WEIGHT, H3_WEIGHT, PARAGRAPH_WEIGHT, METADATA_WEIGHT
	}

	// Maps integer values to their corresponding String values
	public static EnumMap<Weight_Names, Integer> Weight_Value_Mapping() {
		EnumMap<Weight_Names, Integer> weight_value_map = new EnumMap<Weight_Names, Integer>(Weight_Names.class);
		weight_value_map.put(Weight_Names.METADATA_WEIGHT, 6);
		weight_value_map.put(Weight_Names.TITLE_WEIGHT, 20);
		weight_value_map.put(Weight_Names.H1_WEIGHT, 16);
		weight_value_map.put(Weight_Names.H2_WEIGHT, 13);
		weight_value_map.put(Weight_Names.H3_WEIGHT, 10);
		weight_value_map.put(Weight_Names.PARAGRAPH_WEIGHT, 6);
		return weight_value_map;
	}
}
