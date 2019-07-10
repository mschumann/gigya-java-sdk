public interface IRequestDispatcher {

    String getPublicKey();

    String getAccountInfo(String uid);

    String register(String email, String password, int exp);

    String login(String loginId, String password);

}
