public class UserDaoFactory {
    private UserDaoFactory(){}
    private static UserDao userDao;
    public static UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDaoImpl();
        }
        return userDao;
    }
}
