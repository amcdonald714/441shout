package in.kubryant.shout;

import in.kubryant.andhoclib.src.AndHocMessage;

public class Shout extends AndHocMessage {

    public Shout(AndHocMessage message) {
        setUser(message.get("user"));
        setMsg(message.get("msg"));
        setTime(message.get("time"));
        setMsgId(message.get("msgId"));
    }

    public Shout() {}

    // Setters
    public void setUser(String user) {
        record.put("user", user);
    }
    public void setMsg(String msg) {
        record.put("msg", msg);
    }
    public void setTime(String time) {
        record.put("time", time);
    }
    public void setMsgId(String msgId) {
        record.put("msgId", msgId);
    }

    // Getters
    public String getUser() {
        return record.get("user");
    }
    public String getMsg() {
        return record.get("msg");
    }
    public String getTime() {
        return record.get("time");
    }
    public String getMsgId() {
        return record.get("msgId");
    }

}
