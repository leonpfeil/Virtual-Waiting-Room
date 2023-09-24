package CommunicationObjects;

public class HeartbeatTimerTask implements Runnable{
    String name;
    String clientID;

    public HeartbeatTimerTask(String name, String clientID)
    {
        this.name = name;
        this.clientID = clientID;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            System.out.println("Thread Interrupted");
        }


    }
}
