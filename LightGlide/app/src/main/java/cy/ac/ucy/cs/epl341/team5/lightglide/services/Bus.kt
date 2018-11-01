package cy.ac.ucy.cs.epl341.team5.lightglide.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.*
import java.util.function.Consumer

class Bus : Service() {
    private val mStreamMap = mutableMapOf<String, MutableList<Consumer<Any>>>()
    // Binder given to clients
    private val mBinder = LocalBinder()

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal// Return this instance of LocalService so clients can call public methods
        val service: Bus
            get() = this@Bus
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    fun onEventChange(string: String,consumer: Consumer<Any>){
        mStreamMap.getOrPut(string) {mutableListOf()}.add(consumer)
    }

}