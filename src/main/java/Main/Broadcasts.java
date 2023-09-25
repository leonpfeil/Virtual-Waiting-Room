package Main;

import CommunicationModels.QueueTicket;
import InternalModels.Queue;
import com.google.gson.Gson;

public class Broadcasts {
    public static void publishQueueChanges(Queue queue) throws InterruptedException {
        Gson gson = new Gson();

        QueueTicket[] ticketArray = queue.createOrderedQueueTicketArray();

        String arrayAsJSON = gson.toJson(ticketArray);
        System.out.println(arrayAsJSON);

        //Thread.sleep(1000);
        ServerMain.publisher.sendMore("queue");
        ServerMain.publisher.send(arrayAsJSON,0);

        queue.setQueueChanged(false);
        System.out.println("-published queue changes-");
    }
}
