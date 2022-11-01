package ai.cresta.ports.api;

public interface MessageServicePort {

    public void notifyFrontend(final String messages);

    public void notifyUser(final String id, final String messages);

    public void setAccessToken(String userId, String accessToken);
}
