package Main;

import CommunicationModels.*;
import CommunicationModels.QueueTicket.ClientQueueTicket;
import CommunicationModels.QueueTicket.QueueTicket;
import InternalModels.Queue;
import com.google.gson.Gson;
import org.zeromq.ZMQ;

class Broadcasts {

    private ZMQ.Socket publisher;
    private Queue clientQueue;
    private Queue supervisorQueue;

    private Gson gson = new Gson();

    public Broadcasts(ZMQ.Socket socket, Queue clientQueue,Queue supervisorQueue) {
        publisher = socket;
        this.clientQueue = clientQueue;
        this.supervisorQueue = supervisorQueue;
    }

    public void publishQueueChanges(boolean isSupervisor) {

        Queue queue;
        QueueTicket[] ticketArray;
        if(isSupervisor)
        {
            queue = supervisorQueue;
            ticketArray = supervisorQueue.createOrderedSupervisorQueueTicketArray(clientQueue);
        }
        else
        {
            queue = clientQueue;
            ticketArray = clientQueue.createOrderedClientQueueTicketArray();
        }

        String arrayAsJSON = gson.toJson(ticketArray);
        System.out.println(arrayAsJSON);

        publisher.sendMore("queue");
        publisher.send(arrayAsJSON, 0);

        queue.setQueueChanged(false);
        System.out.println("-published queue changes-");
    }

    /**
     * Send a message to the specified User
     * @param acceptedUserName The name of the user that should receive the message
     * @param message The Supervisor's message to the client
     *
     */
    public void publishSupervisorMessage(String acceptedUserName,String supervisorName, String message)
    {
        SupervisorMessage supervisorMessage = new SupervisorMessage(supervisorName,message);
        publisher.sendMore(acceptedUserName);
        publisher.send(gson.toJson(supervisorMessage),0);

        informSupervisorOfClient(acceptedUserName,supervisorName); //after informing the client of the supervisor we next have to inform the supervisor which client they will receive
    }

    /**
     * Send a message to all Users
     * @param message The Supervisor's message to the client
     */
    public void publishSupervisorMessage(String supervisorName,String message)
    {
        SupervisorMessage supervisorMessage = new SupervisorMessage(supervisorName,message);
        publisher.sendMore("supervisorBroadcast");
        publisher.send(gson.toJson(supervisorMessage),0);
    }

    private void informSupervisorOfClient(String acceptedUserName,String supervisorName)
    {
        QueueTicket ticket = new ClientQueueTicket(0,acceptedUserName);
        publisher.sendMore(supervisorName);
        publisher.send(gson.toJson(ticket));

    }
}
