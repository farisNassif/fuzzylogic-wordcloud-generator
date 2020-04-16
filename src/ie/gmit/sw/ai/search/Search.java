package ie.gmit.sw.ai.search;

import ie.gmit.sw.ai.cloud.WordFrequency;

/* Had planned to make this super class, didn't get around to refactoring everything */
public abstract class Search {
	abstract WordFrequency[] ExecuteSearch();
}
