package search;

enum SearchType {

    ALL("ALL"),
    ANY("ANY"),
    NONE("NONE");

    private String type;

    private SearchType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static SearchType valueOfType(String value) {
        for (SearchType type : values()) {
            if (type.getType().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
