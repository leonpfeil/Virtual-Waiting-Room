import CommunicationObjects.QueueTicket;
import com.google.gson.Gson;

import java.util.List;

public class Broadcasts {
    public static void publishQueueChanges() throws InterruptedException {
        Gson gson = new Gson();

        //Copy so i don't have to type "ServerMain." every time
        List<String> positionInQueueCopy = ServerMain.positionInQueue;
        QueueTicket[] ticketArray = new QueueTicket[positionInQueueCopy.size()];




        //Turn Name with corresponding position into a QueueTicket
        for(int i = 0; i < positionInQueueCopy.size();i++)
        {
            ticketArray[i] = new QueueTicket(i,positionInQueueCopy.get(i));
        }

        String arrayAsJSON = gson.toJson(ticketArray);
        System.out.println(arrayAsJSON);

        Thread.sleep(1000);
        ServerMain.publisher.sendMore("Queue");
        ServerMain.publisher.send(arrayAsJSON,0);


        System.out.println("aaa");
        ServerMain.queueChanged = false;
    }
}
