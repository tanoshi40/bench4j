package tanoshi.utils.tables.models;

public class Table {
    private final String[] headers;
    private final String[][] content;
    private final String title;

    public Table(String[] headers, String[][] content, String title) {
        this.headers = headers;
        this.content = content;
        this.title = title;
    }
    public Table(String[] headers, String[][] content) {
        this(headers, content, null);
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
}
