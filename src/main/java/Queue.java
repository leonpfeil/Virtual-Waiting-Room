import CommunicationObjects.QueueItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Queue {

    //List saves the position of the unique User while Map saves the different clientIDs
    List<String> positionInQueue= new ArrayList<>();
    Map<String, QueueItem> queueItems = new HashMap<>();
    boolean queueChanged = false;

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
}
