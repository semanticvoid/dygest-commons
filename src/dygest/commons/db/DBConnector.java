/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dygest.commons.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author anand
 */
public class DBConnector {

    public DBConnector() {
        
    }

    public Connection connect() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/informd";
            Connection con = DriverManager.getConnection(url, "root", "");
            return con;
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String escape(String str) {
        return str.replace("'", "\\'");
    }

}
