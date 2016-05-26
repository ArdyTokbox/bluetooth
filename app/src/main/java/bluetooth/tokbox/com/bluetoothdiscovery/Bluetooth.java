package bluetooth.tokbox.com.bluetoothdiscovery;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.tokbox.audiodevice.AudioDevice;
import com.tokbox.audiodevice.DeviceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bluetooth
        extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, DeviceManager.Listener {


    private final static String     LOG_TAG = "[Bluetooth]";

    private SoundPool       mSoundPool;
    private int             mSoundId;

    private DeviceManager       _deviceMgr;
    private Spinner             _deviceSpinner;
    private List<AudioDevice>   _devList;
    private AudioDevice         _activeDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        /* hook up ui */
        _deviceSpinner = (Spinner)findViewById(R.id.spinner_btpaired);
        _deviceSpinner.setOnItemSelectedListener(this);
        /* load test sound */
        mSoundPool = new SoundPool(AudioManager.STREAM_MUSIC, 1, 0);
        AssetManager assetMgr = getAssets();
        try {
            mSoundId = mSoundPool.load(assetMgr.openFd("timetravel.ogg"), 1);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Failed to load sound", Toast.LENGTH_LONG).show();
        }
        /* create audio device manager */
        _deviceMgr = new DeviceManager(this, this);
    }

    @Override
    protected void onStop() {
        _shutdown();
        super.onStop();
    }

    public void onWidgetClick(View view) {
        switch (view.getId()) {
            case R.id.button_playsound: {
                mSoundPool.play(mSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
                break;
            }
            case R.id.button_exit: {
                _shutdown();
                System.exit(0);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "select:" + id);
        if (null != _devList) {
            if (null != _activeDevice) {
                _activeDevice.disconnect();
            }
            _activeDevice = _devList.get((int) id);
            _activeDevice.connect();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void deviceListUpdated(List<AudioDevice> list) {
        /* create device UI list */
        List<String> lst = new ArrayList<>();
        _devList = list;
        for (AudioDevice device : list) {
            lst.add(device.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                lst
        );
        _deviceSpinner.setAdapter(adapter);
    }

    private void _shutdown() {
        _deviceMgr.shutdown();
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
    }
}
