package com.tool.ibeacon.manager.example;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.logg.Logg;
import com.logg.config.LoggConfiguration;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tool.bluetooth.detector.BluetoothDetectorCallBack;
import com.tool.bluetooth.detector.BluetoothDetector;
import com.tool.bluetooth.detector.BluetoothDetectorHandler;
import com.tool.bluetooth.detector.config.BluetoothFilter;
import com.tool.bluetooth.detector.utils.BleUtil;
import com.tool.bluetooth.detector.entity.BeaconDevice;
import com.tool.bluetooth.detector.utils.Utils;
import com.tool.common.utils.AppUtils;
import com.tool.common.utils.PermissionUtils;

import java.util.ArrayList;

/**
 *
 */
public class ScanActivity extends Activity implements BluetoothDetectorCallBack
//        , BluetoothAdapter.LeScanCallback
{

//    private BluetoothAdapter mBTAdapter;
//    private DeviceAdapter mDeviceAdapter;
//    private boolean mIsScanning;

    private BluetoothDetectorHandler detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        LoggConfiguration configuration = new LoggConfiguration.Buidler()
                .setDebug(BuildConfig.DEBUG_FLAG)
//                .setTag("test")// 自定义全局Tag
                .build();
        Logg.init(configuration);

//        init();

        detector = BluetoothDetector.getInstance();
        detector.requestCheckEach(this, new BluetoothDetectorHandler.CheckResponse() {

            @Override
            public boolean onNeedPermission(int type) {
                switch (type) {
                    case BluetoothDetectorHandler.LOCATION_SERVICE:// 请打开 GPS 开关
                        Logg.e("BluetoothDetectorHandler.LOCATION_SERVICE");
                        Utils.openGps(ScanActivity.this);
                        break;
                    case BluetoothDetectorHandler.LOCATION_PERMISSIONS:// 请求定位权限
                        Logg.e("BluetoothDetectorHandler.LOCATION_SERVICE");
//                        RxPermissions rxPermissions = new RxPermissions(ScanActivity.this);
//                        PermissionUtils.location(new PermissionUtils.RequestPermission() {
//
//                            @Override
//                            public void onRequestPermissionSuccess() {
//                                Logg.e("onRequestPermissionSuccess");
//                            }
//
//                            @Override
//                            public void onRequestPermissionFailure() {
//                                Logg.e("onRequestPermissionFailure");
//                                // 如果失败跳到到应用设置页面
//                                AppUtils.applicationDetailsSettings(ScanActivity.this);
//                            }
//                        }, rxPermissions);
                        break;
                    default:
                        break;
                }

                return false;
            }

            @Override
            public void onCheckSuccess() {
                Logg.e("onCheckSuccess");
                BluetoothFilter configuration = BluetoothFilter.builder()
                        .debug(true)
                        .build();
                detector.startScan(configuration, ScanActivity.this);
            }
        });


    }

    @Override
    public void onScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.e("onScanResult", device.getAddress() + " " + device.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detector.stopScan(this);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if ((mBTAdapter != null) && (!mBTAdapter.isEnabled())) {
//            Toast.makeText(this, R.string.bt_not_enabled, Toast.LENGTH_SHORT).show();
//            invalidateOptionsMenu();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        stopScan();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (mIsScanning) {
//            menu.findItem(R.id.action_scan).setVisible(false);
//            menu.findItem(R.id.action_stop).setVisible(true);
//        } else {
//            menu.findItem(R.id.action_scan).setEnabled(true);
//            menu.findItem(R.id.action_scan).setVisible(true);
//            menu.findItem(R.id.action_stop).setVisible(false);
//        }
//        if ((mBTAdapter == null) || (!mBTAdapter.isEnabled())) {
//            menu.findItem(R.id.action_scan).setEnabled(false);
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == android.R.id.home) {
//            // ignore
//            return true;
//        } else if (itemId == R.id.action_scan) {
//            Logg.e("bbbbbbbbbb");
//            startScan();
//            return true;
//        } else if (itemId == R.id.action_stop) {
//            stopScan();
//            return true;
//        } else if (itemId == R.id.action_clear) {
//            if ((mDeviceAdapter != null) && (mDeviceAdapter.getCount() > 0)) {
//                mDeviceAdapter.clear();
//                mDeviceAdapter.notifyDataSetChanged();
//                getActionBar().setSubtitle("");
//            }
//            return true;
//        }
//        return super.onMenuItemSelected(featureId, item);
//    }
//
//    @Override
//    public void onLeScan(final BluetoothDevice newDeivce, final int newRssi,
//                         final byte[] newScanRecord) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                String summary = mDeviceAdapter.update(newDeivce, newRssi, newScanRecord);
//                if (summary != null) {
//                    getActionBar().setSubtitle(summary);
//                }
//            }
//        });
//    }
//
//    private void init() {
//        // BLE check
//        if (!BleUtil.isBLESupported(this)) {
//            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // BT check
//        BluetoothManager manager = BleUtil.getManager(this);
//        if (manager != null) {
//            mBTAdapter = manager.getAdapter();
//        }
//        if (mBTAdapter == null) {
//            Toast.makeText(this, R.string.bt_not_supported, Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // init listview
//        ListView deviceListView = (ListView) findViewById(R.id.list);
//        mDeviceAdapter = new DeviceAdapter(this, R.layout.listitem_device,
//                new ArrayList<BeaconDevice>());
//        deviceListView.setAdapter(mDeviceAdapter);
//        stopScan();
//    }
//
//    private void startScan() {
//        if ((mBTAdapter != null) && (!mIsScanning)) {
//            mBTAdapter.startLeScan(this);
//            mIsScanning = true;
//            setProgressBarIndeterminateVisibility(true);
//            invalidateOptionsMenu();
//        }
//    }
//
//    private void stopScan() {
//        if (mBTAdapter != null) {
//            mBTAdapter.stopLeScan(this);
//        }
//        mIsScanning = false;
//        setProgressBarIndeterminateVisibility(false);
//        invalidateOptionsMenu();
//    }

}
