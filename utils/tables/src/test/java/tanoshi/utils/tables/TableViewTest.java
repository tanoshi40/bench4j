package tanoshi.utils.tables;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import tanoshi.testdata.models.Person;
import tanoshi.testdata.provider.PersonProvider;
import tanoshi.utils.tables.models.Table;
import tanoshi.utils.tables.settings.TableFormat;
import tanoshi.utils.tables.settings.TableSettings;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({SnapshotExtension.class})
class TableViewTest {

    private Expect expect;
    
    @ParameterizedTest
    @EnumSource(value = TableFormat.class)
    public void viewSimple(TableFormat format) {
        // Arrange
        Table table = buildTable(10, null);

        TableSettings settings = new TableSettings().withFormat(format);

        // Act
        TableView builder = TableView.build(table, settings);
        String tableString = builder.toView();

        // Assert
        assertNotNull(tableString, "table should not be null");

        expect.scenario(format.name()).toMatchSnapshot(tableString);
    }

    @ParameterizedTest
    @EnumSource(value = TableFormat.class)
    public void viewTitleSimple(TableFormat format) {
        // Arrange
        Table table = buildTable(10, "title");

        TableSettings settings = new TableSettings().withFormat(format);

        // Act
        TableView builder = TableView.build(table, settings);
        String tableString = builder.toView();

        // Assert
        assertNotNull(tableString, "table should not be null");

        expect.scenario(format.name()).toMatchSnapshot(tableString);
    }

    @ParameterizedTest
    @EnumSource(value = TableFormat.class)
    public void viewComplex(TableFormat format) {
        // Arrange
        String[] headers = getHeaders();
        Table[] tables = buildTables(false);

        TableSettings settings = new TableSettings().withFormat(format);

        // Act
        TableView builder = TableView.build(tables, headers, "complex table", settings);
        String tableString = builder.toView();

        // Assert
        assertNotNull(tableString, "table should not be null");

        expect.scenario(format.name()).toMatchSnapshot(tableString);
    }

    @ParameterizedTest
    @EnumSource(value = TableFormat.class)
    public void viewTitleComplex(TableFormat format) {
        // Arrange
        String[] headers = getHeaders();
        Table[] tables = buildTables(true);

        TableSettings settings = new TableSettings().withFormat(format);

        // Act
        TableView builder = TableView.build(tables, headers, "complex table", settings);
        String tableString = builder.toView();

        // Assert
        assertNotNull(tableString, "table should not be null");

        expect.scenario(format.name()).toMatchSnapshot(tableString);
    }

    private static String[] getHeaders() {
        return new String[]{"id", "name", "age"};
    }

    private static Table[] buildTables(boolean withTitles) {
        return new Table[]{
                buildTable(10, withTitles ? "table 1" : null),
                buildTable(5, withTitles ? "table 2" : null),
                buildTable(7, withTitles ? "table 3" : null)
        };
    }

    private static Table buildTable(int size, String title) {
        return Table.builder(getHeaders(), getContent(size))
                .withTitle(title).build();
    }

    private static String[][] getContent(int size) {
        String[][] content = new String[size][3];
        List<Person> persons = PersonProvider.getPersons(size);
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            content[i][0] = String.valueOf(person.getId());
            content[i][1] = person.getName();
            content[i][2] = String.valueOf(person.getAge());
        }
        return content;
    }
}