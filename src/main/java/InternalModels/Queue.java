package InternalModels;

import CommunicationModels.QueueTicket;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Queue {

    //List saves the position of the unique User while Map saves the different clientIDs and timers associated with that user
    List<String> positionInQueue= new ArrayList<>();
    Map<String, QueueItem> queueItems = new HashMap<>();
    boolean queueChanged = false;

    Gson json = new Gson();

    public List<String> getPositionInQueue() {
        return positionInQueue;
    }

    public Map<String, QueueItem> getQueueItems() {
        return queueItems;
    }

    public boolean isQueueChanged() {
        return queueChanged;
    }

    public void setQueueChanged(boolean queueChanged) {
        this.queueChanged = queueChanged;
    }

    public QueueTicket[] createOrderedQueueTicketArray()
    {
        QueueTicket[] ticketArray = new QueueTicket[positionInQueue.size()];
        for(int i = 0; i < positionInQueue.size();i++)
        {
            ticketArray[i] = new QueueTicket(i,positionInQueue.get(i));
        }
        return ticketArray;
    }

    public int getCurrentPositionInQueue(String name)
    {
        return positionInQueue.indexOf(name);
    }

    public boolean isNameInQueue(String name)
    {
        return positionInQueue.contains(name);
    }

    public void addNewUserToQueue(String name,String clientID,Queue queue)
    {
        positionInQueue.add(name);

        QueueItem QI = new QueueItem(name, clientID,queue);
        queueItems.put(name,QI);

        setQueueChanged(true);
    }

    public void addIDToExistingUser(String name,String clientID)
    {
        //Check if id already in list, if not add id to this user
        QueueItem QI = queueItems.get(name);
        if(!QI.getClientIDList().contains(clientID))
        {
            QI.addIDToList(clientID);

        }
    }

    public void removeID(String name,String clientID)
    {
        queueItems.get(name).clientIDList.remove(clientID);

        if(queueItems.get(name).clientIDList.isEmpty())
        {
            positionInQueue.remove(name);
            queueItems.remove(name);
            setQueueChanged(true);
        }
    }

    public void startTimer(String name,String clientID)
    {
        try
        {
            queueItems.get(name).startTimer(clientID);
        }
        catch (NullPointerException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public String toString()
    {
        StringBuilder out = new StringBuilder();
        for(String QI : queueItems.keySet())
        {
            out.append(queueItems.get(QI).toString());
        }
        return out.toString();
    }

}
