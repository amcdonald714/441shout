package in.kubryant.shout;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import in.kubryant.andhoclib.src.AndHocMessage;
import in.kubryant.andhoclib.src.AndHocMessageListener;
import in.kubryant.andhoclib.src.AndHocMessenger;
import in.kubryant.andhoclib.src.AndHocService;

public class MainActivity extends ActionBarActivity {
    private EditText editTextMessage;

    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> messageList = new ArrayList<>();
    private ArrayList<String> repeatCheck = new ArrayList<>();

    private AndHocMessenger mMessenger;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Shout!");

        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        ListView messageListView = (ListView) findViewById(R.id.messageListView);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        messageListView.setAdapter(mAdapter);

        mMessenger = new AndHocMessenger(this);

        AndHocService.addListener(new AndHocMessageListener() {
            @Override
            public void onNewMessage(AndHocMessage msg) {
                String message = msg.get("msg");
                String msgId = msg.get("user");

                if (!repeatCheck.contains(msgId)) {
                    repeatCheck.add(msgId);
                    if (!message.equals("")) {
                        messageList.add(message);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!AndHocService.isRunning()) {
            AndHocService.startAndHocService(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMessenger.stopBroadcast(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMessenger.stopBroadcast(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessenger.stopBroadcast(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!AndHocService.isRunning()) {
            AndHocService.startAndHocService(this);
        }
    }

    public void onClickShout(View view) {
        if(AndHocService.listening) {
            AndHocService.listening = false;
        } else {
            timer.cancel();
        }

        String message = editTextMessage.getText().toString();
        editTextMessage.setText("");

        if (!message.equals("")) {
            AndHocMessage record = new AndHocMessage();
            record.add("user", UUID.randomUUID().toString());
            record.add("msg", message);
            messageList.add(message);
            mAdapter.notifyDataSetChanged();
            mMessenger.broadcast(this, record);
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                AndHocService.listening = true;
            }
        }, 7000);
    }

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
            default: return super.onOptionsItemSelected(item);
        }
    }
}
