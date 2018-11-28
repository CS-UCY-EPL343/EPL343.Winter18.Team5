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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import com.signalgenerix.csr.btsmart.BtSmartRequest.RequestType;
import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;

/**
 * Service for communication with a Bluetooth Smart device.
 * Once bound to this service, an Activity can connect and make requests of a remote Bluetooth device, and receive
 * asynchronous updates via the registered handler.
 */
@TargetApi(18)
public class BtSmartService extends Service {
	// // Enumerated type for Bluetooth Smart UUIDs.
	public enum BtSmartUuid {
		HRP_SERVICE("0000180d-0000-1000-8000-00805f9b34fb"), 
		HEART_RATE_MEASUREMENT("00002a37-0000-1000-8000-00805f9b34fb"),
		HEART_RATE_LOCATION("00002a38-0000-1000-8000-00805f9b34fb"),
        THS_SERVICE("0000181A-0000-1000-8000-00805f9b34fb"),
        THS_LOCATION("0000D004-D102-11E1-9B23-00025B00A5A5"),
        THS_TH_MEASUREMENT("00002A6E-0000-1000-8000-00805f9b34fb"),
		CCC("00002902-0000-1000-8000-00805f9b34fb"), 
		IMMEDIATE_ALERT("00001802-0000-1000-8000-00805f9b34fb"),
		ALERT_LEVEL("00002a06-0000-1000-8000-00805f9b34fb"),
		ALERT_NOTIFICATION_SERVICE("00001811-0000-1000-8000-00805f9b34fb"),
		ALERT_NOTIFICATION_CONTROL_POINT("00002a44-0000-1000-8000-00805f9b34fb"),
		UNREAD_ALERT_STATUS("00002a45-0000-1000-8000-00805f9b34fb"),
		NEW_ALERT("00002a46-0000-1000-8000-00805f9b34fb"),
		NEW_ALERT_CATEGORY("00002a47-0000-1000-8000-00805f9b34fb"),
		UNREAD_ALERT_CATEGORY("00002a48-0000-1000-8000-00805f9b34fb"),
		DEVICE_INFORMATION_SERVICE("0000180A-0000-1000-8000-00805f9b34fb"),
		MANUFACTURER_NAME("00002A29-0000-1000-8000-00805f9b34fb"),
		MODEL_NUMBER("00002a24-0000-1000-8000-00805f9b34fb"),
		SERIAL_NUMBER("00002a25-0000-1000-8000-00805f9b34fb"),
		HARDWARE_REVISION("00002a27-0000-1000-8000-00805f9b34fb"),
		FIRMWARE_REVISION("00002a26-0000-1000-8000-00805f9b34fb"),
		SOFTWARE_REVISION("00002a28-0000-1000-8000-00805f9b34fb"),
		BATTERY_SERVICE("0000180f-0000-1000-8000-00805f9b34fb"),
		BATTERY_LEVEL("00002a19-0000-1000-8000-00805f9b34fb"),
		CSC_SERVICE("00001816-0000-1000-8000-00805f9b34fb"),
		CSC_MEASUREMENT("0002a5b-0000-1000-8000-00805f9b34fb"),
		CSC_FEATURE("00002a5c-0000-1000-8000-00805f9b34fb"),
		SENSOR_LOCATION("00002a5d-0000-1000-8000-00805f9b34fb"),
		CSC_CONTROL_POINT("00002a55-0000-1000-8000-00805f9b34fb"),
		RSC_SERVICE("00001814-0000-1000-8000-00805f9b34fb"),
		RSC_MEASUREMENT("00002a53-0000-1000-8000-00805f9b34fb"),		
		SC_CONTROL_POINT("00002a55-0000-1000-8000-00805f9b34fb"),
		BP_SERVICE("00001810-0000-1000-8000-00805f9b34fb"),
		BP_MEASUREMENT("00002a35-0000-1000-8000-00805f9b34fb");
		
		private UUID value;

		private BtSmartUuid(String value) {
			this.value = UUID.fromString(value);
		}

		public UUID getUuid() {
			return value;
		}

		public ParcelUuid getParcelable() {
			return new ParcelUuid(this.value);
		}

		// Lookup table to allow reverse lookup.
		private static final HashMap<UUID, BtSmartUuid> lookup = new HashMap<UUID, BtSmartUuid>();

		// Populate the lookup table at load time
		static {
			for (BtSmartUuid s : EnumSet.allOf(BtSmartUuid.class))
				lookup.put(s.value, s);
		}

		/**
		 * Reverse look up UUID -> BtSmartUuid
		 * 
		 * @param uuid
		 *            The UUID to get a enumerated value for.
		 * @return Enumerated value of type BtSmartUuid.
		 */
		public static BtSmartUuid get(UUID uuid) {
			return lookup.get(uuid);
		}
	}

	// // Messages to send to registered handlers.
	public static final int MESSAGE_SCAN_RESULT = 1;
	public static final int MESSAGE_CONNECTED = 2;
	public static final int MESSAGE_CHARACTERISTIC_VALUE = 3;
	public static final int MESSAGE_DISCONNECTED = 4;
	public static final int MESSAGE_REQUEST_FAILED = 5;
	public static final int MESSAGE_CHARACTERISTIC_READ = 6;
	public static final int MESSAGE_CHARACTERISTIC_WRITE = 7;
	public static final int MESSAGE_DESCRIPTOR_READ = 8;
	public static final int MESSAGE_DESCRIPTOR_WRITE = 9;
	public static final int MESSAGE_DESCRIPTOR_VALUE = 10;
	public static final int MESSAGE_WRITE_COMPLETE = 11;
	
	// // Keys to use for sending extra data with above messages.
	public static final String EXTRA_SCAN_RECORD = "SCANRECORD";
	public static final String EXTRA_VALUE = "CVALUE";
	public static final String EXTRA_RSSI = "RSSI";
	public static final String EXTRA_APPEARANCE_KEY = "APPEARKEY";
	public static final String EXTRA_APPEARANCE_NAME = "APPEARNAME";
	public static final String EXTRA_APPEARANCE_ICON = "APPEARICON";
	public static final String EXTRA_SERVICE_UUID = "SERVUUID";
	public static final String EXTRA_CHARACTERISTIC_UUID = "CHARUUID";
	public static final String EXTRA_DESCRIPTOR_UUID = "DESCUUID";
	public static final String EXTRA_REQUEST_ID = "REQUESTID";
	public static final String EXTRA_CLIENT_REQUEST_ID = "CLIENTREQUESTID";
	
	
	public static final int APPEARANCE_UNKNOWN = 0;

	private final IBinder mBinder = new LocalBinder();
	private Handler mClientDeviceHandler = null;
	private BluetoothManager mBtManager = null;
	private BluetoothAdapter mBtAdapter = null;
	private BluetoothGatt mGattClient = null;		

	private CharacteristicHandlersContainer mNotificationHandlers = new CharacteristicHandlersContainer();

	// Characteristic currently waiting to have a notification value written to it.
	private BluetoothGattCharacteristic mPendingCharacteristic = null;

	Queue<BtSmartRequest> requestQueue = new LinkedList<BtSmartRequest>();

	BtSmartRequest currentRequest = null;

	/**
	 * Class used for the client Binder. Because we know this service always runs in the same process as its clients, we
	 * don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		public BtSmartService getService() {
			// Return this instance of BtSmartService so clients can call public
			// methods.
			return BtSmartService.this;
		}
	}

	/**
	 * Return the interface to this service.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/**
	 * Initialise the service.
	 */
	@Override
	public void onCreate() {
		if (mBtAdapter == null) {
			mBtManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			mBtAdapter = mBtManager.getAdapter();
		}
	}

	/**
	 * When the service is destroyed, make sure to close the Bluetooth connection.
	 */
	@Override
	public void onDestroy() {
		if (mGattClient != null)
			mGattClient.close();		
		super.onDestroy();
	}

	/**
	 * Parse a scan record and retrieve the appearance value.
	 * 
	 * @param scanRecord
	 *            The scan record to search.
	 * @return Appearance value as defined in assigned numbers document.
	 */
	public static int getAppearanceFromScanRecord(byte[] scanRecord) {
		final int STATE_LENGTH = 0;
		final int STATE_AD_TYPE = 1;
		final int STATE_APPEARANCE_DATA = 2;

		final int AD_TYPE_APPEARANCE = 0x19;

		int state = STATE_LENGTH;

		byte length = 0;

		for (int i = 0; i < scanRecord.length; i++) {
			switch (state) {
			case STATE_LENGTH:
				length = scanRecord[i];
				state++;
				break;
			case STATE_AD_TYPE:
				// Found what we're looking for. Set the next state to read the value.
				if (scanRecord[i] == AD_TYPE_APPEARANCE) {
					state = STATE_APPEARANCE_DATA;
				} else {
					// Skip the rest of this data as it's not an appearance.
					i += (length - 1);
					state = STATE_LENGTH;
				}
				break;
			case STATE_APPEARANCE_DATA:
				// This is a 16-bit value in little-endian format.
				int MSB = (int) scanRecord[i + 1] << 8;
				int LSB = (int) scanRecord[i];
				return (MSB + LSB);
			}
		}
		// Appearance data was not found in the scan data.
		return APPEARANCE_UNKNOWN;
	}

	/**
	 * Connect to a remote Bluetooth Smart device. The deviceHandler will receive MESSAGE_CONNECTED on
     * connection success.
	 * 
	 * @param device
	 *            The BluetothDevice to connect to.
	 * @return Boolean success value.
	 */
	public void connectAsClient(BluetoothDevice device, Handler deviceHandler) {
		mClientDeviceHandler = deviceHandler;
		mGattClient = device.connectGatt(this, false, mGattCallbacks);		
	}
	
	/**
	 * Disconnect from the currently connected Bluetooth Smart device.
	 */
	public void disconnect() {
		if (mGattClient != null) {
			mGattClient.disconnect();
			mGattClient.close();
		}		
	}

	/**
	 * Enable notifications for a particular characteristic and register a handler for those notifications. If a request
	 * is currently in progress then queue it.
	 * 
	 * @param requestId
	 *            An id provided by the caller that will be included in messages to the handler.
	 * @param serviceUuid
	 *            The service that contains the characteristic of interest.
	 * @param characteristicUuid
	 *            The characteristic to register for.
	 * @param notifyHandler
	 *            The handler that will receive MESSAGE_CHARACTERISTIC_VALUE messages containing a byte array named
	 *            EXTRA_VALUE.
	 */
	public void requestCharacteristicNotification(int requestId, UUID serviceUuid, UUID characteristicUuid,
			Handler notifyHandler) {
		if (currentRequest == null) {
			performNotificationRequest(requestId, serviceUuid, characteristicUuid, notifyHandler);
		} else {
			requestQueue.add(new BtSmartRequest(RequestType.CHARACTERISTIC_NOTIFICATION, requestId, serviceUuid,
					characteristicUuid, null, notifyHandler));
		}
	}

	/**
	 * Request the current value of a characteristic. This will return the value once only in a
	 * MESSAGE_CHARACTERISTIC_VALUE. If a request is currently in progress then queue it.
	 * 
	 * @param requestId
	 *            An id provided by the caller that will be included in messages to the handler.
	 * @param service
	 *            The UUID of the service that contains the characteristic of interest.
	 * @param characteristic
	 *            The UUID of the characteristic.
	 * @param valueHandler
	 *            The handler that will receive MESSAGE_CHARACTERISTIC_VALUE messages containing a byte array named
	 *            EXTRA_VALUE.
	 */
	public void requestCharacteristicValue(int requestId, UUID service, UUID characteristic, Handler valueHandler) {
		if (currentRequest == null) {
			performCharacValueRequest(requestId, service, characteristic, valueHandler);
		} else {
			requestQueue.add(new BtSmartRequest(RequestType.READ_CHARACTERISTIC, requestId, service, characteristic,
					null, valueHandler));
		}
	}

	/**
	 * Request the current value of a descriptor. This will return the value once only in a MESSAGE_DESCRIPTOR_VALUE. If
	 * a request is currently in progress then queue it. Use requestCharacteristicNotification() for constant updates
	 * when a characteristic value changes.
	 * 
	 * @param requestId
	 *            An id provided by the caller that will be included in messages to the handler.
	 * @param service
	 *            The UUID of the service that contains the characteristic and descriptor of interest.
	 * @param characteristic
	 *            The UUID of the characteristic.
	 * @param descriptor
	 *            The UUID of the descriptor.
	 * @param valueHandler
	 *            The handler that will receive MESSAGE_DESCRIPTOR_VALUE messages containing a byte array named
	 *            EXTRA_VALUE.
	 */
	public void requestDescriptorValue(int requestId, UUID service, UUID characteristic, UUID descriptor,
			Handler valueHandler) {
		if (currentRequest == null) {
			performDescValueRequest(requestId, service, characteristic, descriptor, valueHandler);
		} else {
			requestQueue.add(new BtSmartRequest(RequestType.READ_DESCRIPTOR, requestId, service, characteristic,
					descriptor, valueHandler));
		}
	}

	/**
	 * Write a value to a charactersitic.
     * @param requestId
     *         An id to uniquely identify the request. Included in messages to the handler.
     * @param service
     *         The service that contains the characteristic to write.
     * @param characteristic
     *         The characteristic to write.
     * @param value
     *         The value to write to the characteristic.
     * @param msgHandler
     *         The handler that will receive messages about the result of the write operation.         
     */
    public void writeCharacteristicValue(int requestId, UUID service, UUID characteristic, byte[] value,
            Handler msgHandler) {        
        if (currentRequest == null) {
            performCharacWrite(requestId, service, characteristic, msgHandler, value);
        }
        else {
            requestQueue.add(new BtSmartRequest(RequestType.WRITE_CHARACTERISTIC, requestId, service, characteristic,
                    null, msgHandler, value));
        }
    }

	/**
	 * Helper function to send a message to a handler with no parameters.
	 * 
	 * @param h
	 *            The Handler to send the message to.
	 * @param msgId
	 *            The message identifier to send. Use one of the defined constants.
	 */
	private void sendMessage(Handler h, int msgId) {
		if (h != null) {
			Message.obtain(h, msgId).sendToTarget();
		}
	}

	/**
	 * Helper function to send a message to a handler with no parameters except the request ID.
	 * 
	 * @param h
	 *            The Handler to send the message to.
	 * @param msgId
	 *            The message identifier to send. Use one of the defined constants.
	 * @param requestId
	 *            The request ID provided by the client of this Service.
	 */
	private void sendMessage(Handler h, int requestId, int msgId) {
		if (h != null) {
			Bundle messageBundle = new Bundle();
			Message msg = Message.obtain(h, msgId);
			messageBundle.putInt(EXTRA_CLIENT_REQUEST_ID, requestId);
			msg.setData(messageBundle);
			msg.sendToTarget();
		}
	}

	/**
	 * This is where most of the interesting stuff happens in response to changes in BLE state for a client.
	 */
	private BluetoothGattCallback mGattCallbacks = new BluetoothGattCallback() {

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED && mGattClient != null) {
				// Get all the available services. This allows us to query them later. The result of this being
				// successful will be a call to onServicesDiscovered().
			    // Don't tell the handler that we are connected until the services have been discovered.
				mGattClient.discoverServices();
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				sendMessage(mClientDeviceHandler, MESSAGE_DISCONNECTED);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				// Services are discovered, so now we are ready for characteristic handlers to register themselves.
				sendMessage(mClientDeviceHandler, MESSAGE_CONNECTED);
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			// A notification for a characteristic has been received, so notify
			// the registered Handler.
			Handler notificationHandler = mNotificationHandlers.getHandler(characteristic.getService().getUuid(),
					characteristic.getUuid());
			if (notificationHandler != null) {
				Bundle messageBundle = new Bundle();
				Message msg = Message.obtain(notificationHandler, MESSAGE_CHARACTERISTIC_VALUE);
				messageBundle.putByteArray(EXTRA_VALUE, characteristic.getValue());
				messageBundle.putParcelable(EXTRA_SERVICE_UUID, new ParcelUuid(characteristic.getService().getUuid()));
				messageBundle.putParcelable(EXTRA_CHARACTERISTIC_UUID, new ParcelUuid(characteristic.getUuid()));
				msg.setData(messageBundle);
				msg.sendToTarget();
			}
		}

		/**
		 * After calling registerForNotification this callback should trigger, and then we can perform the actual
		 * enable. It could also be called when a descriptor was requested directly, so that case is handled too.
		 */
		@Override
		public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
			if (currentRequest.type == RequestType.CHARACTERISTIC_NOTIFICATION) {
				// Descriptor was requested indirectly as part of registration for notifications.
				if (status != BluetoothGatt.GATT_SUCCESS) {
					sendMessage(currentRequest.notifyHandler, currentRequest.requestId, MESSAGE_REQUEST_FAILED);
					mNotificationHandlers
							.removeHandler(characteristic.getService().getUuid(), characteristic.getUuid());
				}
				if (characteristic.getService().getUuid().compareTo(mPendingCharacteristic.getService().getUuid()) == 0
						&& characteristic.getUuid().compareTo(mPendingCharacteristic.getUuid()) == 0) {
					mNotificationHandlers.addHandler(characteristic.getService().getUuid(), characteristic.getUuid(),
							currentRequest.notifyHandler);
					if (!enableNotification(true, characteristic)) {
						sendMessage(currentRequest.notifyHandler, currentRequest.requestId, MESSAGE_REQUEST_FAILED);
						mNotificationHandlers.removeHandler(characteristic.getService().getUuid(),
								characteristic.getUuid());
					}
					// Don't call processNextRequest yet as this request isn't
					// complete until onDescriptorWrite() triggers.
				}
			} else if (currentRequest.type == RequestType.READ_DESCRIPTOR) {
				// Descriptor was requested directly.
				if (status == BluetoothGatt.GATT_SUCCESS) {
					Bundle messageBundle = new Bundle();
					Message msg = Message.obtain(currentRequest.notifyHandler, MESSAGE_DESCRIPTOR_VALUE);
					messageBundle.putByteArray(EXTRA_VALUE, characteristic.getValue());
					messageBundle.putParcelable(EXTRA_SERVICE_UUID, new ParcelUuid(characteristic.getService().getUuid()));
	                messageBundle.putParcelable(EXTRA_CHARACTERISTIC_UUID, new ParcelUuid(characteristic.getUuid()));
	                messageBundle.putParcelable(EXTRA_DESCRIPTOR_UUID, new ParcelUuid(descriptor.getUuid()));
					messageBundle.putInt(EXTRA_CLIENT_REQUEST_ID, currentRequest.requestId);
					msg.setData(messageBundle);
					msg.sendToTarget();
				} else {
					sendMessage(currentRequest.notifyHandler, currentRequest.requestId, MESSAGE_REQUEST_FAILED);
				}
				// This request is now complete, so see if there is another.
				processNextRequest();
			}
		}

		/**
		 * After writing the CCC for a notification this callback should trigger. It could also be called when a
		 * descriptor write was requested directly, so that case is handled too.
		 */
		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
			if (currentRequest.type == RequestType.CHARACTERISTIC_NOTIFICATION) {
				if (status != BluetoothGatt.GATT_SUCCESS) {
					sendMessage(currentRequest.notifyHandler, currentRequest.requestId, MESSAGE_REQUEST_FAILED);
					mNotificationHandlers
							.removeHandler(characteristic.getService().getUuid(), characteristic.getUuid());
				}
			} else if (currentRequest.type == RequestType.WRITE_DESCRIPTOR) {
				// TODO: If descriptor writing is implemented, add code here to send message to handler.
			}
			processNextRequest();
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			// This can only be in response to the current request as there can't be more than one in progress.
			// So check this is what we were expecting.
			if (currentRequest.type == RequestType.READ_CHARACTERISTIC) {
				if (currentRequest.notifyHandler != null) {
					if (status == BluetoothGatt.GATT_SUCCESS) {
						Bundle messageBundle = new Bundle();
						Message msg = Message.obtain(currentRequest.notifyHandler, MESSAGE_CHARACTERISTIC_VALUE);
						messageBundle.putByteArray(EXTRA_VALUE, characteristic.getValue());
						messageBundle.putParcelable(EXTRA_SERVICE_UUID, new ParcelUuid(characteristic.getService().getUuid()));
	                    messageBundle.putParcelable(EXTRA_CHARACTERISTIC_UUID, new ParcelUuid(characteristic.getUuid()));	                    
						messageBundle.putInt(EXTRA_CLIENT_REQUEST_ID, currentRequest.requestId);
						msg.setData(messageBundle);
						msg.sendToTarget();
					} else {
						sendMessage(currentRequest.notifyHandler, currentRequest.requestId, MESSAGE_REQUEST_FAILED);
					}
				}
				processNextRequest();
			}
		}
		
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			if (currentRequest.type == RequestType.WRITE_CHARACTERISTIC) {
				if (currentRequest.notifyHandler != null) {
					if (status == BluetoothGatt.GATT_SUCCESS) {
						Bundle messageBundle = new Bundle();
						Message msg = Message.obtain(currentRequest.notifyHandler, MESSAGE_WRITE_COMPLETE);
						messageBundle.putByteArray(EXTRA_VALUE, characteristic.getValue());
						messageBundle.putParcelable(EXTRA_SERVICE_UUID, new ParcelUuid(characteristic.getService().getUuid()));
	                    messageBundle.putParcelable(EXTRA_CHARACTERISTIC_UUID, new ParcelUuid(characteristic.getUuid()));	                    
						messageBundle.putInt(EXTRA_CLIENT_REQUEST_ID, currentRequest.requestId);
						msg.setData(messageBundle);
						msg.sendToTarget();
					} else {
						sendMessage(currentRequest.notifyHandler, currentRequest.requestId, MESSAGE_REQUEST_FAILED);
					}
				}
				processNextRequest();
			}
		}
		
	};
	
	/**
	 * Process the next request in the queue for some BLE action (such as characteristic read). This is required because
	 * the Android 4.3 BLE stack only allows one active request at a time.
	 */
	private void processNextRequest() {
		if (requestQueue.isEmpty()) {
			currentRequest = null;
			return;
		}
		BtSmartRequest request = requestQueue.remove();
		switch (request.type) {
		case CHARACTERISTIC_NOTIFICATION:
			performNotificationRequest(request.requestId, request.serviceUuid, request.characteristicUuid,
					request.notifyHandler);
			break;
		case READ_CHARACTERISTIC:
			performCharacValueRequest(request.requestId, request.serviceUuid, request.characteristicUuid,
					request.notifyHandler);
			break;
		case READ_DESCRIPTOR:
			performDescValueRequest(request.requestId, request.serviceUuid, request.characteristicUuid,
					request.descriptorUuid, request.notifyHandler);
			break;
		case WRITE_CHARACTERISTIC:
			performCharacWrite(request.requestId, request.serviceUuid, request.characteristicUuid,
					request.notifyHandler, request.value);
		default:
			break;
		}
	}

	/**
	 * Perform the notification request now.
	 * 
	 * @param requestId
	 *            An id provided by the caller that will be included in messages to the handler.
	 * @param service
	 *            The service that contains the characteristic of interest.
	 * @param characteristic
	 *            The characteristic to register for.
	 * @param notifyHandler
	 *            The handler that will receive MESSAGE_CHARACTERISTIC_VALUE messages containing a byte array named
	 *            EXTRA_VALUE.
	 */
	private void performNotificationRequest(int requestId, UUID service, UUID characteristic, Handler notifyHandler) {
		// This currentRequest object will be used when we get the value back asynchronously in the callback.
		currentRequest = new BtSmartRequest(RequestType.CHARACTERISTIC_NOTIFICATION, requestId, service,
				characteristic, null, notifyHandler);
		
		BluetoothGattService serviceObject = mGattClient.getService(currentRequest.serviceUuid);
		if (serviceObject != null) {
			mPendingCharacteristic = serviceObject.getCharacteristic(characteristic);
			if (mPendingCharacteristic != null) {
				BluetoothGattDescriptor clientCharacteristicConfig = mPendingCharacteristic
						.getDescriptor(BtSmartUuid.CCC.value);
				// If the CCC exists then attempt to read it.
				if (clientCharacteristicConfig == null || !mGattClient.readDescriptor(clientCharacteristicConfig)) {
					// CCC didn't exist or the read failed early.
				    // Send the failed message and move onto the next request.
					sendMessage(notifyHandler, currentRequest.requestId, MESSAGE_REQUEST_FAILED);
					processNextRequest();
				}
			}
		}
	}

	/**
	 * Perform the characteristic value request now.
	 * 
	 * @param requestId
	 *            An id provided by the caller that will be included in messages to the handler.
	 * @param service
	 *            The service that contains the characteristic of interest.
	 * @param characteristic
	 *            The characteristic to get the value of.
	 * @param valueHandler
	 *            The handler that will receive MESSAGE_CHARACTERISTIC_VALUE messages containing a byte array named
	 *            EXTRA_VALUE.
	 */
	private void performCharacValueRequest(int requestId, UUID service, UUID characteristic, Handler valueHandler) {
		// This currentRequest object will be used when we get the value back asynchronously in the callback.
		currentRequest = new BtSmartRequest(RequestType.READ_CHARACTERISTIC, requestId, service, characteristic, null,
				valueHandler);
		BluetoothGattService serviceObject = mGattClient.getService(service);
		if (serviceObject != null) {
			BluetoothGattCharacteristic characteristicObject = serviceObject.getCharacteristic(characteristic);
			if (characteristicObject != null) {
				if (!mGattClient.readCharacteristic(characteristicObject)) {
					sendMessage(valueHandler, currentRequest.requestId, MESSAGE_REQUEST_FAILED);
					processNextRequest();
				}
			}
		}
	}

	/**
	 * Perform the descriptor value request now.
	 * 
	 * @param requestId
	 *            An id provided by the caller that will be included in messages to the handler.
	 * @param serviceUuid
	 *            The service that contains the characteristic of interest.
	 * @param characteristic
	 *            The characteristic that contains the descriptor of interest.
	 * @param descriptor
	 *            The descriptor to get the value of.
	 * @param valueHandler
	 *            The handler that will receive MESSAGE_CHARACTERISTIC_VALUE messages containing a byte array named
	 *            EXTRA_VALUE.
	 */
	private void performDescValueRequest(int requestId, UUID service, UUID characteristic, UUID descriptor,
			Handler valueHandler) {
		// This currentRequest object will be used when we get the value back asynchronously in the callback.
		currentRequest = new BtSmartRequest(RequestType.READ_CHARACTERISTIC, requestId, service, characteristic,
				descriptor, valueHandler);
		BluetoothGattService serviceObject = mGattClient.getService(service);
		if (serviceObject != null) {
			BluetoothGattCharacteristic characteristicObject = serviceObject.getCharacteristic(characteristic);
			if (characteristicObject != null) {
				BluetoothGattDescriptor descriptorObject = characteristicObject.getDescriptor(descriptor);
				if (descriptorObject != null) {
					if (!mGattClient.readDescriptor(descriptorObject)) {
						sendMessage(valueHandler, currentRequest.requestId, MESSAGE_REQUEST_FAILED);
						processNextRequest();
					}
				}
			}
		}
	}

	/**
	 * Perform the charactersitic write now.
	 * 
	 * @param requestId
	 *         An id to uniquely identify the request. Included in messages to the handler.
	 * @param service
	 *         The service that contains the characteristic to write.
	 * @param characteristic
	 *         The characteristic to write.
	 * @param msgHandler
	 *         The handler that will receive messages about the result of the write operation.
	 * @param value
	 *         The value to write to the characteristic.
	 */
    private void performCharacWrite(int requestId, UUID service, UUID characteristic, Handler msgHandler, byte[] value) {
        currentRequest =
                new BtSmartRequest(RequestType.WRITE_CHARACTERISTIC, requestId, service, characteristic, null,
                        msgHandler, value);
        BluetoothGattService serviceObject = mGattClient.getService(service);
        if (serviceObject != null) {
            BluetoothGattCharacteristic characteristicObject = serviceObject.getCharacteristic(characteristic);
            if (characteristicObject != null) {
                characteristicObject.setValue(value);
                if (!mGattClient.writeCharacteristic(characteristicObject)) {
                    sendMessage(msgHandler, currentRequest.requestId, MESSAGE_REQUEST_FAILED);
                    processNextRequest();
                }
            }
        }
    }
	
	/**
	 * Write to the CCC to enable or disable notifications.
	 * 
	 * @param enable
	 *            Boolean indicating whether the notification should be enabled or disabled.
	 * @param characteristic
	 *            The CCC to write to.
	 * @return Boolean result of operation.
	 */
	private boolean enableNotification(boolean enable, BluetoothGattCharacteristic characteristic) {
		if (mGattClient == null) {
			throw new NullPointerException("GATT client not started.");
		}
		if (!mGattClient.setCharacteristicNotification(characteristic, enable)) {
			return false;
		}
		BluetoothGattDescriptor clientConfig = characteristic.getDescriptor(BtSmartUuid.CCC.value);
		if (clientConfig == null) {
			return false;
		}
		if (enable) {
			clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		} else {
			clientConfig.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
		}

		return mGattClient.writeDescriptor(clientConfig);
	}

}
