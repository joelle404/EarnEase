package com.joelle.backend.staff;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
 
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long>{

    Optional<Staff> findByEmail(String  email);


}
