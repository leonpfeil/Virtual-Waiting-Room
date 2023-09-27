package CommunicationModels;

import com.google.gson.Gson;

public class ErrorMessage {
    String error;
    String msg;

    Gson gson = new Gson();
    public ErrorMessage(String error, String msg) {
        this.error = error;
        this.msg = msg;
    }

    public String createJSON()
    {
        return gson.toJson(this);
    }
}
