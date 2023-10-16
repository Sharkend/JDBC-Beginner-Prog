import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        //init
        EmployeeDao empDao = EmployeeDaoFactory.getEmployeeDao();
        UserDao userDao = UserDaoFactory.getUserDao();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome! \n 1. Customer \n 2. Employee \n 3. Exit");
            int userType = scanner.nextInt();
            //Customer
            if (userType == 1) {
                System.out.println("\n1. Register\n2. Login");
                int choice = scanner.nextInt();
                if (choice == 1) { //register
                    System.out.println("Enter name and password:");
                    int userID = userDao.register(scanner.next(), scanner.next());
                    System.out.println("Registration awaiting approval...\nUser ID: "+userID);
                } else if (choice == 2) { //login
                    System.out.println("Enter userID and password");
                    int userID = scanner.nextInt();
                    String password = scanner.next();
                    if (userDao.login(userID, password)) userLoggedIn(userID);
                    else System.out.println("Failed to login");
                }
                //Employee
            } else if (userType == 2) {
                System.out.println("Enter Employee ID and password:");
                int empID = scanner.nextInt();
                String password = scanner.next();
                if (empDao.login(empID, password)) empLoggedIn(empID);
                else System.out.println("Failed to login.");
                //Exit
            } else {
                System.out.println("\n Thank you.");
                return;
            }
        }
    }

    private static void empLoggedIn(int empID) throws SQLException {
        EmployeeDao empDao = EmployeeDaoFactory.getEmployeeDao();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome Employee.");

        //console for employee
        while(true) {
            System.out.println("""
                1. View Account
                2. Approve Registrations
                3. View Transactions
                """
            );
            int choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("Enter account number");
                empDao.viewAccount(scanner.nextInt());
            }
            else if (choice == 2) empDao.approve();
            else if (choice == 3) {
                System.out.print("Enter Size:");
                empDao.viewHistory(scanner.nextInt());
            }
            else {
                System.out.println("\n Logged out successfully.\n");
                return;
            }
        }
    }

    private static void userLoggedIn(int userID) throws SQLException {
        UserDao userDao = UserDaoFactory.getUserDao();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome User.");

        //console for user
        while(true) {
            System.out.println("""
                1. Check Balance
                2. Withdraw
                3. Deposit
                4. Transfer Money
                5. Logout
                """
            );
            int choice = scanner.nextInt();
            if (choice == 1) System.out.println("Balance : "+userDao.checkBalance(userID));
            else if (choice == 2) {
                System.out.print("Enter amount to withdraw: ");
                userDao.withdraw(userID, scanner.nextInt());
            } else if (choice == 3) {
                //proof of concept, funds are assumed to be deposited via unknown means
                System.out.print("Enter amount to deposit: ");
                userDao.deposit(userID, scanner.nextInt());
            } else if (choice == 4) {
                System.out.print("Enter Beneficiary ID: ");
                int bID = scanner.nextInt();
                System.out.print("Enter Amount: ");
                int amount = scanner.nextInt();
                if (userDao.makePayment(userID, bID, amount))
                    System.out.println("Payment Successful");
                else System.out.println("Payment Failed");
            } else {
                System.out.println("\n Logged out successfully.\n");
                return;
            }
        }
    }
}