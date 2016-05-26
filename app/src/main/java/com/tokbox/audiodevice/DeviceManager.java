package com.tokbox.audiodevice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ardy on 5/24/16.
 */
public class DeviceManager implements BluetoothProfile.ServiceListener {

    private Context                 _ctx;
    private DeviceManager.Listener  _cb;
    private BluetoothProfile        _profile;

    public static interface Listener {
        void deviceListUpdated(final List<AudioDevice> lst);
    }

    public DeviceManager(Context ctx, DeviceManager.Listener notify) {
        _ctx = ctx;
        _cb = notify;
        /* add bluetooth devices */
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.getProfileProxy(ctx, this, BluetoothProfile.HEADSET);
    }

    public void shutdown() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null != _profile) {
            btAdapter.closeProfileProxy(BluetoothProfile.HEADSET, _profile);
        }
    }

    @Override
    public void onServiceConnected(int profile, BluetoothProfile proxy) {
        List<AudioDevice> lst = _getDeviceList();
        _profile = proxy;
        if (BluetoothProfile.HEADSET == profile) {
            for (BluetoothDevice device: proxy.getConnectedDevices()) {
                lst.add(new BtHeadsetDevice(_ctx, (BluetoothHeadset)proxy, device));
            }
        }
        _cb.deviceListUpdated(lst);
    }

    @Override
    public void onServiceDisconnected(int profile) {
        _cb.deviceListUpdated(_getDeviceList());
    }

    private List<AudioDevice> _getDeviceList() {
        List<AudioDevice>   deviceLst = new ArrayList<>();
        deviceLst = new ArrayList<>();
        deviceLst.add(new Loudspeaker(_ctx));
        deviceLst.add(new Headphone(_ctx));
        return deviceLst;
    }
}
