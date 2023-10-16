import java.sql.*;
import java.util.Scanner;

public class EmployeeDaoImpl implements EmployeeDao{
    Connection connection;

    public EmployeeDaoImpl(){
        connection = ConnectionFactory.getConnection();
    }

    @Override
    public void viewAccount(int userID) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String sql = "SELECT * FROM user WHERE userID = "+ userID;
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            System.out.println("Name: "+rs.getString("userName")+ "\nBalance: "+ rs.getInt("balance"));
        else System.out.println("Account does not exist");
    }

    @Override
    public void viewHistory(int lim) throws SQLException {
        String sql = "SELECT * FROM History ORDER BY ID DESC LIMIT ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, lim);
        ResultSet rs = ps.executeQuery();

        System.out.println("Remitter\tBeneficiary\tAmount");
        while(rs.next()) {
            System.out.println("---------------------------------------------------------");
            int r = rs.getInt("remitter");
            int b = rs.getInt("beneficiary");
            int a = rs.getInt("amount");
            System.out.println(r + "\t" + b + "\t" + a);
        }
    }

    @Override
    public void approve() throws SQLException {
        String sql = "SELECT * FROM User WHERE active = 0",
                sqlY = "UPDATE User SET active = 1 WHERE userID = ",
                sqlN = "DELETE FROM User WHERE userID = ";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            String name = rs.getString("userName");
            int bal = rs.getInt("balance");
            String uid = rs.getString("userID");

            System.out.println(name+"\nRs. "+bal);
            System.out.println("Approve (Y/N), STOP, or defer");
            Scanner scanner = new Scanner(System.in);
            switch(scanner.next()) {
                case "Y": case "y":
                    PreparedStatement psy = connection.prepareStatement(sqlY + uid);
                    int temp = psy.executeUpdate();
                    System.out.println("APPROVED");
                    break;
                case "N": case "n": //DRY not worth
                    PreparedStatement psn = connection.prepareStatement(sqlN + uid);
                    int tmp = psn.executeUpdate();
                    System.out.println("REJECTED");
                    break;
                case "STOP": case "stop": return;
                default:
                    System.out.println("DEFERRED");
            }
        }
        System.out.println("\n Action Complete. \n");
    }

    @Override
    public boolean login(int empID, String password) throws SQLException {
        String sql = "SELECT * FROM employee WHERE empID = ? AND password = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, empID);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
