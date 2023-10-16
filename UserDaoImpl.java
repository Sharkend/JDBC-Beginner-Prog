import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class UserDaoImpl implements UserDao{
    Connection connection;

    public UserDaoImpl(){
        connection = ConnectionFactory.getConnection();
    }

    private void updateBal(int userID, int amount) throws SQLException {
        String sql = "UPDATE user SET balance = (? + ?) WHERE userID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, checkBalance(userID));
        ps.setInt(2, amount);
        ps.setInt(3, userID);
        int count = ps.executeUpdate(); // TODO handle > 0 for success
    }
    @Override
    public int checkBalance(int userID) throws SQLException {
        //TODO CHECK ACTIVE ACCOUNT
        String sql = "SELECT balance FROM user WHERE userID = "+userID;
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("balance") : -1;
    }

    @Override
    public void withdraw(int userID, int amount) throws SQLException {
        int bal = checkBalance(userID);
        if (amount > bal || amount <= 0) {
            System.out.println("FAILED! Please enter an amount from 0 to "+bal+"\n");
            return;
        }
        updateBal(userID, -amount);
    }

    @Override
    public boolean deposit(int userID, int amount) throws SQLException {
        if (amount <= 0) return false;
        updateBal(userID, amount);
        return true;
    }

    @Override
    public boolean makePayment(int userID, int beneficiaryID, int amount) throws SQLException {
        String sql = "SELECT * FROM user WHERE userID = "+userID;
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) return false;
        int userBal = rs.getInt("balance");
        if (userBal < amount) return false;

        sql = "SELECT * FROM user WHERE userID = "+beneficiaryID;
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        if (!rs.next()) return false;


        withdraw(userID, amount);
        boolean valid = deposit(beneficiaryID, amount);
        if (valid) {
            sql = "INSERT INTO History (remitter, beneficiary, amount) VALUES (?, ?, ?)";
            PreparedStatement ps3 = connection.prepareStatement(sql);
            ps3.setInt(1, userID);
            ps3.setInt(2, beneficiaryID);
            ps3.setInt(3, amount);
            ps3.executeUpdate();
        }
        return valid;
    }

    @Override
    public int register(String userName, String password) throws SQLException {
        //TODO add starting balance
        //Input validation
        while (Objects.equals(userName, "") || Objects.equals(password, "")) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Re-enter Name and Password");
            userName = scanner.next(); password = scanner.next();
        }

        //Register
        String sql = "INSERT INTO User (userName, password) VALUES (? ,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ps.executeUpdate();

        sql = "SELECT * FROM user WHERE userID = (SELECT MAX(userID) FROM User)";
        PreparedStatement ps2 = connection.prepareStatement(sql);
        ResultSet rs = ps2.executeQuery();

        if(rs.next()) return rs.getInt("userID");
        return -1;
    }

    @Override
    public boolean login(int userID, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE userID = ? AND password = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        return rs.next() && rs.getBoolean("active");
    }
}
