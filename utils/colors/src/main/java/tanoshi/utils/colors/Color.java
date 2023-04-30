package tanoshi.utils.colors;

public enum Color {
    // Regular Colors. Normal color, no bold, background color etc.
    BLACK(AsciiColorCodes.BLACK,
            AsciiColorCodes.BLACK_BOLD,
            AsciiColorCodes.BLACK_UNDERLINED,
            AsciiColorCodes.BLACK_BACKGROUND,
            AsciiColorCodes.BLACK_BRIGHT,
            AsciiColorCodes.BLACK_BOLD_BRIGHT,
            AsciiColorCodes.BLACK_BACKGROUND_BRIGHT),
    RED(AsciiColorCodes.RED,
            AsciiColorCodes.RED_BOLD,
            AsciiColorCodes.RED_UNDERLINED,
            AsciiColorCodes.RED_BACKGROUND,
            AsciiColorCodes.RED_BRIGHT,
            AsciiColorCodes.RED_BOLD_BRIGHT,
            AsciiColorCodes.RED_BACKGROUND_BRIGHT),
    GREEN(AsciiColorCodes.GREEN,
            AsciiColorCodes.GREEN_BOLD,
            AsciiColorCodes.GREEN_UNDERLINED,
            AsciiColorCodes.GREEN_BACKGROUND,
            AsciiColorCodes.GREEN_BRIGHT,
            AsciiColorCodes.GREEN_BOLD_BRIGHT,
            AsciiColorCodes.GREEN_BACKGROUND_BRIGHT),
    YELLOW(AsciiColorCodes.YELLOW,
            AsciiColorCodes.YELLOW_BOLD,
            AsciiColorCodes.YELLOW_UNDERLINED,
            AsciiColorCodes.YELLOW_BACKGROUND,
            AsciiColorCodes.YELLOW_BRIGHT,
            AsciiColorCodes.YELLOW_BOLD_BRIGHT,
            AsciiColorCodes.YELLOW_BACKGROUND_BRIGHT),
    BLUE(AsciiColorCodes.BLUE,
            AsciiColorCodes.BLUE_BOLD,
            AsciiColorCodes.BLUE_UNDERLINED,
            AsciiColorCodes.BLUE_BACKGROUND,
            AsciiColorCodes.BLUE_BRIGHT,
            AsciiColorCodes.BLUE_BOLD_BRIGHT,
            AsciiColorCodes.BLUE_BACKGROUND_BRIGHT),
    MAGENTA(AsciiColorCodes.PURPLE,
            AsciiColorCodes.PURPLE_BOLD,
            AsciiColorCodes.PURPLE_UNDERLINED,
            AsciiColorCodes.PURPLE_BACKGROUND,
            AsciiColorCodes.PURPLE_BRIGHT,
            AsciiColorCodes.PURPLE_BOLD_BRIGHT,
            AsciiColorCodes.PURPLE_BACKGROUND_BRIGHT),
    CYAN(AsciiColorCodes.CYAN,
            AsciiColorCodes.CYAN_BOLD,
            AsciiColorCodes.CYAN_UNDERLINED,
            AsciiColorCodes.CYAN_BACKGROUND,
            AsciiColorCodes.CYAN_BRIGHT,
            AsciiColorCodes.CYAN_BOLD_BRIGHT,
            AsciiColorCodes.CYAN_BACKGROUND_BRIGHT),
    WHITE(AsciiColorCodes.WHITE,
            AsciiColorCodes.WHITE_BOLD,
            AsciiColorCodes.WHITE_UNDERLINED,
            AsciiColorCodes.WHITE_BACKGROUND,
            AsciiColorCodes.WHITE_BRIGHT,
            AsciiColorCodes.WHITE_BOLD_BRIGHT,
            AsciiColorCodes.WHITE_BACKGROUND_BRIGHT);

    private final String normal;
    private final String bold;
    private final String underlined;
    private final String background;
    private final String bright;
    private final String brightBold;
    private final String brightBackground;

    Color(String normal, String bold, String underlined, String background,
          String bright, String brightBold, String brightBackground) {

        this.normal = normal;
        this.bold = bold;
        this.underlined = underlined;
        this.background = background;
        this.bright = bright;
        this.brightBold = brightBold;
        this.brightBackground = brightBackground;
    }

    public String getCode() {
        return getCode(CodeType.NORMAL, false);
    }

    public String getCode(CodeType type) {
        return getCode(type, false);
    }

    public String getCode(CodeType type, boolean isBright) {
        return isBright ? switch (type) {
            case NORMAL -> bright;
            case BOLD -> brightBold;
            case UNDERLINED -> underlined;
            case BACKGROUND -> brightBackground;
        } : switch (type) {
            case NORMAL -> normal;
            case BOLD -> bold;
            case UNDERLINED -> underlined;
            case BACKGROUND -> background;
        };
    }

    public String colorizeText(String text) {
        return ColorUtils.colorizeText(text, this, null, null, null);
    }

    public String getReset() { return AsciiColorCodes.RESET; }
}
