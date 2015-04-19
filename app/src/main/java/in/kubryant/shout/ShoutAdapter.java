package in.kubryant.shout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ShoutAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Shout> shoutList;
    private static LayoutInflater inflater = null;

    public ShoutAdapter(Activity a, ArrayList<Shout> s) {
        activity = a;
        shoutList = s;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return shoutList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.shout_layout, null);

        TextView user = (TextView)vi.findViewById(R.id.username);
        TextView msg = (TextView)vi.findViewById(R.id.message);
        TextView time = (TextView)vi.findViewById(R.id.timestamp);

        Shout shout = new Shout();
        shout = shoutList.get(position);

        user.setText(shout.getUser());
        msg.setText(shout.getMsg());
        time.setText(shout.getTime());

        return vi;
    }
}
