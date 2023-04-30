package tanoshi.utils.colors;

public class ColorUtils {
    private static final String fullTemplate = "%s%s%s%s";
    private static final String foregroundTemplate = "%s%s%s";

    public static String colorizeText(String text, Color textColor, Color backColor, TextColorType foregroundType, BackColorType backgroundType) {

        if (textColor == null) {
            textColor = Color.WHITE;
        }

        if (foregroundType == null) {
            foregroundType = TextColorType.NORMAL;
        }

        return backColor == null || backgroundType == null ?
                String.format(foregroundTemplate, foregroundType.getColorCode(textColor), text, AsciiColorCodes.RESET):
        String.format(fullTemplate,
                foregroundType.getColorCode(textColor),
                backgroundType.getColorCode(backColor),
                text, AsciiColorCodes.RESET);
    }

    public static String colorizeText(String text, ConsoleTextColor textColor) {
        return colorizeText(text, textColor.getTextColor(), textColor.getBackColor(), textColor.getTextType(), textColor.getBackType());
    }
}

