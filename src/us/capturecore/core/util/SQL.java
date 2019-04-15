package us.capturecore.core.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {

    private static Connection connection;
    private static String url;

    public static Connection getConnection() { return connection; }

    public static void setupConnection(
            String hostName,
            String port,
            String database,
            String username,
            String password ) {

        String driver = "com.mysql.jdbc.Driver";
        url = "jdbc:mysql://" + hostName;

        if ( port != null && !port.equals( "" ) ) {
            url += ":" + port;
        }

        if ( driver != null && !driver.equals( "" ) ) {
            url += "/" + database;
        }

        try {
            Class.forName( driver );
        } catch ( Exception e ) {
            System.err.println( "[CaptureCore] SQL: Driver class not found." );
            return;
        }

        try {
            if ( username != null && !username.equals( "" ) && password != null && !password.equals( "" ) ) {
                connection = DriverManager.getConnection( url, username, password );
            } else {
                connection = DriverManager.getConnection( url );
            }
            //connection = DriverManager.getConnection( "jdbc:mysql://localhost/BlockcadeDB" );

        } catch ( SQLException e ) {
            System.out.println( "[CaptureCore] SQL: Error establishing connection using URL: " + url );
            e.printStackTrace();
            return;
        }
    }

    public static void refreshConnection() {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println( "[CaptureCore] SQL: Error establishing connection using URL: " + url );
            e.printStackTrace();
            return;
        }
    }

    public static void close() {
        if ( connection == null ) {
            return;
        }

        try {
            if ( !connection.isClosed() ) {
                connection.close();
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    public static ResultSet execute(String query) throws NullPointerException {
        try {
            Statement statement = connection.createStatement();
            connection.createStatement().execute(query);

            return statement.executeQuery(query);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a table in the database
     * @param tableName Name of the table you're trying to create
     * @param columnNameToDataType array of the column name to the data type
     */
    public static void createTable( String tableName, String[]...columnNameToDataType ) {
        if ( !hasOpenConnection() ) {
            return;
        }

        Statement statement = null;

        try {
            statement = connection.createStatement();
        } catch ( SQLException e1 ) {
            e1.printStackTrace();
        }

        String body = "";

        for ( String[] array : columnNameToDataType ) {
            if ( array.length < 2 ) {
                throw new Error( "Each column must have a corresponding data type." );
            }

            body += array[0] + " " + array[1] + ",";
        }

        body = body.substring( 0, body.length() - 1 );

        try {
            statement.execute( "CREATE TABLE " + tableName + " (ID int NOT NULL AUTO_INCREMENT, " +
                    body + ", PRIMARY KEY (ID));"  );
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Insert key value pairs into a table
     * @param tableName
     * @return False if there was an error executing the insert.
     */
    public static boolean insertInto( String tableName, String[] columnNames, String[] values ) {
        if ( !hasOpenConnection() ) {
            return false;
        }

        if ( !tableExists( tableName ) ) {
            return false;
        }

        if ( columnNames.length < 1 || values.length < 1 ) {
            return false;
        }

        Statement statement;

        try {
            statement = connection.createStatement();
        } catch ( SQLException e1 ) {
            e1.printStackTrace();
            return false;
        }

        // TODO
        String cols = "";

        for ( String col : columnNames ) {
            cols += col + ", ";
        }

        cols = cols.substring( 0, cols.length() - 2 );

        String vals = "";

        for ( String val : values ) {
            vals += quote(val) + ", ";
        }

        vals = vals.substring( 0, vals.length() - 2 );

        String s = "INSERT INTO " + tableName + "(" + cols + ")" + " VALUES (" + vals + ");";

        try {
            statement.executeUpdate( s );
        } catch ( SQLException e ) {
            System.out.println( "\n\nERROR IN SQL STATEMENT \'" + s + "\'\n\n" );
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * EXISTS(SELECT 1 FROM table_name WITH(NOLOCK) WHERE condition)
     * @param tableName
     * @param condition
     * @return True if record exists for condition
     */
    public static boolean recordExists( String tableName, String condition ) {
        if ( !hasOpenConnection() ) {
            return false;
        }

        if ( !tableExists( tableName ) ) {
            return false;
        }

        Statement statement;

        try {
            statement = connection.createStatement();
        } catch ( SQLException e1 ) {
            e1.printStackTrace();
            return false;
        }

        String s = "SELECT 1 FROM " + tableName + " WHERE " + condition;

        try {
            ResultSet r = statement.executeQuery( s );
            return r.next();
        } catch ( SQLException e ) {
            System.out.println( "\n\nERROR IN SQL STATEMENT \'" + s + "\'\n\n" );
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update a text element in a table
     * @param tableName
     * @param condition CAN BE NULL!
     * @return False if there was an error making the update.
     */
    public static boolean updateTextInTable( String tableName, String condition, String[]... columnsToValues ) {
        if ( !hasOpenConnection() ) {
            return false;
        }

        if ( !tableExists( tableName ) ) {
            return false;
        }

        Statement statement = null;

        try {
            statement = connection.createStatement();
        } catch ( SQLException e1 ) {
            e1.printStackTrace();
        }

        String body = "";

        for ( String[] colsToVal : columnsToValues ) {
            body += colsToVal[0] + " = \'" + colsToVal[1] + "\', ";
        }

        body = body.substring( 0, body.length() - 2 );

        if ( condition != null && !condition.equalsIgnoreCase( "" ) ) {
            String s = "UPDATE " + tableName + " SET " + body + " WHERE " + condition + ";";
            try {
                statement.executeUpdate( s );
            } catch ( SQLException e ) {
                System.out.println( "\n\nERROR IN SQL STATEMENT \'" + s + "\'\n\n" );
                return false;
            }

            return true;
        }

        String s = "UPDATE " + tableName + " SET " + body + ";";
        try {
            statement.executeUpdate( s );
        } catch ( SQLException e ) {
            System.out.println( "\n\nERROR IN SQL STATEMENT \'" + s + "\'\n\n" );
            return false;
        }

        return true;
    }

    public static ResultSet selectFromTable( String tableName, String condition, String...columns ) {
        if ( !hasOpenConnection() ) {
            return null;
        }

        if ( !tableExists( tableName ) ) {
            return null;
        }

        Statement statement = null;

        try {
            statement = connection.createStatement();
        } catch ( SQLException e1 ) {
            e1.printStackTrace();
        }

        String body = "";
        for ( String col : columns ) {
            body += col + ", ";
        }

        if ( body.length() > 1 ) {
            body = body.substring( 0, body.length() - 2 );
        }

        ResultSet output = null;

        if ( body.length() < 1 ) {
            body = "*";
        }

        if ( condition == null || condition.equals( "" ) ) {
            String s1 = "SELECT " + body + " FROM " + tableName + ";";
            try {
                output = statement.executeQuery( s1 );
            } catch ( SQLException e ) {
                System.out.println( "\n\nERROR IN SQL STATEMENT \'" + s1 + "\'\n\n" );
                e.printStackTrace();
            }

            return output;
        }

        String s = "SELECT " + body + " FROM " + tableName + " WHERE " + condition + ";";
        try {
            output = statement.executeQuery( s );
        } catch ( SQLException e ) {
            System.out.println( "\n\nERROR IN SQL STATEMENT \'" + s + "\'\n" );
            e.printStackTrace();
        }

        return output;
    }

    public static boolean tableExists( String tableName ) {
        if ( !hasOpenConnection() ) {
            return false;
        }

        Statement statement = null;

        try {
            statement = connection.createStatement();
        } catch ( SQLException e1 ) {
            e1.printStackTrace();
        }

        try {
            statement.executeQuery( "SELECT * FROM " + tableName + ";" );
        } catch ( SQLException e ) {
            return false;
        }
        return true;
    }

    public static boolean hasOpenConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch ( SQLException e ) {
            return false;
        }
    }

    public static String[] stringsToStringArray( String...strings ) {
        return strings;
    }

    public static String[][] stringArraysToArray( String[]...arrays ) {
        return arrays;
    }

    public static String quote( Object s ) {
        return "\"" + s + "\"";
    }

}