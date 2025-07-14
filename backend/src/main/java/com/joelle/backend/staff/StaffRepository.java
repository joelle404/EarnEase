package com.joelle.backend.staff;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class StaffRepository {
    




    // private final DataSource dataSource;

    // public StaffRepository(DataSource dataSource) {
    //     this.dataSource = dataSource;
    // }

    // public List<Staff> findAll() {
    //     List<Staff> staffList = new ArrayList<>();
    //     String query = "SELECT * FROM staff";

    //     try (Connection conn = dataSource.getConnection();
    //          PreparedStatement stmt = conn.prepareStatement(query);
    //          ResultSet rs = stmt.executeQuery()) {

    //         while (rs.next()) {
    //             Staff staff = new Staff();
    //             staff.setId(rs.getLong("id"));
    //             staff.setName(rs.getString("name"));
    //             staff.setRole(rs.getString("role"));
    //             staffList.add(staff);
    //         }

    //     } catch (SQLException e) {
    //         e.printStackTrace(); // or use a custom exception
    //     }

    //     return staffList;
    // }

    // // public void save(Staff staff) {
    //     String query = "INSERT INTO staff (name, role, income) VALUES (?, ?, ?)";

    //     try (Connection conn = dataSource.getConnection();
    //          PreparedStatement stmt = conn.prepareStatement(query)) {

    //         stmt.setString(1, staff.getName());
    //         stmt.setString(2, staff.getRole());
    //         stmt.setDouble(3, staff.getIncome());

    //         stmt.executeUpdate();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
}
