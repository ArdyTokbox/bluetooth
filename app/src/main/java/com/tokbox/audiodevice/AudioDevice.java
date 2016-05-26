package com.tokbox.audiodevice;

/**
 * Created by ardy on 5/24/16.
 */
public interface AudioDevice {
    enum Profile { BT_ALL, BT_SCO, BT_A2DP };
    void connect();
    void disconnect();
    boolean isConnected();
    Profile getProfile();
}
