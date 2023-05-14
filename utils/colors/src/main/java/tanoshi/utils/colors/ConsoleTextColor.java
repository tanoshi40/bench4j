package tanoshi.utils.colors;

public class ConsoleTextColor {

    private final Color textColor;
    private final Color backColor;
    private final TextColorType textType;
    private final BackColorType backType;

    public ConsoleTextColor(Color textColor, Color backColor, TextColorType textType, BackColorType backType) {
        this.textColor = textColor;
        this.backColor = backColor;
        this.textType = textType;
        this.backType = backType;
    }

    public ConsoleTextColor(Color textColor, Color backColor) {
        this(textColor, backColor, TextColorType.NORMAL, BackColorType.NORMAL);
    }

    public ConsoleTextColor(Color textColor, TextColorType textType) {
        this(textColor, null, textType, BackColorType.NORMAL);
    }

    public ConsoleTextColor(Color textColor) {
        this(textColor, null, TextColorType.NORMAL, null);
    }

    public ConsoleTextColor() {
        this(Color.WHITE, null, TextColorType.NORMAL, null);
    }

    public String colorizeText(String text) {
        return ColorUtils.colorizeText(text, this);
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getBackColor() {
        return backColor;
    }

    public String getColorCode() {
        Color frColor = textColor;
        if (frColor == null) {
            frColor = Color.WHITE;
        }

        TextColorType frType = this.textType;
        if (frType == null) {
            frType = TextColorType.NORMAL;
        }

        if (backColor == null || backType == null) {
            return frType.getColorCode(frColor);
        }

        return frType.getColorCode(frColor) + backType.getColorCode(backColor);
    }

    public TextColorType getTextType() {
        return textType;
    }

    public BackColorType getBackType() {
        return backType;
    }
}
