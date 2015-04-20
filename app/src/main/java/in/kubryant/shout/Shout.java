package in.kubryant.shout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

import in.kubryant.andhoclib.src.AndHocMessage;

public class Shout extends AndHocMessage {

    private String timeRecv;

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

    @Override
    public String toString() {
        return "Shout: time: " + getTime() + " recv: " + getTimeRecv() + " msg: " + getMsg();
    }

    public static Comparator<Shout> ReceivedTimeSort = new Comparator<Shout>() {
        @Override
        public int compare(Shout s1, Shout s2) {
            Long cmp = Long.parseLong(s2.getTimeRecv()) - Long.parseLong(s1.getTimeRecv());
            if (cmp > 0) {
                return 1;
            } else if (cmp < 0) {
                return -1;
            }
            return 0;
        }
    };

    public static Comparator<Shout> CreatedTimeSort = new Comparator<Shout>() {
        @Override
        public int compare(Shout s1, Shout s2) {
            Long cmp = Long.parseLong(s2.getTime()) - Long.parseLong(s1.getTime());
            if (cmp > 0) {
                return 1;
            } else if (cmp < 0) {
                return -1;
            }
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
        this.timeRecv = timeRecv;
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
        return this.timeRecv;
    }
    public String getHumanTime() {
        return makeHumanReadable(getTime());
    }

}
