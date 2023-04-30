package tanoshi.utils.tables.models;

import tanoshi.utils.colors.ConsoleTextColor;
import tanoshi.utils.tables.TableFormat;
import tanoshi.utils.tables.TableStyle;

public class TableSettings {

    private TableFormat format = TableFormat.DEFAULT;
    private TableStyle style = TableStyle.DEFAULT;
    private ConsoleTextColor contentColor = null;

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
}