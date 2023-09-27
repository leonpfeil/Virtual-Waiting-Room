package InternalModels;

import java.util.*;

public class QueueItem {
    private String name;

    private int index;

    List<String> clientIDList = new ArrayList<>();

    //Map from ClientID to corresponding Heartbeattimer
    private final Map<String,Timer> heartBeatTimerMap = new HashMap<>();
    private Queue queue;

    //supervisor
    private QueueItem client;
    private SupervisorState.Status status = SupervisorState.Status.pending;
    private String supervisorMessage;

    public QueueItem(String name,String clientID,Queue queue,int index)
    {
        this.name = name;
        this.queue = queue;
        this.index = index;

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

    public QueueItem getClient() {
        return client;
    }

    public void setClient(QueueItem client) {
        this.client = client;
        queue.setQueueChanged(true);
    }

    public SupervisorState.Status getStatus() {
        return status;
    }

    public void setStatus(SupervisorState.Status status) {
        this.status = status;
        queue.setQueueChanged(true);

        if(status == SupervisorState.Status.available)
            queue.available.add(getName());
        else
            queue.available.remove(getName());
    }

    public String getSupervisorMessage() {
        return supervisorMessage;
    }

    public void setSupervisorMessage(String supervisorMessage) {
        this.supervisorMessage = supervisorMessage;
    }


    public void destroyAllTimers()
    {
        for (String s: clientIDList) {
            heartBeatTimerMap.get(s).cancel();
        }
    }

    public int getIndex() {
        return index;
    }
}
