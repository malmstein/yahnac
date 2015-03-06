package com.malmstein.hnews.connectivity;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

public class NetworkStateObserver implements Connectable, Disconnectable, Bindable {

    private final Callbacks callbacks;

    private boolean isConnected;

    public NetworkStateObserver(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void onBind(NetworkStatus networkStatus) {
        if (networkStatus.isAvailable()) {
            onConnect();
        } else {
            onDisconnect();
        }
    }

    @Override
    public void onConnect() {
        isConnected = true;
        callbacks.onConnectedToNetwork();
    }

    @Override
    public void onDisconnect() {
        isConnected = false;
        callbacks.onDisconnectedFromNetwork();
    }

    public boolean connectedToNetwork() {
        return isConnected;
    }

    public interface Callbacks {

        void onConnectedToNetwork();

        void onDisconnectedFromNetwork();

    }

}
