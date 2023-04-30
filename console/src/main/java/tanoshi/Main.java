package tanoshi;

import tanoshi.utils.tables.TableFormat;
import tanoshi.utils.tables.TableSettings;
import tanoshi.utils.tables.TableView;
import tanoshi.utils.tables.models.Table;

public class Main {
    public static void main(String[] args) {

        testTables();
    }

    private static void testTables() {
        testNormal();
        testNormalTitle();

        testComplex();
        testComplexTitle();
    }

    private static String[][] getContent() {
        return new String[][]{
                {"entry1", "entry2", "entry3"},
                {"entry4", "entry5", "entry6"},
                {"entry7", "entry8", "entry9"},
                {"entry10", "entry11", "entry12"},
        };
    }

    private static String[] getHeaders() {
        return new String[]{"column1", "column2", "column3"};
    }

    private static void testNormal() {
        String[] headers = getHeaders();
        Table table = new Table(headers, getContent());

        TableSettings tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.MINIMAL);
        String print = TableView.build(table, tableSettings).toView();
        System.out.println(print);

        System.out.println();

        tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.DEFAULT);
        print = TableView.build(table, tableSettings).toView();
        System.out.println(print);

        System.out.println();

        tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.LARGE);
        print = TableView.build(table, tableSettings).toView();
        System.out.println(print);

        System.out.println();
        System.out.println();
        System.out.println();
    }


    private static void testNormalTitle() {
        String[] headers = getHeaders();
        Table table = new Table(headers, getContent(), "some title");

        TableSettings tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.MINIMAL);
        String print = TableView.build(table, tableSettings).toView();
        System.out.println(print);

        System.out.println();

        tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.DEFAULT);
        print = TableView.build(table, tableSettings).toView();
        System.out.println(print);

        System.out.println();

        tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.LARGE);
        print = TableView.build(table, tableSettings).toView();
        System.out.println(print);

        System.out.println();
        System.out.println();
        System.out.println();
    }

    private static void testComplex() {
        String[] headers = getHeaders();
        Table table = new Table(headers, getContent());
        Table table2 = new Table(headers, getContent());
        Table table3 = new Table(headers, getContent());
        Table[] tables = new Table[]{table, table2, table3};

        TableSettings tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.MINIMAL);
        String print = TableView.build(tables, headers, null, tableSettings).toView();
        System.out.println(print);

        System.out.println();

        tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.DEFAULT);
        print = TableView.build(tables, headers, null, tableSettings).toView();
        System.out.println(print);

        System.out.println();

        tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.LARGE);
        print = TableView.build(tables, headers, null, tableSettings).toView();
        System.out.println(print);

        System.out.println();
        System.out.println();
        System.out.println();
    }

    private static void testComplexTitle() {
        String[] headers = getHeaders();
        Table table = new Table(headers, getContent(), "table number one");
        Table table2 = new Table(headers, getContent(), "table number two");
        Table table3 = new Table(headers, getContent(), "table number three");
        Table[] tables = new Table[]{table, table2, table3};

        TableSettings tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.MINIMAL);
        String print = TableView.build(tables, headers, "tables", tableSettings).toView();
        System.out.println(print);

        System.out.println();

        tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.DEFAULT);
        print = TableView.build(tables, headers, "tables", tableSettings).toView();
        System.out.println(print);

        System.out.println();

        tableSettings = new TableSettings();
        tableSettings.setFormat(TableFormat.LARGE);
        print = TableView.build(tables, headers, "tables", tableSettings).toView();
        System.out.println(print);

        System.out.println();
        System.out.println();
        System.out.println();
    }

}