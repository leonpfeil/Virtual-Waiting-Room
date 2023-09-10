import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.List;

public class ServerMain {

    public static ZMQ.Socket reply;
    static ZMQ.Socket publisher;

    static List<String> queue = new ArrayList<>();
    public static boolean queueChanged = false;
    public static void main(String[] args) throws Exception
    {


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

    }
}
