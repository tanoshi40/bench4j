package tanoshi.utils.tables.settings;

import tanoshi.utils.colors.ConsoleTextColor;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum TableFormat {
    MINIMAL {
        @Override
        public String buildConnectorTemplate(int[] columnWidths) {
            StringBuilder builder = new StringBuilder();

            char[] preChars = new char[columnWidths[0] + 1];
            Arrays.fill(preChars, 'h');
            builder.append(String.valueOf(preChars));

            for (int i = 1; i < columnWidths.length - 1; i++) {
                builder.append('c');

                int columnWidth = columnWidths[i];

                char[] chars = new char[columnWidth + 2];
                Arrays.fill(chars, 'h');
                builder.append(String.valueOf(chars));
            }

            builder.append('c');
            preChars = new char[columnWidths[columnWidths.length - 1] + 1];
            Arrays.fill(preChars, 'h');
            builder.append(String.valueOf(preChars));

            return colorize(builder.toString(),getLineColor());
        }
    },
    DEFAULT {
        @Override
        public String buildConnectorTemplate(int[] columnWidths) {
            return colorize(buildStandardConnectorTemplate(columnWidths), getLineColor());
        }
    },
    LARGE {
        @Override
        public String buildConnectorTemplate(int[] columnWidths) {
            return colorize(buildStandardConnectorTemplate(columnWidths), getLineColor());
        }
    };


    private ConsoleTextColor lineColor = null;

    public abstract String buildConnectorTemplate(int[] columnWidths);

    public void setLineColor(ConsoleTextColor lineColor) {
        this.lineColor = lineColor;
    }

    ConsoleTextColor getLineColor() {
        return lineColor;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static String buildStandardConnectorTemplate(int[] columnWidths) {
        // "lhhhshhhshhhshr"
        StringBuilder builder = new StringBuilder();

        builder.append('l');

        builder.append(Arrays.stream(columnWidths).mapToObj(width -> {
            char[] chars = new char[width + 2];
            Arrays.fill(chars, 'h');
            return String.valueOf(chars);
        }).collect(Collectors.joining("c")));

        builder.append('r');
        return builder.toString();
    }

    private static String colorize(String content, ConsoleTextColor color) {
        if (color == null) {
            return content;
        }
        return color.colorizeText(content);
    }
}
