package InternalModels;

public class SupervisorQueueItem extends QueueItem{

    private QueueItem client;
    private SupervisorState.Status status = SupervisorState.Status.pending;
    private String supervisorMessage;

    //TODO better way of doing this
    private SupervisorQueue queue;

    public SupervisorQueueItem(String name, String clientID, SupervisorQueue queue) {
        super(name, clientID, queue);

    }


    public QueueItem getClient() {
        return client;
    }

    public void setClient(QueueItem client) {
        this.client = client;
        queue.setQueueChanged(true);
    }

    public SupervisorState.Status getStatus() {
        return status;
    }

    public void setStatus(SupervisorState.Status status) {
        this.status = status;
        queue.setQueueChanged(true);

        if(status == SupervisorState.Status.available)
            queue.available.add(getName());
        else
            queue.available.remove(getName());
    }

    public String getSupervisorMessage() {
        return supervisorMessage;
    }

    public void setSupervisorMessage(String supervisorMessage) {
        this.supervisorMessage = supervisorMessage;
    }

}
