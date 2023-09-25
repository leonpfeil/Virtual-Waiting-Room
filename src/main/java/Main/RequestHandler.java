package Main;

import CommunicationModels.ClientMessage;
import CommunicationModels.QueueTicket;
import InternalModels.Queue;
import com.google.gson.*;

public class RequestHandler {
    Queue queue;

    public RequestHandler(Queue queue)
    {
        this.queue = queue;
    }
    public String handleRequest(String incomingRequestString)
    {
        Gson gson = new Gson();

        ClientMessage message = gson.fromJson(incomingRequestString,ClientMessage.class);

        QueueTicket replyTicket;
        String replyString;


        if(message.getEnterQueue())
        {
            joinQueue(message);

            //create CommunicationObjects.QueueTicket item for the response
            int index = queue.getCurrentPositionInQueue(message.getName());
            replyTicket = new QueueTicket(index,message.getName());
            replyString = gson.toJson(replyTicket);
        }
        else
        {
            handleHeartBeat(message);
            //empty json
            replyString = "{}";

        }




        return replyString;
    }

    void joinQueue(ClientMessage message)
    {
        if(!queue.isNameInQueue(message.getName()))
        {
            //If client wants to join and isnt already in the queue (via another client) add them to the list
            queue.addNewUserToQueue(message.getName(),message.getClientID(),queue);
        }
        else
        {
            queue.addIDToExistingUser(message.getName(), message.getClientID());

        }
    }


    void handleHeartBeat(ClientMessage message)
    {
        System.out.println(queue.toString());
        queue.startTimer(message.getName(), message.getClientID());
    }
}
