package search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class InvertedIndexSearchEngine implements SearchEngine {

    private Map<String, List<Integer>> invertedIndex;

    public InvertedIndexSearchEngine(Map<String, List<Integer>> invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    @Override
    public List<String> search(String[] data, String query, SearchType searchType) {
        String[] queryWords = query.split("\\s+");
        Set<Integer> indexes = null;
        switch (searchType) {
            case ALL:
                indexes = searchAll(queryWords);
                break;
            case ANY:
                indexes = searchAny(queryWords);
                break;
            case NONE:
                indexes = searchNone(data, queryWords);
                break;
            default:
                throw new IllegalArgumentException("Search type cannot be null");
        }

        List<String> results = new ArrayList<>();
        indexes.stream().forEach(index -> results.add(data[index]));
        return results;
    }

    private Set<Integer> searchAll(String[] queryWords) {
        Set<Integer> results = new HashSet<>();
        if (queryWords.length == 0) {
            return results;
        }

        List<Integer> indexes = invertedIndex.get(queryWords[0].toLowerCase());
        if (indexes != null) {
            results.addAll(indexes);
        }

        for (int i = 1; i < queryWords.length; i++) {
            indexes = invertedIndex.get(queryWords[i].toLowerCase());
            if (indexes != null) {
                results.retainAll(indexes);
            }
        }
        return results;
    }

    private Set<Integer> searchAny(String[] queryWords) {
        Set<Integer> results = new HashSet<>();

        List<Integer> indexes;
        for (String query : queryWords) {
            indexes = invertedIndex.get(query.toLowerCase());
            if (indexes != null) {
                results.addAll(indexes);
            }
        }
        return results;
    }

    private Set<Integer> searchNone(String[] data, String[] queryWords) {
        Set<Integer> searchAnyIndexes = searchAny(queryWords);

        Set<Integer> results = new HashSet<>();
        for (int i = 0; i < data.length; i++) {
            results.add(i);
        }

        results.removeIf(searchAnyIndexes::contains);
        return results;
    }
}