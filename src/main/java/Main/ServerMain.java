package Main;

import CommunicationModels.ErrorMessage;
import InternalModels.Queue;
import InternalModels.SupervisorQueue;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Timer;
import java.util.TimerTask;


public class ServerMain {

    static ZMQ.Socket reply;
    static ZMQ.Socket publisher;

    public static void main(String[] args) {
        Queue clientQueue = new Queue();
        SupervisorQueue supervisorQueue = new SupervisorQueue();

        ZContext context = new ZContext();


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



                /*reply.monitor("tcp://*:5556",ZMQ.EVENT_CONNECTED);

                new Thread(() -> {
                    ZMQ.Socket monitorSocket = context.createSocket(ZMQ.PAIR);
                    monitorSocket.connect("inproc://monitor.rep");

                    while (!Thread.currentThread().isInterrupted()) {
                        byte[] event = monitorSocket.recv();
                        int eventCode = ZMQ.

                        if (eventCode == ZMQ.EVENT_CONNECTED) {
                            System.out.println("A new connection was established.");
                            // Add your handling code here
                        }
                    }

                    monitorSocket.close();
                }).start();*/

                String incomingRequestString = reply.recvStr(0);
                if (!incomingRequestString.isEmpty()) {
                    String replyString = requestHandler.handleRequest(incomingRequestString);
                    reply.send(replyString);
                }
                else //send error
                {
                    ErrorMessage error = new ErrorMessage("InvalidFormat","Message is empty");
                    reply.send(error.createJSON());
                }


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
