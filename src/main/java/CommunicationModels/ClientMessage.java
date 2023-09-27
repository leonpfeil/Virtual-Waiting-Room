package CommunicationModels;

import InternalModels.SupervisorState;

public class ClientMessage {

    boolean enterQueue;
    String name;

    String clientId;


    //Supervisor-exclusive information
    boolean supervisor;
    String optionalMessage;
    SupervisorState.Status status;

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

    public boolean isEnterQueue() {
        return enterQueue;
    }

    public void setEnterQueue(boolean enterQueue) {
        this.enterQueue = enterQueue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setSupervisor(boolean supervisor) {
        this.supervisor = supervisor;
    }

    public String getOptionalMessage() {
        return optionalMessage;
    }

    public void setOptionalMessage(String optionalMessage) {
        this.optionalMessage = optionalMessage;
    }

    public SupervisorState.Status getStatus() {
        return status;
    }

    public void setStatus(SupervisorState.Status status) {
        this.status = status;
    }
}
