import CommunicationObjects.ClientMessage;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.google.gson.Gson;

import java.time.Instant;

public class ClientMain {
    public static void main(String[] args) throws InterruptedException {
        ZContext context = new ZContext();
        ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
        subscriber.connect("tcp://localhost:5555");
        subscriber.subscribe("Queue".getBytes(ZMQ.CHARSET));

        ZMQ.Socket socket = context.createSocket(SocketType.REQ);
        socket.connect("tcp://localhost:5556");


        ClientMessage newMessage = new ClientMessage(true,"test","69");

        Gson gson = new Gson();
        String json = gson.toJson(newMessage);
        System.out.println("Sending:");
        System.out.println(json);
        socket.send(json.getBytes(ZMQ.CHARSET));

        System.out.println("Received Queue Ticket:");
        System.out.println(socket.recvStr());

        System.out.println("End of Req/Rep");
        System.out.println("Broadcast:");


        String string = subscriber.recvStr(0).trim();
        System.out.println(string);
        string = subscriber.recvStr(0).trim();
        System.out.println(string);


    }
}
