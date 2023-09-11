import com.google.gson.*;
import zmq.socket.clientserver.Server;

import java.util.HashSet;
import java.util.Set;

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

        }

        //ServerMain.positionInQueue.indexOf()
        //QueueTicket replyTicket = new QueueTicket(name,);
        ServerMain.reply.send("aaaa");
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
                //if no changes return early to not change changedFlag
                return;
            }
        }
        ServerMain.queueChanged = true;
    }


    static void handleHeartBeat()
    {

    }
}
