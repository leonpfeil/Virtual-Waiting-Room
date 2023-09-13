package CommunicationObjects;

public class ClientMessage {
    public boolean enterQueue;
    public String name;
    public String clientID;

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
