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


    public void debugPrintQueueItems()
    {
        System.out.println(json.toJson(queueItems));
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
    }

    public boolean addIDToExistingUser(String name,String clientID)
    {
        QueueItem QI = queueItems.get(name);
        if(QI.getClientIDList().contains(clientID))
        {
            //ID is already in list, nothing changed so we don't need to broadcast any changes
            return false;
        }
        else
        {
            QI.addIDToList(clientID);
            return true;
        }
    }

    public void removeID(String name,String clientID)
    {
        List<String> test = queueItems.get(name).clientIDList;
        System.out.println(test.toString());
        queueItems.get(name).clientIDList.remove(clientID);

        if(queueItems.get(name).clientIDList.isEmpty())
        {
            positionInQueue.remove(name);
            queueItems.remove(name);
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
