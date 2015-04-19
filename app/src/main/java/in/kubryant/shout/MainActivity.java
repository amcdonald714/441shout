package in.kubryant.shout;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
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
                    shoutList.add(shout);
                    shoutAdapter.notifyDataSetChanged();
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

//    private void reloadMessages() {
//        if(messageList.isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Reloading messages", Toast.LENGTH_LONG).show();
//            Set<AndHocMessage> messages = mDbHelper.getAllMessages();
//            for (AndHocMessage message : messages) {
//                String msg = message.get("msg");
//                Log.d("FeedReader", "Reloading message: "+msg);
//                messageList.add(msg);
//            }
//            mAdapter.notifyDataSetChanged();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        AndHocService.setListening(true);
//        reloadMessages();
    }

    public void onClickShout(View view) {
        String message = editTextMessage.getText().toString();
        editTextMessage.setText("");

        if (!message.equals("")) {
            String msgId = UUID.randomUUID().toString();
            Shout shout = new Shout();
            String username = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString("username", "Anonymous");
            if(username.equals("Anonymous")) {
                Log.d("OurSettings", "Anonymous username");
            }
            shout.setUser(username);
            shout.setMsg(message);
            shout.setTime("April 19, 3:15PM");
            shout.setMsgId(msgId);
            shoutList.add(shout);

            repeatCheck.add(msgId);

            shoutAdapter.notifyDataSetChanged();
            mMessenger.broadcast(this, shout);
        }
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

//            case R.id.action_clear_messages:
//                mDbHelper.clear();
//                messageList.clear();
//                mAdapter.notifyDataSetChanged();
//                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }
}
