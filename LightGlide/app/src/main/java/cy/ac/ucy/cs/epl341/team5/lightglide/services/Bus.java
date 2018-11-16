package cy.ac.ucy.cs.epl341.team5.lightglide.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class Bus extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    LocalBinder mBinder = new LocalBinder();    // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    private ConcurrentMap<String, ConcurrentLinkedDeque<Consumer<Object>>> mConsumers = new ConcurrentHashMap<>();

    @Override
    public void onCreate() {
        /*
        We now have to register the service on each component it's pulling the data from.
         */
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    }

    public void onEventChange(String s, Consumer<Object> c) {
        Objects.requireNonNull(mConsumers.putIfAbsent(s, new ConcurrentLinkedDeque<Consumer<Object>>())).add(c);
    }

    class LocalBinder extends Binder {
        public Bus getService() {
            return Bus.this;
        }
    }

}