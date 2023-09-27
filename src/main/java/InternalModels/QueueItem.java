package InternalModels;

import java.util.*;

public class QueueItem {
    private String name;

    List<String> clientIDList = new ArrayList<>();

    //Map from ClientID to corresponding Heartbeattimer
    private final Map<String,Timer> heartBeatTimerMap = new HashMap<>();
    private Queue queue;

    public QueueItem(String name,String clientID,Queue queue)
    {
        this.name = name;
        this.queue = queue;

        addIDToList(clientID);

    }


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
            //refresh heartbeat-timer
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

    public String getName() {
        return name;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}
