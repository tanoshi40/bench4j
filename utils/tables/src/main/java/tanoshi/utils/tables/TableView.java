package tanoshi.utils.tables;

import tanoshi.utils.tables.models.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class TableView {

    public static TableView build(Table table) {
        return build(table, new TableSettings());
    }

    public static TableView build(Table table, TableSettings settings) {
        if (settings == null) {
            settings = new TableSettings();
        }

        int[] columnWidths = TableUtils.calcColumnWidths(table);
        int totalWidth = TableUtils.adjustColumnForTitles(columnWidths, table.getTitle());

        String connectorTemplate = settings.getFormat().buildConnectorTemplate(columnWidths);

        return new TableView(settings, connectorTemplate, columnWidths, totalWidth).addTable(table, true);
    }

    public static TableView build(Table[] table, String[] headers) {
        return build(table, headers, null, new TableSettings());
    }

    public static TableView build(Table[] tables, String[] headers, String title, TableSettings settings) {
        if (settings == null) {
            settings = new TableSettings();
        }

        String[] titles = new String[tables.length + 1];
        titles[0] = title;
        for (int i = 0; i < tables.length; i++) {
            titles[i + 1] = tables[i].getTitle();
        }

        int[] columnWidths = TableUtils.calcColumnWidths(tables, headers);
        int totalWidth = TableUtils.adjustColumnForTitles(columnWidths, titles);

        String connectorTemplate = settings.getFormat().buildConnectorTemplate(columnWidths);

        TableView tableView = new TableView(settings, connectorTemplate, columnWidths, totalWidth);

        if (title != null) {
            tableView.addRow(new TitleRow(title));
        }

        tableView.addRow(new HeaderRow(headers));

        for (Table table : tables) {
            tableView.addTable(table, false)
                    .addRow(new EmptyRow());
        }

        return tableView;
    }


    private final TableSettings settings;

    private final String connectorTemplate;
    private final int[] columnWidths;
    private final int totalWidth;

    private final List<ITableRow> rows;

    TableView(TableSettings settings, String connectorTemplate, int[] columnWidths, int totalWidth) {
        this.settings = settings;

        this.connectorTemplate = connectorTemplate;
        this.columnWidths = columnWidths;
        this.totalWidth = totalWidth;

        rows = new ArrayList<>();
    }

    TableView addRow(ITableRow row) {
        rows.add(row);
        return this;
    }

    TableView addTable(Table table, boolean includeHeader) {
        if (table.getTitle() != null) {
            addRow(new TitleRow(table.getTitle()));
        }

        if (includeHeader) {
            addRow(new HeaderRow(table.getHeaders()));
        }

        for (String[] row : table.getContent()) {
            addRow(new ContentRow(row));
        }
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TableView.class.getSimpleName() + "[", "]")
                .add("settings=" + settings)
                .add("rows={" + rows + "}")
                .toString();
    }

    public String toView() {
        StringJoiner lines = new StringJoiner("\n");

        String defaultConnector = TableUtils.buildDefaultConnector(connectorTemplate, settings);

        // top line
        if (!settings.getFormat().equals(TableFormat.MINIMAL)) {
            lines.add(TableUtils.buildConnector(
                    connectorTemplate,
                    TableUtils.ConnectorType.TOP,
                    rows.get(0).getType() == ITableRow.TableRowType.TITLE,
                    false,
                    settings));
        }

        ITableRow.TableRowType lastRowType = null;
        for (ITableRow row : rows) {
            ITableRow.TableRowType type = row.getType();
            String connector = buildConnector(type, lastRowType, settings, defaultConnector);

            if (!type.equals(ITableRow.TableRowType.SEPARATOR)) {
                if (connector != null) {
                    lines.add(connector);
                }
                lines.add(row.build(settings, columnWidths, totalWidth));
            }

            lastRowType = type;
        }

        // add bottom line (if not minimalistic)
        if (!settings.getFormat().equals(TableFormat.MINIMAL)) {
            lines.add(TableUtils.buildConnector(
                    connectorTemplate, TableUtils.ConnectorType.BOTTOM,
                    settings));
        }

        return lines.toString();
    }

    private String buildConnector(ITableRow.TableRowType nextType,
                                  ITableRow.TableRowType lastType,
                                  TableSettings settings,
                                  String connector) {

        if (nextType == null || lastType == null) {
            return null;
        }

        if (nextType.equals(ITableRow.TableRowType.TITLE)) {
            return TableUtils.buildConnector(connectorTemplate, TableUtils.ConnectorType.CENTER, true, settings.getFormat() == TableFormat.LARGE, settings);
        }

        return switch (lastType) {
            case TITLE -> switch (settings.getFormat()) {
                case MINIMAL -> null;
                case DEFAULT, LARGE ->
                        TableUtils.buildConnector(connectorTemplate, TableUtils.ConnectorType.BOTTOM, true, false, settings);
            };
            case HEADER, SEPARATOR -> switch (settings.getFormat()) {
                case MINIMAL, DEFAULT -> connector;
                case LARGE ->
                        TableUtils.buildConnector(connectorTemplate, TableUtils.ConnectorType.CENTER, false, true, settings);
            };
            case CONTENT -> switch (settings.getFormat()) {
                case MINIMAL, DEFAULT -> null;
                case LARGE -> connector;
            };
        };
    }
}