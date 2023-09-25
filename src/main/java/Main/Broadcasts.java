package Main;

import CommunicationModels.QueueTicket;
import InternalModels.Queue;
import com.google.gson.Gson;
import org.zeromq.ZMQ;

public class Broadcasts {

    ZMQ.Socket publisher;
    Queue queue;
    public Broadcasts(ZMQ.Socket socket,Queue queue)
        {
            publisher = socket;
            this.queue = queue;
        }

    public void publishQueueChanges(){
        Gson gson = new Gson();

        QueueTicket[] ticketArray = queue.createOrderedQueueTicketArray();

        String arrayAsJSON = gson.toJson(ticketArray);
        System.out.println(arrayAsJSON);

        //Thread.sleep(1000);
        publisher.sendMore("queue");
        publisher.send(arrayAsJSON,0);

        queue.setQueueChanged(false);
        System.out.println("-published queue changes-");
    }
}
