import CommunicationObjects.ClientMessage;
import CommunicationObjects.QueueItem;
import CommunicationObjects.QueueTicket;
import com.google.gson.*;

public class IncomingRequests {
    static String name;
    static ClientMessage message;
    public static void handleNewRequest(String incomingRequest)
    {
        Gson gson = new Gson();
        message = gson.fromJson(incomingRequest,ClientMessage.class);
        name = message.name;
        System.out.println(message.name + message.clientID + message.enterQueue);

        if(message.enterQueue)
        {
            joinQueue();
        }
        else
        {
            //TODO
            //handleHeartBeat();
        }

        //create CommunicationObjects.QueueTicket item for the response
        int index = ServerMain.positionInQueue.indexOf(name);
        QueueTicket replyTicket = new QueueTicket(index,name);


        ServerMain.reply.send(gson.toJson(replyTicket));
    }

    static void joinQueue()
    {
        //If client wants to join and isnt already in the queue (via another client) add them to the list
        if(!ServerMain.positionInQueue.contains(name))
        {
            ServerMain.positionInQueue.add(name);

            QueueItem QI = new QueueItem(name, message.clientID);
            ServerMain.queueItems.put(name,QI);
        }
        else
        {
            QueueItem QI = ServerMain.queueItems.get(name);
            if(!QI.clientID.contains(message.clientID))
            {
                QI.clientID.add(message.clientID);
                ServerMain.queueItems.replace(name,QI);
            }
            else
            {
                //if no changes return early to not change QueueChanged Flag
                return;
            }
        }
        ServerMain.queueChanged = true;
    }


    static void handleHeartBeat()
    {

    }
}
