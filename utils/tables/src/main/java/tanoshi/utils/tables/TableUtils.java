package tanoshi.utils.tables;

import tanoshi.utils.tables.models.Table;

import java.util.Arrays;
import java.util.StringJoiner;

class TableUtils {

    private static final String defaultRowFormat = "%c %s %c";

    enum ConnectorType {
        TOP,
        BOTTOM,
        CENTER
    }

    static String buildRow(int[] columnWidths, String[] rowContent, TableSettings settings) {
        char verticalChar = TableCharacters.CharacterType.LINE_VERTICAL.getChar();
        StringJoiner builder = new StringJoiner(String.format(" %c ", verticalChar));
        for (int i = 0; i < rowContent.length; i++) {
            String rawEntryText = rowContent[i];
            int width = columnWidths[i];
            builder.add(buildPaddedEntry(rawEntryText, width));
        }

        return switch (settings.getFormat()) {
            case MINIMAL -> builder.toString();
            case DEFAULT, LARGE -> String.format(defaultRowFormat, verticalChar, builder, verticalChar);
        };
    }

    static String buildPaddedEntry(String rowEntry, int width) {
        // entry is empty -> new padded string
        if (rowEntry.length() == 0) {
            char[] chars = new char[width];
            Arrays.fill(chars, ' ');
            return String.valueOf(chars);
        }

        // entry has content but needs padding
        if (rowEntry.length() < width) {
            char[] chars = new char[width];
            rowEntry.getChars(0, rowEntry.length(), chars, 0);
            Arrays.fill(chars, rowEntry.length(), width, ' ');
            return String.valueOf(chars);
        }

        // no padding required
        return rowEntry;
    }

    static String buildDefaultConnector(String lineTemplate, TableSettings settings) {
        return buildConnector(lineTemplate, ConnectorType.CENTER, settings);
    }
    
    static String buildConnector(String lineTemplate, ConnectorType position, TableSettings settings) {
        return buildConnector(lineTemplate, position, false, false, settings);
    }

    static String buildConnector(String lineTemplate, ConnectorType position, boolean titleLine, boolean doubleHorizontal, TableSettings settings) {
        TableCharacters.LineType lineType = doubleHorizontal ? TableCharacters.LineType.DOUBLE_HORIZONTAL : TableCharacters.LineType.NORMAL;

        return buildConnector(
                lineTemplate,
                switch (position) {
                    case TOP -> TableCharacters.CharacterType.CORNER_TOP_LEFT.getChar(lineType);
                    case CENTER -> TableCharacters.CharacterType.CROSS_LEFT.getChar(lineType);
                    case BOTTOM -> titleLine ?
                            TableCharacters.CharacterType.CROSS_LEFT.getChar(lineType) :
                            TableCharacters.CharacterType.CORNER_BOTTOM_LEFT.getChar(lineType);
                },
                titleLine ?
                        switch (position) {
                            case TOP -> TableCharacters.CharacterType.LINE_HORIZONTAL.getChar(lineType);
                            case CENTER -> TableCharacters.CharacterType.CROSS_BOTTOM.getChar(lineType);
                            case BOTTOM -> TableCharacters.CharacterType.CROSS_TOP.getChar(lineType);
                        } :
                        switch (position) {
                            case TOP -> TableCharacters.CharacterType.CROSS_TOP.getChar(lineType);
                            case CENTER -> TableCharacters.CharacterType.CROSS_CENTER.getChar(lineType);
                            case BOTTOM -> TableCharacters.CharacterType.CROSS_BOTTOM.getChar(lineType);
                        },
                switch (position) {
                    case TOP -> TableCharacters.CharacterType.CORNER_TOP_RIGHT.getChar(lineType);
                    case CENTER -> TableCharacters.CharacterType.CROSS_RIGHT.getChar(lineType);
                    case BOTTOM -> titleLine ?
                            TableCharacters.CharacterType.CROSS_RIGHT.getChar(lineType) :
                            TableCharacters.CharacterType.CORNER_BOTTOM_RIGHT.getChar(lineType);
                },
                TableCharacters.CharacterType.LINE_HORIZONTAL.getChar(lineType)
        );
    }

    static String buildConnector(String lineTemplate, char l, char s, char r, char h) {
        return lineTemplate
                .replace('h', h)
                .replace('l', l)
                .replace('c', s)
                .replace('r', r);
    }

    static String buildTitleRow(String title, int totalWidth, TableSettings settings) {
        return switch (settings.getFormat()) {
            case MINIMAL -> title;
            case DEFAULT, LARGE -> {
                char verticalChar = TableCharacters.CharacterType.LINE_VERTICAL.getChar();
                yield String.format(defaultRowFormat, verticalChar, buildPaddedEntry(title, totalWidth), verticalChar);
            }
        };
    }

    static int[] calcColumnWidths(String[][] content, String[] headers) {
        int[] columnWidths = new int[headers.length];

        for (int i = 0; i < headers.length; i++) columnWidths[i] = headers[i].length();

        for (String[] row : content) {
            for (int i = 0; i < row.length; i++) {
                String entry = row[i];

                if (entry.length() > columnWidths[i]) {
                    columnWidths[i] = entry.length();
                }
            }
        }
        return columnWidths;
    }

    static int[] calcColumnWidths(Table table) {
        return calcColumnWidths(table.getContent(), table.getHeaders());
    }

    static int[] calcColumnWidths(Table[] tables, String[] headers) {
        int rows = 0;
        for (Table table : tables) {
            rows += table.getContent().length;
        }

        String[][] mergedRows = new String[rows][headers.length];

        int row = 0;
        for (Table table : tables) {
            for (String[] strings : table.getContent()) {
                mergedRows[row] = strings;
                row++;
            }
        }

        return calcColumnWidths(mergedRows, headers);
    }

    static int adjustColumnForTitles(int[] initialColumnWidths, String title) {
        int totalWidth = calcTotalWidth(initialColumnWidths);

        // has title && title is bigger than total length -> adjust lengths to fit with title
        if (title != null && !title.isEmpty() && totalWidth < title.length()) {
            initialColumnWidths[initialColumnWidths.length - 1] += (title.length() - totalWidth);
            totalWidth = title.length();
        }
        return totalWidth;
    }

    static int adjustColumnForTitles(int[] initialColumnWidths, String[] titles) {
        int totalWidth = calcTotalWidth(initialColumnWidths);

        int longestTitleLength = Arrays.stream(titles)
                .filter(title -> title != null && !title.isEmpty())
                .mapToInt(String::length)
                .max().orElse(0);

        // longestTitle bigger than totalWidth -> adjust lengths to fit with title
        if (totalWidth < longestTitleLength) {
            initialColumnWidths[initialColumnWidths.length - 1] += (longestTitleLength - totalWidth);
            totalWidth = longestTitleLength;
        }
        return totalWidth;
    }

    static int calcTotalWidth(int[] columnWidths) {
        return Arrays.stream(columnWidths).sum() + (columnWidths.length * 3) - 3;
    }
}
