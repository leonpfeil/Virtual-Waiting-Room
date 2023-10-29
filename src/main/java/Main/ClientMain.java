package Main;

import CommunicationModels.ClientMessage;
import CommunicationModels.TestMessage;
import com.google.gson.Gson;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ClientMain {
    // test client implementation, ignore please
    public static void main(String[] args) throws InterruptedException {
        ZContext context = new ZContext();
        ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
        subscriber.connect("tcp://localhost:5555");
        subscriber.subscribe("queue".getBytes(ZMQ.CHARSET));

        ZMQ.Socket socket = context.createSocket(SocketType.REQ);
        socket.connect("tcp://localhost:5556");


        ClientMessage newMessage = new ClientMessage(true, "test1", "699");

        Gson gson = new Gson();

        String json = gson.toJson(newMessage);
        System.out.println("Sending:");
        System.out.println(json);
        socket.send(json.getBytes(ZMQ.CHARSET));

        System.out.println("queue Ticket:");
        System.out.println(socket.recvStr());

        System.out.println("End of Req/Rep");
        System.out.println("Broadcast:");


        String string = subscriber.recvStr(0).trim();
        System.out.println(string);
        string = subscriber.recvStr(0).trim();
        System.out.println(string);

        Thread.sleep(2000);


        TestMessage heartbeat = new TestMessage("test", "699");
        socket.send(gson.toJson(heartbeat));
        String reply = socket.recvStr();
        System.out.println(reply);

    }
}
