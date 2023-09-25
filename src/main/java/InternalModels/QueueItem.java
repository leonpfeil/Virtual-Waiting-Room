package InternalModels;

import java.util.*;

public class QueueItem {
    String name;

    List<String> clientIDList = new ArrayList<>();

    //Map from ClientID to corresponding Heartbeattimer
    Map<String,Timer> heartBeatTimerMap = new HashMap();
    Queue queue;


    public QueueItem(String name,String clientID,Queue queue)
    {
        this.name = name;
        this.queue = queue;

        addIDToList(clientID);

    }

    //maybe change interaction to something more reasonable
    public void addIDToList(String ID)
    {
        clientIDList.add(ID);
        startTimer(ID);

    }

    public List<String> getClientIDList() {
        return clientIDList;
    }

    public void startTimer(String clientID)
    {
        Timer heartBeatTimer = new Timer();
        HeartbeatTimerTask task = new HeartbeatTimerTask(name,clientID,queue);
        heartBeatTimer.schedule(task,4000L);

        if(heartBeatTimerMap.containsKey(clientID))
        {
            //refresh heartbeattimer
            heartBeatTimerMap.get(clientID).cancel();
            heartBeatTimerMap.replace(clientID,heartBeatTimer);
        }
        else
        {
            //execute heartbeat for first time
            heartBeatTimerMap.put(clientID,heartBeatTimer);
        }

    }

    public String toString()
    {
        StringBuilder out = new StringBuilder();
        for(String s : clientIDList)
        {
            out.append(s + " ");
        }
        return out.toString();
    }


}
