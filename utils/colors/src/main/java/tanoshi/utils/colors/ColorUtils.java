package tanoshi.utils.colors;

public class ColorUtils {
    private static final String template = "%s%s%s";

    public static String colorizeText(String text, String colorCode) {
        return template.formatted(colorCode, text, AsciiColorCodes.RESET);
    }

    public static String colorizeText(String text, ConsoleTextColor textColor) {
        return colorizeText(text, textColor.getColorCode());
    }
}

