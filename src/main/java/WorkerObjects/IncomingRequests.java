package WorkerObjects;

import CommunicationObjects.ClientMessage;
import CommunicationObjects.QueueTicket;
import FunctionalObjects.Queue;
import com.google.gson.*;

public class IncomingRequests {
    String name;
    String clientID;
    ClientMessage message;
    Queue queue;

    Gson gson = new Gson();

    public IncomingRequests(String incomingRequestString, Queue queue)
    {
        System.out.println("JSON:");
        System.out.println(incomingRequestString);

        message = gson.fromJson(incomingRequestString,ClientMessage.class);
        name = message.getName();
        clientID = message.getClientID();

        System.out.println("Object:");
        System.out.println(message.getName() + message.getClientID() + message.getEnterQueue());

        this.queue = queue;
    }
    public void handleNewRequest()
    {

        QueueTicket replyTicket;
        String replyString;

        if(message.getEnterQueue())
        {
            joinQueue();

            //create CommunicationObjects.QueueTicket item for the response
            int index = queue.getCurrentPositionInQueue(name);
            replyTicket = new QueueTicket(index,name);
            replyString = gson.toJson(replyTicket);
        }
        else
        {
            //TODO figure out reply format
            handleHeartBeat();
            //empty json
            replyString = "{}";

        }




        ServerMain.reply.send(replyString);
    }

    void joinQueue()
    {
        boolean changed = false;
        if(!queue.isNameInQueue(name))
        {
            //If client wants to join and isnt already in the queue (via another client) add them to the list
            queue.addNewUserToQueue(name,clientID,queue);
            changed = true;
        }
        else
        {
            changed = queue.addIDToExistingUser(name,clientID);

        }
        queue.setQueueChanged(changed);
    }


    void handleHeartBeat()
    {
        queue.startTimer(name,clientID);
    }
}
