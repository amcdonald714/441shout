package in.kubryant.shout;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import in.kubryant.andhoclib.src.AndHocMessage;
import in.kubryant.andhoclib.src.AndHocMessageListener;
import in.kubryant.andhoclib.src.AndHocMessenger;
import in.kubryant.andhoclib.src.AndHocService;

public class MainActivity extends ActionBarActivity {
    private EditText editTextMessage;
    private ArrayList<Shout> shoutList;
    private ListView shoutListView;
    private ShoutAdapter shoutAdapter;

    private ArrayList<String> repeatCheck = new ArrayList<String>();

    private AndHocMessenger mMessenger;
    private FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Shout!");
        getSupportActionBar().setElevation(0);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }

        mDbHelper = new FeedReaderDbHelper(getApplicationContext());

        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        shoutList = new ArrayList<Shout>();
        shoutListView = (ListView) findViewById(R.id.messageListView);
        shoutAdapter = new ShoutAdapter(this, shoutList);
        shoutListView.setAdapter(shoutAdapter);

        mMessenger = new AndHocMessenger(this);

        AndHocService.addListener(new AndHocMessageListener() {
            @Override
            public void onNewMessage(AndHocMessage msg) {
                Shout shout = new Shout(msg);
                String msgId = shout.getMsgId();

                if (!repeatCheck.contains(msgId)) {
                    repeatCheck.add(msgId);
                    shoutList.add(0, shout);
                    shoutAdapter.notifyDataSetChanged();
                    mDbHelper.insertMessage(shout);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    Notification n  = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Shout!")
                            .setContentText(shout.getMsg())
                            .setSmallIcon(R.drawable.notification)
                            .setContentIntent(pIntent)
                            .setAutoCancel(true).build();

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    notificationManager.notify(0, n);
                }
            }
        });

        if(!AndHocService.isRunning()) {
            AndHocService.startAndHocService(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mMessenger.stopBroadcast(this);
        AndHocService.setListening(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mMessenger.stopBroadcast(this);
        AndHocService.setListening(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mMessenger.stopBroadcast(this);
        AndHocService.setListening(true);
    }

    private void reloadMessages() {
        if(shoutList.isEmpty()) {
            Set<Shout> messages = mDbHelper.getAllMessages();
            for (Shout message : messages) {
                String msg = message.get("msg");
                Log.d("FeedReader", "Reloading message: " + msg);
                shoutList.add(0, message);
                repeatCheck.add(message.getMsgId());
            }
            shoutAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadMessages();
        AndHocService.setListening(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_clear_messages:
                mDbHelper.clear();
                shoutList.clear();
                shoutAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_sort_received_time:
                Log.d("TEST", "ACTION SORT BY RECEIVED TIME");
                return true;
            case R.id.menu_sort_sent_time:
                Log.d("TEST", "ACTION SORT BY SENT TIME");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickShout(View view) {
        String message = editTextMessage.getText().toString();
        editTextMessage.setText("");

        if (!message.equals("")) {
            String msgId = UUID.randomUUID().toString();
            Shout shout = new Shout();
            String username = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString("username", "Anonymous");

            shout.setUser(username);
            shout.setMsg(message);
            shout.setMsgId(msgId);
            shoutList.add(0, shout);
            repeatCheck.add(msgId);
            mDbHelper.insertMessage(shout);

            shoutAdapter.notifyDataSetChanged();
            mMessenger.broadcast(this, shout);
        }
    }
}
