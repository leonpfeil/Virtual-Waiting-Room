package CommunicationModels.QueueTicket;

public class ClientQueueTicket extends QueueTicket{
    Integer ticket;

    public ClientQueueTicket(Integer ticket, String name)
    {
        this.ticket = ticket;
        this.name = name;
    }
}
