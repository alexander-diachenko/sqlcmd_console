package sqlcmd.command;

import java.sql.*;

public class List {

    public String doList(Connection connection){

        if (connection != null) {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet resultSet1 = metaData.getTables(null, "public", "%", new String[]{"TABLE"});
                String tables = "[";
                while (resultSet1.next()) {
                    tables += resultSet1.getString(3) + ", ";
                }
                String result;
                result = tables.substring(0, tables.length() - 2);
                result += "]";

                System.out.println(result);

                resultSet1.close();
                return result;

            } catch (SQLException e) {
e.printStackTrace();
            }
        }
        return null;
    }
}