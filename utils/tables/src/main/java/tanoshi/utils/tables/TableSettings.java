package tanoshi.utils.tables;

import tanoshi.utils.colors.ConsoleTextColor;

public class TableSettings {

    private TableFormat format = TableFormat.DEFAULT;
    private TableStyle style = TableStyle.DEFAULT;
    private ConsoleTextColor contentColor = null;

    public TableFormat getFormat() {
        return format;
    }

    public void setFormat(TableFormat tableFormat) {
        this.format = tableFormat;
    }

    public ConsoleTextColor getContentColor() {
        return contentColor;
    }

    public void setContentColor(ConsoleTextColor contentColor) {
        this.contentColor = contentColor;
    }

    public TableStyle getStyle() {
        return style;
    }

    public void setStyle(TableStyle style) {
        this.style = style;
    }

    public String colorizedContent(String content) {
        return contentColor == null ? content : contentColor.colorizeText(content);
    }
}