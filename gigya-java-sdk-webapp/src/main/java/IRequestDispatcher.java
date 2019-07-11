public interface IRequestDispatcher {

    String register(String email, String password, int exp, String profile);

    String login(String loginId, String password);

    String getAccountInfo(String uid);

    String verifyLogin(String uid);

    String isAvailableLoginId(String loginId);

    String getJWTPublicKey();

}
