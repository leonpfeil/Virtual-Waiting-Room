package InternalModels;

import CommunicationModels.QueueTicket.ClientQueueTicket;
import CommunicationModels.QueueTicket.SupervisorQueueTicket;

import java.util.*;

public class Queue {

    //List saves the position of the unique User while Map saves the different clientIDs and timers associated with that user
    private List<String> positionInQueue= new ArrayList<>();
    private Map<String, QueueItem> queueItems = new HashMap<>();

    //supervisor
    List<String> available = new ArrayList<>();

    //is only set to true inside of an queue object or queueitem object
    //will be set to false once changes have been broadcast to all clients in Broadcast.java
    private boolean queueChanged = false;

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

    public ClientQueueTicket[] createOrderedClientQueueTicketArray()
    {
        ClientQueueTicket[] ticketArray = new ClientQueueTicket[positionInQueue.size()];
        for(int i = 0; i < positionInQueue.size();i++)
        {
            String name = positionInQueue.get(i);
            ticketArray[i] = new ClientQueueTicket(queueItems.get(name).getIndex(),name);
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

    public void addNewUserToQueue(String name,String clientID,Queue queue,int index)
    {
        positionInQueue.add(name);

        QueueItem QI = new QueueItem(name, clientID,queue,index);
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
            //System.out.println(e.getMessage());
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

    //supervisor related methods


    /**
     * removes first Client from Queue and returns the name
     * @return
     */
    public QueueItem acceptClient()
    {
        String name = positionInQueue.get(0);
        QueueItem clientItem = queueItems.get(name);

        positionInQueue.remove(0);
        queueItems.get(name).destroyAllTimers();
        queueItems.remove(name);

        setQueueChanged(true);

        return clientItem;
    }

    /**
     * removes a specific client from the Queue
     * @param name Client name
     */
    public QueueItem acceptClient(String name)
    {
        //removes specified client
        QueueItem item;

        positionInQueue.remove(name);
        queueItems.get(name).destroyAllTimers();
        item = queueItems.get(name);
        queueItems.remove(name);

        setQueueChanged(true);
        return item;
    }


    public SupervisorQueueTicket[] createOrderedSupervisorQueueTicketArray(Queue clientQueue)
    {


        SupervisorQueueTicket[] ticketArray = new SupervisorQueueTicket[getPositionInQueue().size()];
        for(int i = 0; i < getPositionInQueue().size();i++)
        {
            String name = getPositionInQueue().get(i);
            QueueItem QI = getQueueItems().get(name);

            ClientQueueTicket clientTicket = null;
            if(QI.getClient() != null)
            {
                String clientName = QI.getClient().getName();

                clientTicket = new ClientQueueTicket(QI.getIndex(),clientName);
            }

            ticketArray[i] = new SupervisorQueueTicket(name,QI.getStatus(),clientTicket);
        }
        return ticketArray;

    }

    public String removeSupervisorFromAvailable()
    {
        String name = available.get(0);
        available.remove(0);

        return name;
    }

    public List<String> getAvailableSupervisor()
    {
        return available;
    }

}
