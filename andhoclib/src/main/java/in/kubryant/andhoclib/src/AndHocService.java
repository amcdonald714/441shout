package in.kubryant.andhoclib.src;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import in.kubryant.andhoclib.interfaces.AndHocServiceInterface;

public class AndHocService extends Service implements AndHocServiceInterface, Runnable {
    private final String TAG = AndHocService.class.getSimpleName();

    private Handler handler = new Handler();
    private static boolean listening = false;

    private static AndHocMessageListener mListener = null;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;

    private int listenFrequency = 6000;

    @Override
    public void onCreate() {
        Log.d(TAG, "AndHocService Created");
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        new Thread(this, "AndHocService").start();
        AndHocService.setListening(true);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "AndHocService Destroyed");
        if(AndHocService.listening) {
            AndHocService.listening = false;
            handler.removeCallbacks(this);
            stopListen();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "AndHocService onStartCommand");
        return START_STICKY;
    }

    @Override
    public void run() {
        if(AndHocService.listening) {
            AndHocService.setListening(false);
            //restartListen();
            listen();
        } else {
            AndHocService.setListening(true);
            stopListen();
            handler.postDelayed(this, listenFrequency);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startAndHocService(Context context) {
        Log.d(AndHocService.class.getSimpleName(), "AndHocService Started");
        Intent intent = new Intent(context, AndHocService.class);
        context.startService(intent);
        AndHocService.listening = true;
    }

    public static void stopAndHocService(Context context) {
        Log.d(AndHocService.class.getSimpleName(), "AndHocService Stopped");
        Intent intent = new Intent(context, AndHocService.class);
        context.stopService(intent);
        AndHocService.listening = false;
    }

    public static void addListener(AndHocMessageListener listener) {
        mListener = listener;
    }

    public static void removeListener() {
        mListener = null;
    }

    public static boolean isRunning() {
        return AndHocService.listening;
    }

    @Override
    public void listen() {
        Log.d(TAG, "Listening Started");
        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
            @Override
            public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> record, WifiP2pDevice srcDevice) {
            Log.d(TAG, "Record available: " + record.toString());
            AndHocMessage message = new AndHocMessage(record);
            mListener.onNewMessage(message);
            }
        };
        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice resourceType) {
            Log.d(TAG, "Service available: " + resourceType.deviceName);
            }
        };
        mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);

        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance("_msg", "_presence._tcp");
        mManager.addServiceRequest(mChannel, serviceRequest, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                mManager.discoverServices(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Discover services success");
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.d(TAG, "Service discovery failed (" + reason + ")");
                    }
                });
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Service request failed (" + reason + ")");
            }
        });
        handler.postDelayed(this, listenFrequency);
    }

    @Override
    public void stopListen() {
        mManager.clearServiceRequests(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Listening Stopped");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Listening stop failed (" + reason + ")");
            }
        });
    }

    @Override
    public void restartListen() {
        mManager.clearServiceRequests(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Listening Restarting");
                listen();
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Listening stop failed (" + reason + ")");
            }
        });
    }

    public static void setListening(boolean listening) {
        AndHocService.listening = listening;
    }

    public static boolean getListening() {
        return AndHocService.listening;
    }
}