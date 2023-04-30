package tanoshi.utils.tables;

import tanoshi.utils.colors.ConsoleTextColor;

public class TableSettings {

    private TableFormat format = TableFormat.DEFAULT;
    private TableStyle style = TableStyle.DEFAULT;
    private ConsoleTextColor color = new ConsoleTextColor();

    public TableFormat getFormat() {
        return format;
    }

    public void setFormat(TableFormat tableFormat) {
        this.format = tableFormat;
    }

    public ConsoleTextColor getColor() {
        return color;
    }

    public void setColor(ConsoleTextColor color) {
        this.color = color;
    }

    public TableStyle getStyle() {
        return style;
    }

    public void setStyle(TableStyle style) {
        this.style = style;
    }

}