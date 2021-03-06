package com.wfx.jasmine_trail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.MyLocationStyle;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.vector.utils.animation.MarkerTranslateAnimator;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class LocationMarkerMoveActivity extends AbsMapActivity implements EasyPermissions.PermissionCallbacks, LocationSource, TencentLocationListener {
    private TencentMap mTencentMap;
    private UiSettings mUiSettings;
    private TencentLocationManager locationManager;
    private TencentLocationRequest locationRequest;
    private MyLocationStyle locationStyle;
    private OnLocationChangedListener locationChangedListener;
    private SensorEventHelper mSensorHelper;
    private Marker marker;
    private String username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState, TencentMap pTencentMap) {
        super.onCreate(savedInstanceState, pTencentMap);
        mTencentMap = pTencentMap;
        requestDynamicPermisson();
        init();
        initLocation();
        initSensor();
    }

    private void initSensor() {
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }


    private void init() {
        mUiSettings = mTencentMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(true);//????????????????????????????????????
    }

    private void requestDynamicPermisson() {
        //???????????????????????????
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
        };

        if (EasyPermissions.hasPermissions(this, perms)) {//???????????????????????????
            Log.i("location", "???????????????");
        } else {
            EasyPermissions.requestPermissions(this, "???????????????", 0, perms);
        }
    }

    private void initLocation() {
        //????????????????????????????????????, ???????????????????????????????????????
        locationManager = TencentLocationManager.getInstance(this);
        //???????????????
        locationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        //??????????????????
        locationRequest = TencentLocationRequest.create();
        //??????????????????????????????????????????????????????3s
        locationRequest.setInterval(3000);

        //??????????????????????????????
        mTencentMap.setLocationSource(this);
        //?????????true??????????????????????????????????????????false??????????????????????????????????????????????????????false
        mTencentMap.setMyLocationEnabled(true);
        //????????????????????????
        //setLocMarkerStyle();
    }

    private void setLocMarkerStyle() {
        locationStyle = new MyLocationStyle();
        //??????????????????
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????,?????????????????????
        //????????????
//        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getBitMap(R.mipmap.location_icon));
//        locationStyle.icon(bitmapDescriptor);
        //???????????????????????????????????????
        locationStyle.strokeWidth(3);
        //????????????????????????
        locationStyle.fillColor(R.color.colorAccent);
        locationStyle.strokeColor(R.color.colorPrimary);
        mTencentMap.setMyLocationStyle(locationStyle);
    }

    private Bitmap getBitMap(int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 55;
        int newHeight = 55;
        float widthScale = ((float) newWidth) / width;
        float heightScale = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (i == TencentLocation.ERROR_OK && locationChangedListener != null) {
            Location location = new Location(tencentLocation.getProvider());
            //???????????????????????????
            location.setLatitude(tencentLocation.getLatitude());
            location.setLongitude(tencentLocation.getLongitude());
            location.setAccuracy(tencentLocation.getAccuracy());
            LatLng latLng = new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude());
            addMarker(latLng);
            Toast.makeText(LocationMarkerMoveActivity.this,latLng.toString(),Toast.LENGTH_SHORT).show();
            Double latitude = latLng.getLatitude();
            Double longitude = latLng.getLongitude();
            String addInfo=latitude.toString()+","+longitude.toString();
            Intent intent=getIntent();
            username = intent.getStringExtra("usernameLocal");
            new Thread(){
                public void run(){
                    try{
                        //?????????????????????????????????
                        new ConnectWeb().addInfo(username,addInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                    }
                }
            }.start();


            if(mSensorHelper!=null){
                mSensorHelper.setCurrentMarker(marker);
            }
            locationChangedListener.onLocationChanged(location);
        }
    }

    private void addMarker(LatLng position) {
        if (marker != null) return;
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.navi_map_gps_locked)));
        options.anchor(0.5f, 0.5f);
        options.icon(BitmapDescriptorFactory.fromBitmap(getBitMap(R.mipmap.navi_map_gps_locked)));
        options.position(position);
        marker = mTencentMap.addMarker(options);
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        locationChangedListener = onLocationChangedListener;
        int err = locationManager.requestLocationUpdates(locationRequest, this, Looper.myLooper());
        switch (err) {
            case 1:
                Toast.makeText(this, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "manifest ???????????? key ?????????", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "????????????libtencentloc.so??????", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    public void deactivate() {
        locationManager.removeUpdates(this);
        locationManager = null;
        locationRequest = null;
        locationChangedListener = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_local_stop);
        menu.findItem(R.id.menu_local_back);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_local_stop:
                locationManager.removeUpdates(this);
                break;
            case R.id.menu_local_back:
                locationManager.removeUpdates(this);
                Intent intent=new Intent();
                intent.setClass(LocationMarkerMoveActivity.this, MainActivity.class);
                intent.putExtra("username",username.trim() );
                startActivity(intent);
                break;
        }
        supportInvalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }
}

