package tanoshi.utils.tables.settings;

import tanoshi.utils.colors.ConsoleTextColor;

public class TableSettings {

    private TableFormat format = TableFormat.DEFAULT;
    private TableStyle style = TableStyle.DEFAULT;
    private ConsoleTextColor contentColor = null;
    private ConsoleTextColor lineColor = null;

    public TableFormat getFormat() {
        return format;
    }

    public TableSettings withFormat(TableFormat tableFormat) {
        this.format = tableFormat;
        return this;
    }

    public ConsoleTextColor getContentColor() {
        return contentColor;
    }

    public TableSettings withContentColor(ConsoleTextColor contentColor) {
        this.contentColor = contentColor;
        return this;
    }

    public TableSettings withLineColor(ConsoleTextColor lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public ConsoleTextColor getLineColor() {
        return lineColor;
    }

    public TableStyle getStyle() {
        return style;
    }

    public TableSettings withStyle(TableStyle style) {
        this.style = style;
        return this;
    }

    public String colorizedContent(String content) {
        return contentColor == null ? content : contentColor.colorizeText(content);
    }

    public String colorizedLineElement(String lineContent) {
        return lineColor == null ? lineContent : lineColor.colorizeText(lineContent);
    }
}