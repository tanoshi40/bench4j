package tanoshi.utils.tables;


import tanoshi.utils.tables.models.TableSettings;

interface ITableRow {

    enum TableRowType {
        TITLE,
        HEADER,
        CONTENT,
        SEPARATOR
    }

    String build(TableSettings settings, int[] columnWidths, int totalWidth);

    TableRowType getType();
}

class TitleRow implements ITableRow {
    private final String title;

    TitleRow(String title) {
        this.title = title;
    }

    @Override
    public String build(TableSettings settings, int[] columnWidths, int totalWidth) {
        return TableUtils.buildTitleRow(title, totalWidth, settings);
    }

    @Override
    public TableRowType getType() {
        return TableRowType.TITLE;
    }
}

class ContentRow implements ITableRow {

    private final String[] content;

    ContentRow(String[] content) {
        this.content = content;
    }

    @Override
    public String build(TableSettings settings, int[] columnWidths, int totalWidth) {
        return TableUtils.buildRow(columnWidths, content, settings);
    }

    @Override
    public TableRowType getType() {
        return TableRowType.CONTENT;
    }
}

class HeaderRow implements ITableRow {
    private final String[] headers;

    HeaderRow(String[] headers) {
        this.headers = headers;
    }

    @Override
    public String build(TableSettings settings, int[] columnWidths, int totalWidth) {
        return TableUtils.buildRow(columnWidths, headers, settings);
    }

    @Override
    public TableRowType getType() {
        return TableRowType.HEADER;
    }
}

class EmptyRow implements ITableRow {

    @Override
    public String build(TableSettings settings, int[] columnWidths, int totalWidth) {
        return null;
    }

    @Override
    public TableRowType getType() {
        return TableRowType.SEPARATOR;
    }
}