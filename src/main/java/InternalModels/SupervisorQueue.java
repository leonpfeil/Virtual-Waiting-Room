package InternalModels;

import CommunicationModels.QueueTicket.ClientQueueTicket;
import CommunicationModels.QueueTicket.SupervisorQueueTicket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupervisorQueue extends Queue{
    //supervisor
    List<String> available = new ArrayList<>();

    private Map<String, SupervisorQueueItem> queueItems = new HashMap<>();

    public SupervisorQueueTicket[] createOrderedSupervisorQueueTicketArray(Queue clientQueue)
    {


        SupervisorQueueTicket[] ticketArray = new SupervisorQueueTicket[super.getPositionInQueue().size()];
        for(int i = 0; i < getPositionInQueue().size();i++)
        {
            String name = getPositionInQueue().get(i);
            SupervisorQueueItem QI = (SupervisorQueueItem) getQueueItems().get(name);

            ClientQueueTicket clientTicket = null;
            if(QI.getClient() != null)
            {
                String clientName = QI.getClient().getName();
                int position = clientQueue.getCurrentPositionInQueue(clientName);

                clientTicket = new ClientQueueTicket(position,name);
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
