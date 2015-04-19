package in.kubryant.shout;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
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
                    mDbHelper.insertMessage(shout);
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
            Toast.makeText(getApplicationContext(), "Reloading messages", Toast.LENGTH_LONG).show();
            Set<Shout> messages = mDbHelper.getAllMessages();
            for (Shout message : messages) {
                String msg = message.get("msg");
                Log.d("FeedReader", "Reloading message: "+msg);
                shoutList.add(message);
            }
            shoutAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
<<<<<<< HEAD
        reloadMessages();
=======
        AndHocService.setListening(true);
//        reloadMessages();
>>>>>>> ce11f3f5effe6b019a8be50d9f68e9d0e8db97fa
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

    public void onClickShout(View view) {
        String message = editTextMessage.getText().toString();
        editTextMessage.setText("");

        if (!message.equals("")) {
            String msgId = UUID.randomUUID().toString();
            Shout shout = new Shout();
            shout.setUser("Anonymous");
            shout.setMsg(message);
            shout.setTime(getTime());
            shout.setMsgId(msgId);
            shoutList.add(shout);

            repeatCheck.add(msgId);

            shoutAdapter.notifyDataSetChanged();
            mMessenger.broadcast(this, shout);
            mDbHelper.insertMessage(shout);
        }
    }

<<<<<<< HEAD
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            default: return super.onOptionsItemSelected(item);
        }
=======
    private String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, h:m a", Locale.US);
        return sdf.format(cal.getTime());
>>>>>>> ce11f3f5effe6b019a8be50d9f68e9d0e8db97fa
    }
}
