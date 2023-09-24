package CommunicationObjects;

import java.util.*;

public class QueueItem {
    String name;

    List<String> clientIDList = new ArrayList<>();

    //Map from ClientID to corresponding Heartbeattimer
    //Map<String,Timer> heartBeatTimerMap = new HashMap();


    public QueueItem(String name,String clientID)
    {
        this.name = name;
        addIDToList(clientID);






    }

    //maybe change interaction to something more reasonable
    public void addIDToList(String ID)
    {
        clientIDList.add(ID);
        //startNewTimer(ID);

    }

    public List<String> getClientIDList() {
        return clientIDList;
    }

    /*public void startNewTimer(String ID)
    {
        Timer heartBeatTimer = new Timer();
        HeartbeatTimerTask task = new HeartbeatTimerTask(name,ID);
        task.run();

    }

    public void executeHeartbeat(String ID)
    {
        Timer t = new Timer();
        HeartbeatTimerTask task = new HeartbeatTimerTask(name,ID);
        //t.schedule(task,);
        heartBeatTimerMap.get(ID).cancel();


        heartBeatTimerMap.replace(ID,new Timer());
    } */


}
