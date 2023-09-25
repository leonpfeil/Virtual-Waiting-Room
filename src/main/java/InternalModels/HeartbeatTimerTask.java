package InternalModels;

import java.util.TimerTask;

public class HeartbeatTimerTask extends TimerTask{
    String name;
    String clientID;
    Queue queue;

    public HeartbeatTimerTask(String name, String clientID,Queue queue)
    {
        this.name = name;
        this.clientID = clientID;
        this.queue = queue;
    }

    @Override
    public void run() {
        //if this task runs it means the Heartbeat timed out, so the corresponding client will be removed now. If that was the last ID the user will be fully removed from the queue
        //and lose his position
        queue.removeID(name,clientID);
    }
}
