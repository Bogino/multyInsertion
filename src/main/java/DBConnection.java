import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

public class DBConnection
{
    private static Connection connection;

    private static String dbName = "learn";
    private static String dbUser = "root";
    private static String dbPass = "testtest";
    //private static StringBuilder insertQuery = new StringBuilder();
    private static String sql;
    private static String values = new String();
    private static StringBuilder stringBuilder = new StringBuilder();
    private static boolean isBlock = false;

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
                        "PRIMARY KEY(id), KEY(NAME(50)))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void executeMultiInsert() throws SQLException{

        sql = "INSERT INTO voter_count(name, birthDate, 'count') " +
                "VALUES" + values;

        DBConnection.getConnection().createStatement().execute(sql);

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

    public static void countVoter(String name, String birthDay) throws SQLException
    {
        birthDay = birthDay.replace('.','-');

//
//        System.out.println(name);
//        values += values.length() == 0 ? "" : ",";
//        values += "('" + name + "', '" + birthDay + "', 1)";
        if (!isBlock){
            stringBuilder.append("('" + name + "', '" + birthDay + "', 1)");
        }
        isBlock = true;
        stringBuilder.append("," + " ('" + name + "', '" + birthDay + "', 1)");
        if (stringBuilder.length() > 1000){
            stringBuilder.append("," + " ('" + name + "', '" + birthDay + "', 1)");
            values += stringBuilder.toString();
            stringBuilder = new StringBuilder();
        }



//        String sql = "SELECT id FROM voter_count WHERE birthDate='" + birthDay + "' AND name='" + name + "'";
//        ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
//        if(!rs.next())
//        {
//            DBConnection.getConnection().createStatement()
//                    .execute("INSERT INTO voter_count(name, birthDate, `count`) VALUES('" +
//                            name + "', '" + birthDay + "', 1)");
//        }
//        else {
//            Integer id = rs.getInt("id");
//            DBConnection.getConnection().createStatement()
//                    .execute("UPDATE voter_count SET `count`=`count`+1 WHERE id=" + id);
//        }
//      rs.close();
    }

    public static void printVoterCounts() throws SQLException
    {
        String sql = "SELECT name, birthDate, count(count) FROM voter_count GROUP BY name";
        ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
        while(rs.next())
        {
            System.out.println("\t" + rs.getString("name") + " (" +
                    rs.getString("birthDate") + ") - " + rs.getInt("count"));
        }
    }
}
