package Main;

import InternalModels.Queue;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;


public class ServerMain {

    static ZMQ.Socket reply;
    static ZMQ.Socket publisher;

    public static void main(String[] args) {
        Queue clientQueue = new Queue();
        Queue supervisorQueue = new Queue(); 

        try (ZContext context = new ZContext()) {


            //start listening for clients that want to join the queue
            reply = context.createSocket(SocketType.REP);
            reply.bind("tcp://*:5556");

            //open socket to send updates about queue status
            publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5555");

            Broadcasts broadcast = new Broadcasts(publisher, clientQueue,supervisorQueue);
            RequestHandler requestHandler = new RequestHandler(broadcast,clientQueue, supervisorQueue);

            System.out.println("Connection established");
            //main loop
            while (!Thread.currentThread().isInterrupted()) {
                //Handle incoming queue join requests
                //or Heartbeats
                String incomingRequestString = reply.recvStr(0);
                if (!incomingRequestString.isEmpty()) {
                    String replyString = requestHandler.handleRequest(incomingRequestString);
                    reply.send(replyString);
                    //queue.debugPrintQueueItems();
                }

                //TODO add connect event monitor
                //TODO clean buffer before start
                //TODO timer that checks if main thread is still alive, if not restart with persistent queue
                //TODO save queue state to storage
                //Inform clients of changes in queue
                if (clientQueue.isQueueChanged()) {
                    System.out.println("client queue has changed, broadcasting changes");
                    broadcast.publishQueueChanges(false);
                }
                if(supervisorQueue.isQueueChanged())
                {
                    System.out.println("supervisor queue has changed, broadcasting changes");
                    broadcast.publishQueueChanges(true);
                }
            }

        }

    }

}
