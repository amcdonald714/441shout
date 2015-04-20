package in.kubryant.shout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.kubryant.andhoclib.src.AndHocMessage;

public class Shout extends AndHocMessage {

    public Shout(AndHocMessage message) {
        setUser(message.get("user"));
        setMsg(message.get("msg"));
        setTime(message.get("time"));
        setMsgId(message.get("msgId"));
        setTimeRecv(getTimestamp());
    }

    public Shout() {
        setTime(getTimestamp());
        setTimeRecv(getTimestamp());
    }

    private String getTimestamp() {
        Calendar cal = Calendar.getInstance();
        return Long.toString(cal.getTimeInMillis());
    }

    public String makeHumanReadable(String dateInMillis) {
        Long milliseconds = Long.parseLong(dateInMillis);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, h:mm a", Locale.US);
        cal.setTimeInMillis(milliseconds);
        return sdf.format(cal.getTime());
    }

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
    public void setTimeRecv(String timeRecv) {
        record.put("timeRecv", timeRecv);
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
    public String getTimeRecv() {
        return record.get("timeRecv");
    }
    public String getHumanTime() {
        return makeHumanReadable(getTime());
    }

}
