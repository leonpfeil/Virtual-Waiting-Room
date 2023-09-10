public class ClientMessage {
    boolean enterQueue;
    String name;
    String clientID;

    public ClientMessage(boolean enterQueue,String name,String clientID)
    {
        this.enterQueue = enterQueue;
        this.name = name;
        this.clientID = clientID;
    }

    public ClientMessage(String name,String clientID)
    {
        this.name = name;
        this.clientID = clientID;
    }
}
