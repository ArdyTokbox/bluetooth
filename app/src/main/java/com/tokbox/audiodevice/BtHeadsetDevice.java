package com.tokbox.audiodevice;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.Context;
import android.media.AudioManager;

import bluetooth.tokbox.com.bluetoothdiscovery.Bluetooth;

/**
 * Created by ardy on 5/24/16.
 */
public class BtHeadsetDevice implements AudioDevice {

    private BluetoothHeadset    _btProfile;
    private BluetoothDevice     _btDevice;

    public BtHeadsetDevice(Context ctx, BluetoothHeadset profile, BluetoothDevice device) {
        _btProfile = profile;
        _btDevice   = device;
    }

    @Override
    public void connect() {
        _btProfile.startVoiceRecognition(_btDevice);
    }

    @Override
    public void disconnect() {
        _btProfile.stopVoiceRecognition(_btDevice);
    }

    @Override
    public boolean isConnected() {
        return (_btProfile.startVoiceRecognition(_btDevice));
    }

    @Override
    public Profile getProfile() {
        return Profile.BT_SCO;
    }

    public String toString() {
        return "Bluetooth: " + _btDevice.getName();
    }
}
