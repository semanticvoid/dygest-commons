/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dygest.commons.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author anand
 */
public class DBConnector {

    private static DBConnector dbconn = null;

    private Connection con;

    private DBConnector() {
    }

    public static DBConnector getSingleton() {
        if(dbconn != null) {
            dbconn = new DBConnector();
        }

        return dbconn;
    }

    private Connection connect() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/informd";
            con = DriverManager.getConnection(url, "root", "");
            return con;
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int executeUpdate(String sql) throws Exception {
        Statement stmt = this.con.createStatement();
        return stmt.executeUpdate(sql);
    }

    public ResultSet executeSelect(String sql) throws Exception {
        Statement stmt = this.con.createStatement();
        return stmt.executeQuery(sql);
    }

    public Connection getConnection() {
        return this.con;
    }

    public static String escape(String str) {
        return str.replace("'", "\\'");
    }

}
