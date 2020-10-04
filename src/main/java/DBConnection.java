import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

public class DBConnection
{
    private static Connection connection;

    private static String dbName = "learn";
    private static String dbUser = "root";
    private static String dbPass = "testtest";
    private static String sql;
    private static StringBuilder stringBuilder = new StringBuilder();


    public static Connection getConnection()
    {
        if(connection == null)
        {
            try {
                connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + dbName +
                    "?user=" + dbUser + "&password=" + dbPass + "&serverTimezone=UTC");
                connection.createStatement().execute("DROP TABLE IF EXISTS voter_count");
                connection.createStatement().execute("CREATE TABLE voter_count(" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "name TINYTEXT NOT NULL, " +
                        "birthDate DATE NOT NULL, " +
                        "`count` INT NOT NULL, " +
                        "PRIMARY KEY(id))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void executeMultiInsert() throws SQLException {
        sql = "INSERT INTO voter_count (name, birthDate, `count`) " +
                "VALUES " + stringBuilder.toString() + ";";

        DBConnection.getConnection().createStatement().execute(sql);
        stringBuilder = new StringBuilder();
    }

    public static int customSelect() throws SQLException{
        String sql = "SELECT id FROM voter_count WHERE name='Исаичев Эмилиан'";
        ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
        if(!rs.next()) {
            return -1;
        }
        else {
            return rs.getInt("id");
        }
    }

    public static void countVoter(String name, String birthDay) throws SQLException {
        birthDay = birthDay.replace('.', '-');

        if (stringBuilder.length() == 0) {
            stringBuilder.append("('" + name + "', '" + birthDay + "', 1)");
        }
        stringBuilder.append(", ('" + name + "', '" + birthDay + "', 1)");
        if (stringBuilder.length() > 10000000) {
            executeMultiInsert();
        }
    }

    public static void printVoterCounts() throws SQLException
    {
        String sql = "SELECT name, birthDate, count(id) FROM voter_count GROUP BY name, birthDate HAVING COUNT(id) > 1;";
        ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
        while(rs.next())
        {
            System.out.println("\t" + rs.getString("name") + " (" +
                    rs.getString("birthDate") + ") - " + rs.getInt("count"));
        }
    }
}
