package CommunicationModels.QueueTicket;

public class SupervisorLoginStatus extends QueueTicket{
    boolean login;

    public SupervisorLoginStatus(boolean login,String name) {
        this.login = login;
        this.name = name;
    }
}
