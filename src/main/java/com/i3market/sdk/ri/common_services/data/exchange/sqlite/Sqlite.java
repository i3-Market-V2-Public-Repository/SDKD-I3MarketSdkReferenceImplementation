package com.i3market.sdk.ri.common_services.data.exchange.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class Sqlite {

    private static double BlockPrice = 0.156;
    // dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Connection connect() {

        String db_name = "consumer.db";
        Connection conn = null;
        String url = "jdbc:sqlite:" + db_name;

        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createTable(Connection conn) {

        String sql = "CREATE TABLE IF NOT EXISTS accounting(Date TEXT, ProviderID TEXT, BlockID TEXT, PoO TEXT, PoR TEXT, PoP TEXT PRIMARY KEY)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String Date, String ProviderID, String BlockID, String PoO, String PoR, String PoP, Connection conn) {

        String sql = "INSERT INTO accounting(Date, ProviderID, BlockID, PoO, PoR, PoP) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Date);
            pstmt.setString(2, ProviderID);
            pstmt.setString(3, BlockID);
            pstmt.setString(4, PoO);
            pstmt.setString(5, PoR);
            pstmt.setString(6, PoP);
            pstmt.executeUpdate();
            System.out.println("Data inserted in database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public JSONObject countBlocks(Connection conn, String fromDate, String toDate, Double priceFromApi) {

        JSONObject price = new JSONObject();
        String sql = "SELECT * FROM accounting where Date >= " + fromDate + " AND Date <= " + toDate;

        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            price = this.comparePrice(rs, priceFromApi);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return price;
    }

    // Check if price from dataAccess matches the price from dataAccessClient -----> To be changed
    private JSONObject comparePrice(ResultSet rs, Double priceFromApi) throws SQLException {
        int size = 0;
        JSONObject response = new JSONObject();
        
        if (rs != null) {
            rs.last(); 
            size = rs.getRow();  
        }
        double totalPrice = size * BlockPrice;
        if (totalPrice == priceFromApi){
            response.put("response", totalPrice);
        }  else{
            response.put("response", "The total BlockPrice from consumer, " + totalPrice + " , doesn\'t match the total BlockPrice from provider, " + priceFromApi);
        }
        return response;
    }
}
