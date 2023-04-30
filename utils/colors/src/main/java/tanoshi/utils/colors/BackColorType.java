package tanoshi.utils.colors;

public enum BackColorType {
    NORMAL(false),
    BRIGHT(true);

    private final boolean bright;

    BackColorType(boolean bright) {
        this.bright = bright;
    }

    public String getColorCode(Color color) {
        return color.getCode(CodeType.BACKGROUND, bright);
    }
}