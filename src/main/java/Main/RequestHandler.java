package Main;

import CommunicationModels.ClientMessage;
import CommunicationModels.ErrorMessage;
import CommunicationModels.QueueTicket.ClientQueueTicket;
import CommunicationModels.QueueTicket.QueueTicket;
import CommunicationModels.QueueTicket.SupervisorLoginStatus;
import CommunicationModels.QueueTicket.SupervisorStatus;
import InternalModels.Queue;
import InternalModels.QueueItem;
import InternalModels.SupervisorState.Status;
import com.google.gson.Gson;

public class RequestHandler {
    private Queue clientQueue;
    private Queue supervisorQueue;
    Broadcasts broadcast;
    private Gson gson = new Gson();

    int clientIndex = 1;
    int supervisorIndex = 1;

    public RequestHandler(Broadcasts broadcast, Queue clientQueue, Queue supervisorQueue) {
        this.clientQueue = clientQueue;
        this.supervisorQueue = supervisorQueue;
        this.broadcast = broadcast;
    }

    public String handleRequest(String incomingRequestString) {

        ClientMessage message = gson.fromJson(incomingRequestString, ClientMessage.class);

        //if the message is from a supervisor work on the supervisor queue, otherwise use the clientqueue
        //many functions between supervisor and client are shared so it is easier to just swap out the queue reference
        if (message.isSupervisor()) {
            return processRequest(message, supervisorQueue);
        }
        return processRequest(message, clientQueue);

    }

    private String processRequest(ClientMessage message, Queue queue) {


        QueueTicket replyTicket;
        String replyString;


        if (message.getEnterQueue()) {

            boolean joinSuccessful = joinQueue(message, queue);

            if (!joinSuccessful) {
                ErrorMessage error = new ErrorMessage("Invalid name", "The chosen name is invalid");
                return gson.toJson(error);
            }

            if (message.isSupervisor()) //supervisor get a ticket with just their name. Technically redundant information but the client needs it for synchronisation
            {
                replyTicket = new SupervisorLoginStatus(true, message.getName());
            } else //Clients get a ticket with their position in the queue
            {
                //create QueueTicket item for the response
                try {
                    replyTicket = new ClientQueueTicket(queue.getQueueItems().get(message.getName()).getIndex(), message.getName());
                } catch (Exception e) {
                    replyTicket = new ClientQueueTicket(clientIndex - 1, message.getName());
                }

            }
            replyString = gson.toJson(replyTicket);

        } else if (message.isSupervisor() && (message.getStatus() != null || message.getOptionalMessage() != null)) { //if supervisor but not login = either a statuschange or a message broadcast
            replyString = handleSupervisorRequest(message);

        } else {
            handleHeartBeat(message, queue);
            //empty json
            replyString = "{}";
        }


        return replyString;
    }

    /**
     * Adds either a new user or a new client for that corresponding user to the queue
     *
     * @param message
     * @param queue
     * @return false if the name is invalid, otherwise true
     */
    boolean joinQueue(ClientMessage message, Queue queue) {
        //First check for invalid name
        if (message.getName().equals("queue") || message.getName().equals("supervisors") || message.getName().equals("supervisorBroadcast")) {
            return false;
        }

        if (!queue.isNameInQueue(message.getName())) {
            //If client wants to join and isnt already in the queue (via another client) add them to the list
            if (message.isSupervisor()) {
                queue.addNewUserToQueue(message.getName(), message.getClientID(), queue, supervisorIndex);
                supervisorIndex++;
            } else {
                queue.addNewUserToQueue(message.getName(), message.getClientID(), queue, clientIndex);
                clientIndex++;
            }


            //if the client is a user and there are available supervisor assign this client to a supervisor
            if (!message.isSupervisor() && !supervisorQueue.getAvailableSupervisor().isEmpty()) {
                assignClientToSupervisor(message.getName());
            }
        } else {
            queue.addIDToExistingUser(message.getName(), message.getClientID());

        }
        return true;
    }


    void handleHeartBeat(ClientMessage message, Queue queue) {
        System.out.println(queue.toString());
        queue.startTimer(message.getName(), message.getClientID());


    }

    //
    private String handleSupervisorRequest(ClientMessage message) {
        String reply;
        if (message.getStatus() != null) {
            //supervisor wants to update status
            Status status = processStatusChange(message);
            SupervisorStatus supStatus = new SupervisorStatus(status);
            reply = gson.toJson(supStatus);
        } else {
            //supervisor wants to send broadcast without taking on a new client
            broadcast.publishSupervisorMessage(message.getName(), message.getOptionalMessage());
            reply = "{}";
        }
        return reply;
    }

    //Even though there are 3 possible states, the client can only send available or pending.
    //Therefor its assumed that if it is not pending it has to be available
    private Status processStatusChange(ClientMessage message) {
        if (message.getStatus() == Status.pending) {
            supervisorQueue.getQueueItems().get(message.getName()).setStatus(Status.pending);
            return Status.pending;
        } else if (!clientQueue.getPositionInQueue().isEmpty()) //if there are clients waiting in queue immediately send them to supervisor and set occupied
        {
            QueueItem acceptedClient = clientQueue.acceptClient();
            supervisorQueue.getQueueItems().get(message.getName()).setStatus(Status.occupied);
            supervisorQueue.getQueueItems().get(message.getName()).setClient(acceptedClient);
            broadcast.publishSupervisorMessage(acceptedClient.getName(), message.getName(), message.getOptionalMessage(), acceptedClient.getIndex());
            return Status.occupied;
        } else {
            supervisorQueue.getQueueItems().get(message.getName()).setStatus(Status.available);
            supervisorQueue.getQueueItems().get(message.getName()).setSupervisorMessage(message.getOptionalMessage());
            return Status.available;
        }
    }

    private void assignClientToSupervisor(String clientName) {
        QueueItem client = clientQueue.acceptClient(clientName);
        String supervisorName = supervisorQueue.removeSupervisorFromAvailable();
        QueueItem supervisorItem = supervisorQueue.getQueueItems().get(supervisorName);
        supervisorItem.setClient(client);
        supervisorQueue.getQueueItems().get(supervisorName).setStatus(Status.occupied);

        broadcast.publishSupervisorMessage(clientName, supervisorName, supervisorItem.getSupervisorMessage(), client.getIndex());


    }

}
