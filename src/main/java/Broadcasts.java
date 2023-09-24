import CommunicationObjects.QueueTicket;
import com.google.gson.Gson;

import java.util.List;

public class Broadcasts {
    public static void publishQueueChanges(List<String> positionInQueue) throws InterruptedException {
        Gson gson = new Gson();

        //Copy so i don't have to type "ServerMain." every time

        QueueTicket[] ticketArray = new QueueTicket[positionInQueue.size()];




        //Turn Name with corresponding position into a QueueTicket
        for(int i = 0; i < positionInQueue.size();i++)
        {
            ticketArray[i] = new QueueTicket(i,positionInQueue.get(i));
        }

        String arrayAsJSON = gson.toJson(ticketArray);
        System.out.println(arrayAsJSON);

        //Thread.sleep(1000);
        ServerMain.publisher.sendMore("queue");
        ServerMain.publisher.send(arrayAsJSON,0);


        System.out.println("aaa");
        ServerMain.queueChanged = false;
    }
}
