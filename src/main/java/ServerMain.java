import com.google.gson.Gson;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.*;

import org.apache.commons.collections4.*;


public class ServerMain {

    public static ZMQ.Socket reply;
    static ZMQ.Socket publisher;

    //List saves the position of the unique User while Map saves the different clientIDs
    public static List<String> positionInQueue= new ArrayList<>();
    public static Map<String,QueueItem> queueItems = new HashMap<>();
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

            //main loop
            while(!Thread.currentThread().isInterrupted())
            {
                //Handle incoming Queue join requests
                //or Heartbeats
                String incomingRequest = reply.recvStr(0);
                if(incomingRequest != null)
                {
                    IncomingRequests.handleNewRequest(incomingRequest);
                    System.out.println(json.toJson(queueItems));
                }

                //Inform clients of changes in Queue
                if(queueChanged)
                {
                    publishQueueChanges();
                }


            }

        }
        //  Prepare our context and publisher
        /*try (ZContext context = new ZContext()) {
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5556");
            //publisher.bind("ipc://weather");

            //  Initialize random number generator
            Random srandom = new Random(System.currentTimeMillis());
            while (!Thread.currentThread().isInterrupted()) {
                //  Get values that will fool the boss
                publisher.sendMore("Test");
                publisher.send("testicle", 0);
            }

         */

    }

    public static void publishQueueChanges()
    {

        queueChanged = false;
    }
}
