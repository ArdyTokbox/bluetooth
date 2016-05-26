package com.tokbox.audiodevice;

import android.content.Context;
import android.media.AudioManager;

public class Loudspeaker implements AudioDevice {

    private AudioManager    _audioManager;

    public Loudspeaker(Context ctx) {
        _audioManager = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
    }


    public void connect() {
        _audioManager.setSpeakerphoneOn(true);
    }

    @Override
    public void disconnect() {
        _audioManager.setSpeakerphoneOn(false);
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
        return "Loudspeaker";
    }

}
