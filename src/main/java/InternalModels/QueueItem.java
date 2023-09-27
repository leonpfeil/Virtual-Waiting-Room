package InternalModels;

import java.util.*;

public class QueueItem {
    private String name;

    List<String> clientIDList = new ArrayList<>();

    //Map from ClientID to corresponding Heartbeattimer
    private Map<String,Timer> heartBeatTimerMap = new HashMap();
    private Queue queue;

    //if this object represents a supervisor then this object contains the current client the supervisor is attending, otherwise null
    private QueueItem client;
    private SupervisorState.Status status = SupervisorState.Status.pending;

    private String supervisorMessage;

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
            queue.available.add(name);
        else
            queue.available.remove(name);
    }

    public String getName() {
        return name;
    }

    public String getSupervisorMessage() {
        return supervisorMessage;
    }

    public void setSupervisorMessage(String supervisorMessage) {
        this.supervisorMessage = supervisorMessage;
    }
}
