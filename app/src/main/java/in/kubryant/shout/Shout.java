package in.kubryant.shout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import in.kubryant.andhoclib.src.AndHocMessage;

public class Shout extends AndHocMessage implements Comparable<Shout> {

    public Shout(AndHocMessage message) {
        setUser(message.get("user"));
        setMsg(message.get("msg"));
        setTime(message.get("time"));
        setMsgId(message.get("msgId"));
        setTimeRecv(getTimestamp());
    }

    public Shout() {
        record.put("time", getTimestamp());
    }

    public int compareTo(Shout s1) {


        // ascending: this - s1
        // descending: s1 - this
        return 0;
    }

    public static Comparator<Shout> ReceivedTimeSort = new Comparator<Shout>() {
        @Override
        public int compare(Shout s1, Shout s2) {
            return 0;
        }
    };

    public static Comparator<Shout> CreatedTimeSort = new Comparator<Shout>() {
        @Override
        public int compare(Shout s1, Shout s2) {
            return 0;
        }
    };

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
