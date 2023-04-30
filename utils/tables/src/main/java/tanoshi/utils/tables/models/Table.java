package tanoshi.utils.tables.models;

public class Table {
    private final String[] headers;
    private final String[][] content;
    private final String title;

    private Table(String[] headers, String[][] content, String title) {
        this.headers = headers;
        this.content = content;
        this.title = title;
    }

    public String[] getHeaders() {
        return headers;
    }

    public String[][] getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public static TableBuilder builder(String[] headers, String[][] content) {
        return new TableBuilder(headers, content);
    }

    public static class TableBuilder {
        private final String[] headers;
        private final String[][] content;
        private String title;

        public TableBuilder(String[] headers, String[][] content) {
            this(headers, content, null);
        }

        public TableBuilder(String[] headers, String[][] content, String title) {
            this.headers = headers;
            this.content = content;
            this.title = title;
        }

        public TableBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Table build() {
            return new Table(headers, content, title);
        }
    }
}
