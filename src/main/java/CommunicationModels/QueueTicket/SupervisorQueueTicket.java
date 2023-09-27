package CommunicationModels.QueueTicket;

import InternalModels.SupervisorState;

public class SupervisorQueueTicket extends QueueTicket{
    SupervisorState.Status status;
    ClientQueueTicket client;

    public SupervisorQueueTicket(String name, SupervisorState.Status status, ClientQueueTicket client) {
        this.name = name;
        this.status = status;
        this.client = client;
    }
}
