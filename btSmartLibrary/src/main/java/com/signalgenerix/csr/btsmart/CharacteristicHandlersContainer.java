/******************************************************************************
 *  Copyright (C) Cambridge Silicon Radio Limited 2014
 *
 *  This software is provided to the customer for evaluation
 *  purposes only and, as such early feedback on performance and operation
 *  is anticipated. The software source code is subject to change and
 *  not intended for production. Use of developmental release software is
 *  at the user's own risk. This software is provided "as is," and CSR
 *  cautions users to determine for themselves the suitability of using the
 *  beta release version of this software. CSR makes no warranty or
 *  representation whatsoever of merchantability or fitness of the product
 *  for any particular purpose or use. In no event shall CSR be liable for
 *  any consequential, incidental or special damages whatsoever arising out
 *  of the use of or inability to use this software, even if the user has
 *  advised CSR of the possibility of such damages.
 *
 ******************************************************************************/

package com.signalgenerix.csr.btsmart;

import java.util.HashMap;
import java.util.UUID;
import android.os.Handler;

/**
 * A class to hold handlers that want to receive notifications about characteristics.
 * Holds a map of Service UUIDs to secondary maps that map characteristic UUIDs to the Handler.
 * Currently only supports one Handler per characteristic.
 */
public class CharacteristicHandlersContainer {
    private HashMap<UUID, HashMap<UUID, Handler>> mHandlers = new HashMap<UUID, HashMap<UUID, Handler>>();

    /**
     * Add a new characteristic handler.
     * 
     * @param service
     *            The UUID of the service that contains the characteristic to register a handler for.
     * @param characteristic
     *            The UUID of the characteristic to register a handler for.
     * @param notifyHandler
     *            The Handler itself.
     */
    public void addHandler(UUID service, UUID characteristic, Handler notifyHandler) {
        HashMap<UUID, Handler> subMap = mHandlers.get(service);
        if (subMap == null) {
            subMap = new HashMap<UUID, Handler>();
            mHandlers.put(service, subMap);
        }
        subMap.put(characteristic, notifyHandler);
    }

    /**
     * Remove a handler from the map.
     * 
     * @param service
     *            The UUID of the service that contains the characteristic to remove.
     * @param characteristic
     *            The UUID of the characteristic to remove.
     */
    public void removeHandler(UUID service, UUID characteristic) {
        HashMap<UUID, Handler> subMap = mHandlers.get(service);
        if (subMap != null) {
            subMap.remove(characteristic);
        }
    }

    /**
     * Retrieve a handler.
     * 
     * @param service
     *            The UUID of the service that contains the characteristic of interest.
     * @param characteristic
     *            The UUID of the characteristic who's handler should be returned.
     * @return The Handler object.
     */
    public Handler getHandler(UUID service, UUID characteristic) {
        HashMap<UUID, Handler> subMap = mHandlers.get(service);
        if (subMap == null) {
            return null;
        }
        return subMap.get(characteristic);
    }

}