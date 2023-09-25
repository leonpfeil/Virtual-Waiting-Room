package CommunicationModels;

import InternalModels.SupervisorState;

public class ClientMessage {

    boolean enterQueue;
    String name;

    String clientId;


    //Supervisorexclusive information
    boolean supervisor;
    String optionalMessage;
    SupervisorState.switchTo switchTo;

    public ClientMessage(boolean enterQueue, String name, String clientId) {
        this.enterQueue = enterQueue;
        this.name = name;
        this.clientId = clientId;
    }

    public ClientMessage(String name, String clientId) {
        this.name = name;
        this.clientId = clientId;
    }

    public boolean isSupervisor() {
        return supervisor;
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
