package CommunicationObjects;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class QueueItem {
    String name;

    List<String> clientIDList = new ArrayList<>();

    Timer heartBeatTimer;


    public QueueItem(String name,String clientID)
    {
        this.name = name;
        this.clientIDList.add(clientID);
        heartBeatTimer = new Timer(true);



    }

    //maybe change interaction to something more reasonable
    public void addIDToList(String ID)
    {
        clientIDList.add(ID);
    }

    public List<String> getClientIDList() {
        return clientIDList;
    }


}
