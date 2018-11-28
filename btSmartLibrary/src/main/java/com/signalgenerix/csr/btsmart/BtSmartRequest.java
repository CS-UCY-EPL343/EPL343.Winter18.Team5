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

import java.util.UUID;

import android.os.Handler;

// A simple data structure to be used in the request queue.
class BtSmartRequest {
	public enum RequestType {
		CHARACTERISTIC_NOTIFICATION, READ_CHARACTERISTIC, READ_DESCRIPTOR, READ_RSSI, WRITE_CHARACTERISTIC, WRITE_DESCRIPTOR
	};

	public RequestType type;
	public UUID serviceUuid;
	public UUID characteristicUuid;
	public UUID descriptorUuid;
	public Handler notifyHandler;
	public int requestId;
	public byte [] value;
	
	public BtSmartRequest(RequestType type, int requestId, UUID service, UUID characteristic, UUID descriptor,
			Handler handler) {
		this.type = type;
		this.requestId = requestId;
		this.serviceUuid = service;
		this.characteristicUuid = characteristic;
		this.descriptorUuid = descriptor;
		this.notifyHandler = handler;
		this.value = null;
	}
	
	public BtSmartRequest(RequestType type, int requestId, UUID service, UUID characteristic, UUID descriptor,
			Handler handler, byte [] value) {
		this.type = type;
		this.requestId = requestId;
		this.serviceUuid = service;
		this.characteristicUuid = characteristic;
		this.descriptorUuid = descriptor;
		this.notifyHandler = handler;
		this.value = value;
	}
}
