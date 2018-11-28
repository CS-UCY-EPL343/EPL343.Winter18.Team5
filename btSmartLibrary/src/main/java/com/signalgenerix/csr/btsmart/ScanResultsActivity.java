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

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;



/**
 * Activity used to scan for remote Bluetooth Smart devices, show the results in a list, and perform an action (defined
 * by derived class) when a device is selected.
 */
public abstract class ScanResultsActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;

    // Adjust this value to control how long scan should last for. Higher values will drain the battery more.
    // Adjust this value in the derived class.
    protected long mScanPeriodMillis = 200000;

    ListView mScanListView = null;

    private static ArrayList<ScanInfo> mScanResults = new ArrayList<ScanInfo>();

    private static HashSet<String> mScanAddreses = new HashSet<String>();

    private static ScanResultsAdapter mScanResultsAdapter;

    private BluetoothAdapter mBtAdapter = null;

    private static Handler mHandler = new Handler();

    private ImageButton mScanButton = null;

    private boolean mCheckBt = false;


    final Context context = this;

    /**
     * Should be implemented by derived class to handle Bluetooth connection when the user selects a device.
     * 
     * @param deviceToConnect
     *            The Bluetooth device selected by the user.
     */
    abstract protected void connectBluetooth(BluetoothDevice deviceToConnect);


    /**
     * Can be implemented by derived class to filter scan results by UUID.
     *
     * @return Array of UUIDs of services to filter by.
     */
    protected UUID[] uuidFilter() {
        // Default is no filtering by UUID.
        return null;
    }

    /**
     * When the activity is created, set up the UI and start scanning.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prevent screen rotation.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        // Add animated progress indicator in top right corner.
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_scan_results);
        mScanListView = (ListView) this.findViewById(R.id.scanListView);
        mScanResultsAdapter = new ScanResultsAdapter(this, mScanResults);
        mScanListView.setAdapter(mScanResultsAdapter);
        mScanListView.setOnItemClickListener(mDeviceClickListener);

        final BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = btManager.getAdapter();

        mScanButton = (ImageButton) findViewById(R.id.buttonScan);
        mScanButton.setOnClickListener(mScanButtonListener);


        // Register for broadcasts on BluetoothAdapter state change so that we can tell if it has been turned off.
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);
        
        checkEnableBt();
    }

    /**
     * When the Activity is resumed, clear the scan results list.
     */
    @Override
    protected void onResume() {
        super.onResume();
        clearScanResults();
        if (mCheckBt) {
            checkEnableBt();
        }
    }

    @Override
    protected void onPause()  {
       super.onPause();
       scanLeDevice(false);
       // Set flag to check Bluetooth is still enabled when we are resumed.
       // If we end up being destroyed this flag's state will be forgotten, but that's fine because then
       // onCreate will perform the Bluetooth check anyway.
       mCheckBt = true;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
    
    /**
     * Click handler for the scan button that starts scanning for BT Smart devices.
     */
    OnClickListener mScanButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mBtAdapter.isEnabled()) {
                scanLeDevice(true);
            }
        }
    };


    /**
     *  Display a dialogue requesting Bluetooth to be enabled if it isn't already.
     */
    private void checkEnableBt() {
        if (mBtAdapter == null || !mBtAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
    
    /**
     * Callback activated after the user responds to the enable Bluetooth dialogue.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCheckBt = false;
        if (requestCode == REQUEST_ENABLE_BT && resultCode != RESULT_OK) {
            mScanButton.setEnabled(false);
            Toast.makeText(this, "Bluetooth not enabled.", Toast.LENGTH_LONG).show();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_OFF) {
                    Toast.makeText(context, "Bluetooth disabled.", Toast.LENGTH_LONG).show();
                    scanLeDevice(false);
                    clearScanResults();                    
                    mScanButton.setEnabled(false);                    
                }
                else if (state == BluetoothAdapter.STATE_ON) {
                    Toast.makeText(context, "Bluetooth enabled.", Toast.LENGTH_LONG).show();
                    mScanButton.setEnabled(true);
                    mBtAdapter = ((BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
                }
            }
        }
    };
    
    private Runnable scanTimeout = new Runnable() {
        @Override
        public void run() {
            mBtAdapter.stopLeScan(mLeScanCallback);
            setProgressBarIndeterminateVisibility(false);
            mScanButton.setEnabled(true);
        }
    };

	/**
     * Clear the cached scan results, and update the display.
     */
    private void clearScanResults() {
        mScanResults.clear();
        mScanAddreses.clear();
        // Make sure the display is updated; any old devices are removed from the ListView. 
        mScanResultsAdapter.notifyDataSetChanged();
    }
    
    /**
     * Start or stop scanning. Only scan for a limited amount of time defined by SCAN_PERIOD.
     * 
     * @param enable
     *            Set to true to enable scanning, false to stop.
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a predefined scan period.
            mHandler.postDelayed(scanTimeout, mScanPeriodMillis);
            clearScanResults();
            setProgressBarIndeterminateVisibility(true);            
            mBtAdapter.startLeScan(mLeScanCallback);
            mScanButton.setEnabled(false);
        }
        else {
            // Cancel the scan timeout callback if still active or else it may fire later.
            mHandler.removeCallbacks(scanTimeout);
            setProgressBarIndeterminateVisibility(false);
            mBtAdapter.stopLeScan(mLeScanCallback);
            mScanButton.setEnabled(true);
        }
    }

    /**
     * Callback for scan results.
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boolean processAdvert = false;
                    UUID[] filter = uuidFilter();
                    if (filter == null || filter.length == 0) {
                        // No filter was specified.
                        processAdvert = true;
                    }
                    else {
                        // Check the UUID filter.
                        List<UUID> scannedUUIDs = uuidsFromAdvert(scanRecord);
                        for (UUID uuid : filter) {
                            if (scannedUUIDs.contains(uuid)) {
                                processAdvert = true;
                                break;
                            }
                        }
                    }

                    if (processAdvert) {
                        // Check that this isn't a device we have already seen, and add it to the list.
                        if (!mScanAddreses.contains(device.getAddress())) {
                            ScanInfo scanResult = new ScanInfo(device.getName(), device.getAddress(), rssi);
                            mScanAddreses.add(device.getAddress());
                            mScanResults.add(scanResult);
                            mScanResultsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    };

    /**
     * The on-click listener for selecting a device.
     */
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final int _position = position;


            scanLeDevice(false);
            ScanInfo info = (ScanInfo) mScanResultsAdapter.getItem(_position);
            BluetoothDevice deviceToConnect = mBtAdapter.getRemoteDevice(info.address);
                                            //Toast.makeText(getApplicationContext(), "DEBUG:  Bluetooth Device TO Connect: "+deviceToConnect.getName(),  Toast.LENGTH_LONG).show();
             connectBluetooth(deviceToConnect);






        }
    };

    /**
     * Parse the UUIDs from an advert for filtering purposes. This code is based on code from
     * https://code.google.com/p/android/issues/detail?id=59490 and is required to work around issues in the Android API
     * with UUID filtering on scan results.
     * 
     * @param advData
     *            Advertising data from a scan result.
     * @return List of UUIDs found in the advertising data.
     */
    private List<UUID> uuidsFromAdvert(final byte[] advData) {
        final List<UUID> uuids = new ArrayList<UUID>();

        // Pointer within advData to the start of the current ad element being processed.
        int ptrToAdElement = 0;

        // Offsets from start of ad element (i.e. from ptrToAdElement).
        final int OFFSET_LENGTH = 0;
        final int OFFSET_TYPE = 1;
        final int OFFSET_DATA = 2;

        final byte AD_TYPE_UUID_16BIT = 0x02;
        final byte AD_TYPE_UUID_16BIT_LIST = 0x03;
        final byte AD_TYPE_UUID_128BIT = 0x06;
        final byte AD_TYPE_UUID_128BIT_LIST = 0x07;

        final int UUID_16_LENGTH = 2;
        final int UUID_128_LENGTH = 16;

        final String BASE_UUID_FORMAT = "%08x-0000-1000-8000-00805f9b34fb";
        //final String BASE_UUID_FORMAT = "0000xxxx-D102-11E1-9B23-00025B00A5A5";

        while (ptrToAdElement < advData.length - 1) {
            final byte length = advData[ptrToAdElement + OFFSET_LENGTH];

            // The advert data returned by the Android API is padded out with trailing zeroes, so if we reach a
            // zero length then we are done.
            if (length == 0)
                break;

            // Check that there is enough remaining data in the advert for the indicated length.
            if (length > (advData.length - ptrToAdElement - 1)) {
                // This was a malformed advert so return an empty list, even if we got some UUIDs already.
                uuids.clear();
                return uuids;
            }

            final byte adType = advData[ptrToAdElement + OFFSET_TYPE];

            switch (adType) {
            case AD_TYPE_UUID_16BIT:
            case AD_TYPE_UUID_16BIT_LIST:
                for (int i = length; i > UUID_16_LENGTH - 1; i -= UUID_16_LENGTH) {
                    int uuid16 = (advData[ptrToAdElement + OFFSET_DATA] & 0xFF);
                    uuid16 |= ((advData[ptrToAdElement + OFFSET_DATA + 1] & 0xFF) << 8);
                    uuids.add(UUID.fromString(String.format(BASE_UUID_FORMAT, uuid16)));
                }
                break;
            case AD_TYPE_UUID_128BIT:
            case AD_TYPE_UUID_128BIT_LIST:
                for (int i = length; i > UUID_128_LENGTH - 1; i -= UUID_128_LENGTH) {
                    long msb = ((advData[ptrToAdElement + OFFSET_DATA] & 0xFF) << 56);
                    msb |= ((advData[ptrToAdElement + OFFSET_DATA + 1] & 0xFF) << 48);
                    msb |= ((advData[ptrToAdElement + OFFSET_DATA + 2] & 0xFF) << 40);
                    msb |= ((advData[ptrToAdElement + OFFSET_DATA + 3] & 0xFF) << 32);
                    msb |= ((advData[ptrToAdElement + OFFSET_DATA + 4] & 0xFF) << 24);
                    msb |= ((advData[ptrToAdElement + OFFSET_DATA + 5] & 0xFF) << 16);
                    msb |= ((advData[ptrToAdElement + OFFSET_DATA + 6] & 0xFF) << 8);
                    msb |= ((advData[ptrToAdElement + OFFSET_DATA + 7] & 0xFF) << 0);
                    long lsb = ((advData[ptrToAdElement + OFFSET_DATA + 8] & 0xFF) << 56);
                    lsb |= ((advData[ptrToAdElement + OFFSET_DATA + 9] & 0xFF) << 48);
                    lsb |= ((advData[ptrToAdElement + OFFSET_DATA + 10] & 0xFF) << 40);
                    lsb |= ((advData[ptrToAdElement + OFFSET_DATA + 11] & 0xFF) << 32);
                    lsb |= ((advData[ptrToAdElement + OFFSET_DATA + 12] & 0xFF) << 24);
                    lsb |= ((advData[ptrToAdElement + OFFSET_DATA + 13] & 0xFF) << 16);
                    lsb |= ((advData[ptrToAdElement + OFFSET_DATA + 14] & 0xFF) << 8);
                    lsb |= ((advData[ptrToAdElement + OFFSET_DATA + 15] & 0xFF) << 0);
                    uuids.add(new UUID(msb, lsb));
                }
                break;
            default:
                // An advert type we don't care about.
                break;
            }

            // Length byte isn't included in length, hence the +1.
            ptrToAdElement += length + 1;
        }

        return uuids;
    }

    /**
     * The adapter that allows the contents of ScanInfo objects to be displayed in the ListView. The device name,
     * address, RSSI and the icon specified in appearances.xml are displayed.
     */
    private class ScanResultsAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<ScanInfo> data;
        private LayoutInflater inflater = null;

        public ScanResultsAdapter(Activity a, ArrayList<ScanInfo> object) {
            activity = a;
            data = object;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return data.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.list_row, null);

            TextView nameText = (TextView) vi.findViewById(R.id.name);
            TextView addressText = (TextView) vi.findViewById(R.id.address);
            TextView rssiText = (TextView) vi.findViewById(R.id.rssi);

            ScanInfo info = (ScanInfo) data.get(position);
            nameText.setText(info.name);
            addressText.setText(info.address);
            rssiText.setText(String.valueOf(info.rssi) + "dBm");
            return vi;
        }
    }

    private class ScanInfo {
        public String name;
        public String address;
        public int rssi;

        public ScanInfo(String name, String address, int rssi) {
            this.name = name;
            this.address = address;
            this.rssi = rssi;
        }
    }








}
