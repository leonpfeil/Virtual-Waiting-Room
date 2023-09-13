import CommunicationObjects.QueueItem;
import com.google.gson.Gson;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.*;


public class ServerMain {

    public static ZMQ.Socket reply;
    static ZMQ.Socket publisher;

    //List saves the position of the unique User while Map saves the different clientIDs
    public static List<String> positionInQueue= new ArrayList<>();
    public static Map<String, QueueItem> queueItems = new HashMap<>();
    public static boolean queueChanged = false;
    public static void main(String[] args) throws Exception
    {
        Gson json = new Gson();

        try(ZContext context = new ZContext())
        {




            //start listening for clients that want to join the queue
            reply = context.createSocket(SocketType.REP);
            reply.bind("tcp://*:5556");

            //open socket to send updates about queue status
            publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5555");
            publisher.bind("ipc://Queue");

            //main loop
            while(!Thread.currentThread().isInterrupted()) {
                //Handle incoming Queue join requests
                //or Heartbeats
                String incomingRequest = reply.recvStr(0);
                if (incomingRequest != null) {
                    IncomingRequests.handleNewRequest(incomingRequest);
                    System.out.println(json.toJson(queueItems));
                }

                //Inform clients of changes in Queue
                if (queueChanged) {
                    System.out.println("in queue changed rn");
                    Broadcasts.publishQueueChanges();
                }

            }

        }

    }

}
