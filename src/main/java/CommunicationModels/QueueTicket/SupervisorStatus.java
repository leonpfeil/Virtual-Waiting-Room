package CommunicationModels.QueueTicket;

import InternalModels.SupervisorState;

public class SupervisorStatus {
    SupervisorState.Status status;

    public SupervisorStatus(SupervisorState.Status status) {
        this.status = status;
    }
}
