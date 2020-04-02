package search;

import java.util.List;

interface SearchEngine {
    List<String> search(String[] data, String query, SearchType searchType);
}
