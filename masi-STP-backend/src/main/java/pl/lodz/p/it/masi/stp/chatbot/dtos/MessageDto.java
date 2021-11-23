package pl.lodz.p.it.masi.stp.chatbot.dtos;

import com.ibm.watson.developer_cloud.conversation.v1.model.Context;

import java.util.List;

public class MessageDto {
    private String message;
    private List<String> response;
    private Context context;
    private String url;
    private String ip;

    public MessageDto() {
    }

    public MessageDto(Context context, String url, List<String> response) {
        this.context = context;
        this.url = url;
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getResponse() {
        return response;
    }

    public void setResponse(List<String> response) {
        this.response = response;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "message='" + message + '\'' +
                ", response=" + response +
                ", context=" + context +
                ", url='" + url + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
