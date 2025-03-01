package tn.esprit.jdbc.services;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMetadataService {

    private final Connection connection;

    public DatabaseMetadataService(Connection connection) {
        this.connection = connection;
    }

    public List<TableSpecification> getTableSpecifications() throws SQLException {
        List<TableSpecification> tableSpecs = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();

        // Get all tables
        ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            TableSpecification tableSpec = new TableSpecification(tableName);

            // Get columns for the table
            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                tableSpec.addColumn(columnName, columnType, columnSize);
            }

            tableSpecs.add(tableSpec);
        }

        return tableSpecs;
    }

    public static class TableSpecification {
        private final String tableName;
        private final List<ColumnSpecification> columns;

        public TableSpecification(String tableName) {
            this.tableName = tableName;
            this.columns = new ArrayList<>();
        }

        public void addColumn(String name, String type, int size) {
            columns.add(new ColumnSpecification(name, type, size));
        }

        public String getTableName() {
            return tableName;
        }

        public List<ColumnSpecification> getColumns() {
            return columns;
        }
    }

    public static class ColumnSpecification {
        private final String name;
        private final String type;
        private final int size;

        public ColumnSpecification(String name, String type, int size) {
            this.name = name;
            this.type = type;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public int getSize() {
            return size;
        }
    }
}