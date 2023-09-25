package CommunicationModels;

public class ClientMessage {

    boolean enterQueue;

    String name;

    String clientId;

    public ClientMessage(boolean enterQueue,String name,String clientId)
    {
        this.enterQueue = enterQueue;
        this.name = name;
        this.clientId = clientId;
    }

    public ClientMessage(String name,String clientId)
    {
        this.name = name;
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public boolean getEnterQueue() {
        return enterQueue;
    }

    public String getClientID() {
        return clientId;
    }
}
