package bluetooth.tokbox.com.bluetoothdiscovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

public class Sco extends AppCompatActivity {
    private final static String     LOG_TAG = "[Bluetooth]";

    private SoundPool       _soundPool;
    private int             _soundId;
    private CheckBox        _bluetoothCkBox;

    private final BroadcastReceiver _bcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);
            switch (state) {
                case AudioManager.SCO_AUDIO_STATE_DISCONNECTED:
                    Log.d(LOG_TAG, "Bluetooth disconnected");
                    break;
                case AudioManager.SCO_AUDIO_STATE_CONNECTED:
                    Log.d(LOG_TAG, "Bluetooth connected");
                    _bluetoothCkBox.setChecked(true);
                    break;
                case AudioManager.SCO_AUDIO_STATE_CONNECTING:
                    Log.d(LOG_TAG, "Bluetooth connecting");
                    break;
                case AudioManager.SCO_AUDIO_STATE_ERROR:
                    Log.d(LOG_TAG, "Bluetooth error");
                    break;
                default: break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sco);
        /* hook up ui */
        _bluetoothCkBox= (CheckBox)findViewById(R.id.checkbox_bluetooth);
        /* load test sound */
        _soundPool = new SoundPool(AudioManager.STREAM_MUSIC, 1, 0);
        AssetManager assetMgr = getAssets();
        try {
            _soundId = _soundPool.load(assetMgr.openFd("timetravel.ogg"), 1);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Failed to load sound", Toast.LENGTH_LONG).show();
        }
        /* setup Audio SCO */
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        IntentFilter newintent = new IntentFilter();
        newintent.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED);
        try {
            registerReceiver(_bcastReceiver, newintent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        audioManager.setBluetoothScoOn(true);
        audioManager.startBluetoothSco();
    }

    @Override
    protected void onStop() {
        _shutdownSCO();
        super.onStop();
    }

    public void onWidgetClick(View view) {
        switch (view.getId()) {
            case R.id.button_playsound: {
                _soundPool.play(_soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                break;
            }
            case R.id.button_exit: {
                _shutdownSCO();
                System.exit(0);
            }
        }
    }

    private void _shutdownSCO() {
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.stopBluetoothSco();
        audioManager.setBluetoothScoOn(false);
        unregisterReceiver(_bcastReceiver);
        audioManager.setMode(AudioManager.MODE_NORMAL);
    }
}
