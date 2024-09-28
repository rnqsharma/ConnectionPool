package org.example;

import java.sql.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    private static final BlockingQueue<Connection> connectionPool =
            new ArrayBlockingQueue<>(10);

    public static void main(String[] args) {
        try {
            if (connectionPool.isEmpty()) {
                for(int i = 0; i<10; i++) {
                    connectionPool.add(createConnection());
                }
            }

            for(int i =0; i<1000; i++) {
                DbThread thread = new DbThread();
                Thread dd = new Thread(thread);
                dd.start();
                System.out.println("Starting thread....");
                dd.join(10);
            }

        } catch (Exception e) {
            System.out.println("Exception occurred = " + e.getMessage());
        }
    }


    public static Connection getConnection() throws InterruptedException {
        return connectionPool.take();
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sys","root","password123");
    }

    public static void printRows() throws SQLException, ClassNotFoundException, InterruptedException {
        Connection con = getConnection();
        Statement stmt= con.createStatement();
        ResultSet rs=stmt.executeQuery("select * from demopool");
        while(rs.next())
            System.out.println("Value ---> " + rs.getInt(1));
        System.out.println("Sleeping.....");
        ResultSet rs2=stmt.executeQuery("SELECT SLEEP(1)");
        connectionPool.add(con);
    }
}