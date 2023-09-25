package Main;

import CommunicationModels.ClientMessage;
import CommunicationModels.QueueTicket;
import InternalModels.Queue;
import com.google.gson.Gson;

public class RequestHandler {
    Queue clientQueue;
    Queue supervisorQueue;
    Gson gson = new Gson();

    public RequestHandler(Queue clientQueue, Queue supervisorQueue) {
        this.clientQueue = clientQueue;
        this.supervisorQueue = supervisorQueue;
    }

    public String handleRequest(String incomingRequestString) {

        ClientMessage message = gson.fromJson(incomingRequestString, ClientMessage.class);

        //if the message is from a supervisor work on the supervisor queue, otherwise use the clientqueue
        if (message.isSupervisor()) {
            return processRequest(message, supervisorQueue);
        }
        return processRequest(message, clientQueue);

    }

    private String processRequest(ClientMessage message, Queue queue) {


        QueueTicket replyTicket;
        String replyString;


        if (message.getEnterQueue()) {

            joinQueue(message, queue);

            //create QueueTicket item for the response
            int index = queue.getCurrentPositionInQueue(message.getName());
            replyTicket = new QueueTicket(index, message.getName());
            replyString = gson.toJson(replyTicket);
        } else {
            handleHeartBeat(message, queue);
            //empty json
            replyString = "{}";

        }


        return replyString;
    }

    void joinQueue(ClientMessage message, Queue queue) {
        if (!queue.isNameInQueue(message.getName())) {
            //If client wants to join and isnt already in the queue (via another client) add them to the list
            queue.addNewUserToQueue(message.getName(), message.getClientID(), queue);
        } else {
            queue.addIDToExistingUser(message.getName(), message.getClientID());

        }
    }


    void handleHeartBeat(ClientMessage message, Queue queue) {
        System.out.println(queue.toString());
        queue.startTimer(message.getName(), message.getClientID());


    }
}
