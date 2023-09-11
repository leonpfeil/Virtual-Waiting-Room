import java.util.ArrayList;
import java.util.List;

public class QueueItem {
    String name;
    List<String> clientID = new ArrayList<>();

    //keep track of place in queue separately because there can be a single user with multiple clients.
    // All clients are supposed to have the same place and therefore we cant simply use the index of the list
    int placeInQueue;

    public QueueItem(String name,String clientID)
    {
        this.name = name;
        this.clientID.add(clientID);
    }
}
