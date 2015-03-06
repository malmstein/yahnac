package com.malmstein.hnews.connectivity;

import android.content.Context;

import com.novoda.merlin.Merlin;

public class WizMerlin {

    private final Merlin merlin;
    private final NetworkStateObserver networkStateObserver;

    private WizMerlin(Merlin merlin, NetworkStateObserver networkStateObserver) {
        this.merlin = merlin;
        this.networkStateObserver = networkStateObserver;
    }

    public static WizMerlin newInstance(Context context) {
        return newInstance(context, new Callbacks() {

            @Override
            public void onConnectedToNetwork() {
                // no op - nullsafe
            }

            @Override
            public void onDisconnectedFromNetwork() {
                // no op - nullsafe
            }

        });
    }

    public static WizMerlin newInstance(Context context, final Callbacks callbacks) {
        NetworkStateObserver networkStateObserver = new NetworkStateObserver(new NetworkStateObserver.Callbacks() {

            @Override
            public void onConnectedToNetwork() {
                callbacks.onConnectedToNetwork();
            }

            @Override
            public void onDisconnectedFromNetwork() {
                callbacks.onDisconnectedFromNetwork();
            }

        });
        Merlin merlin = new Merlin.Builder()
                .withBindableCallbacks()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .build(context);
        merlin.registerBindable(networkStateObserver);
        merlin.registerConnectable(networkStateObserver);
        merlin.registerDisconnectable(networkStateObserver);
        return new WizMerlin(merlin, networkStateObserver);
    }

    public boolean detectsWorkingNetworkConnection() {
        return networkStateObserver.connectedToNetwork();
    }

    public void onStart() {
        merlin.bind();
    }

    public void onStop() {
        merlin.unbind();
    }

    public interface Callbacks {

        void onConnectedToNetwork();

        void onDisconnectedFromNetwork();

    }

}
