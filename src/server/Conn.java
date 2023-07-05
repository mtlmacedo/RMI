package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Conn {
    public Statement stm;
    public ResultSet rs;
    private String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String path, user, password, database;
    private Connection connection;

    public Connection Connect(String path, String user, String password, String dataBase) {
        this.path = path;
        this.user = user;
        this.password = password;
        this.database = dataBase;

        try {
            System.setProperty("jdbc.Drivers", driver);
            connection= DriverManager.getConnection(this.path + ";" + 
                                                    "databaseName=" + this.database + ";" + 
                                                    "user=" + this.user + ";" + 
                                                    "password=" + this.password + ";" + 
                                                    "encrypt=false");
            System.out.println("Conectou");
            return connection ;
        } catch (Exception e) {
            System.out.println("Falha ao conectar banco");
        }
        return connection;
    }
}