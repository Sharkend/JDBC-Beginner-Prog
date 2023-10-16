import java.sql.SQLException;

/*
User Table Schema:
int userID auto_increment Primary Key
int balance
varchar30 userName
varchar30 password
boolean active
 */
public interface UserDao {
    int checkBalance(int userID) throws SQLException;
    void withdraw(int userID, int amount) throws SQLException;
    boolean deposit(int userID, int amount) throws SQLException;
    boolean makePayment(int userID, int beneficiaryID, int amount) throws SQLException;
    int register(String userName, String password) throws SQLException;
    boolean login(int userID, String password) throws SQLException;
}
