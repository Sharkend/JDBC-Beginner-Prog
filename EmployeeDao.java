import java.sql.SQLException;

/*
Employee Table Schema:
empID int auto_increment Primary Key
empName String
password String
 */
public interface EmployeeDao {
    void viewAccount(int userID) throws SQLException;
    void viewHistory(int lim) throws SQLException;//not void
    void approve() throws SQLException;
    boolean login(int empID, String password) throws SQLException;
}
