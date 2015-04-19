package in.kubryant.andhoclib.src;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import in.kubryant.andhoclib.interfaces.AndHocMessageInterface;

public class AndHocMessage implements AndHocMessageInterface {
    protected Map<String, String> record = new HashMap<String, String>();

    public AndHocMessage() {}

    public AndHocMessage(Map<String, String> rec) {
        record = rec;
    }

    @Override
    public String get(String name) {
        return record.get(name);
    }

    @Override
    public void setRecord(Map<String, String> rec) {
        record = rec;
    }

    @Override
    public Map<String, String> getRecord() {
        return record;
    }

    @Override
    public void add(String key, String value) {
        record.put(key, value);
    }

    public Set<String> getKeys() {
        return record.keySet();
    }
}
