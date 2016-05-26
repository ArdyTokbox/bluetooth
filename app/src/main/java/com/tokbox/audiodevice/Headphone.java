package com.tokbox.audiodevice;

import android.content.Context;
import android.media.AudioManager;

public class Headphone implements AudioDevice {

    private AudioManager    _audioManager;

    public Headphone(Context ctx) {
        _audioManager = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void connect() {
        // does nothing
    }

    @Override
    public void disconnect() {
        // does nothing
    }

    @Override
    public boolean isConnected() {
        return _audioManager.isSpeakerphoneOn();
    }

    @Override
    public Profile getProfile() {
        return Profile.BT_ALL;
    }

    public String toString() {
        return "Headphones";
    }
}
