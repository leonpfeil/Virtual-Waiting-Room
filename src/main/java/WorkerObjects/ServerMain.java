package WorkerObjects;

import FunctionalObjects.Queue;
import com.google.gson.Gson;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;


public class ServerMain {
    //public static List<String> positionInQueue= new ArrayList<>();
    //public static Map<String, QueueItem> queueItems = new HashMap<>();
    //public static boolean queueChanged = false;

    static Queue queue = new Queue();
    public static ZMQ.Socket reply;
    static ZMQ.Socket publisher;

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


            System.out.println("Connection established");
            //main loop
            while(!Thread.currentThread().isInterrupted()) {
                //Handle incoming FunctionalObjects.Queue join requests
                //or Heartbeats
                String incomingRequestString = reply.recvStr(0);
                if (!incomingRequestString.isEmpty()) {
                    IncomingRequests newRequest = new IncomingRequests(incomingRequestString,queue);
                    newRequest.handleNewRequest();
                    //queue.debugPrintQueueItems();
                }

                //TODO add connect event monitor
                //TODO clean buffer before start
                //TODO timer that checks if main thread is still alive, if not restart with persisten queue
                //TODO save queue state to storage
                //Inform clients of changes in FunctionalObjects.Queue
                if (queue.isQueueChanged()) {
                    System.out.println("queue has changed, broadcasting changes");
                    Broadcasts.publishQueueChanges(queue);
                    //lets check if its a reference
                    queue.isQueueChanged();
                }
            }

        }

    }

    public static void deleteFromQueue(String ID)
    {

    }

}
