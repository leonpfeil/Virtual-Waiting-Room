import com.google.gson.*;

public class IncomingRequests {
    public static void handleNewRequest(String incomingRequest)
    {
        Gson gson = new Gson();
        ClientMessage message = gson.fromJson(incomingRequest,ClientMessage.class);

        System.out.println(message.name + message.clientID + message.enterQueue);

        ServerMain.reply.send("aaaa");
    }
}
