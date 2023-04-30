package tanoshi.utils.colors;

public enum TextColorType {
    NORMAL(CodeType.NORMAL, false),
    BOLD(CodeType.BOLD, false),
    UNDERLINED(CodeType.UNDERLINED, false),
    BRIGHT(CodeType.NORMAL, true),
    BRIGHT_BOLD(CodeType.BOLD, true);

    private final CodeType codeType;
    private final boolean bright;

    TextColorType(CodeType codeType, boolean bright) {
        this.codeType = codeType;
        this.bright = bright;
    }

    public String getColorCode(Color color) {
        return color.getCode(codeType, bright);
    }
}
