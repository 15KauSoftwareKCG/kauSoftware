package com.kaudesign.locationtracker;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends Activity implements View.OnClickListener {
 GoogleMap map;
 LocationManager locMgr;
 LocationListener locLnr;
 
 Button startButton;
 Button stopButton;
 
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  
  locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
  locLnr = new MyLocationListener();
  
  startButton = (Button) findViewById(R.id.start);
  stopButton = (Button) findViewById(R.id.stop);
  
  startButton.setOnClickListener(this);
  stopButton.setOnClickListener(this);
  
  MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
  map = mf.getMap();
  map.setOnMapClickListener(new OnMapClickListener() {
   @Override
   public void onMapClick(LatLng latlng) {
    map.animateCamera(CameraUpdateFactory.newLatLng(latlng));
   }
  });
  
  map.setMyLocationEnabled(true);
  
  UiSettings setting = map.getUiSettings();
  setting.setMyLocationButtonEnabled(true);
  setting.setCompassEnabled(true);
  setting.setZoomControlsEnabled(true);
  }
 
 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
  getMenuInflater().inflate(R.menu.main, menu);
  return true;
 }
 
 @Override
 protected void onResume() {
  super.onResume();
  
  setPosition();
  addLine();
 }
 
 //앱을 처음 실행시 이곳에 찍힌 위도, 경도값의 위치가 처음 화면에 보임
 private void setPosition() {
  LatLng latlng = new LatLng(37.30283, 127.90814);
  CameraPosition cp = new CameraPosition.Builder().target((latlng)).zoom(15).build();
  map.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
 }
 
 private void addLine() {
  map.addPolyline(new PolylineOptions()
  .color(Color.GREEN)
  .add(new LatLng(37.304325,127.90758),
    new LatLng(37.304205,127.907622),
    new LatLng(37.303326,127.906544),
    new LatLng(37.303194,127.906469),
    new LatLng(37.302955,127.906426),
    new LatLng(37.302392,127.906458),
    new LatLng(37.302204,127.906598),
    new LatLng(37.301244,127.907982),
    new LatLng(37.300992,127.908604),
    new LatLng(37.30089,127.909328),
    new LatLng(37.300898,127.909881),
    new LatLng(37.302742,127.909886),
    new LatLng(37.302878,127.909977),
    new LatLng(37.304154,127.908089),
    new LatLng(37.304154,127.908089),
    new LatLng(37.304201,127.907612)));
 }
 
 @Override
 public void onClick(View v) {
  int millis = 5000;
  int distance = 5;
  
  if (v == startButton) {
   locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, millis,
     distance, locLnr);
  } else if (v == stopButton) {
   locMgr.removeUpdates(locLnr);
  }
 }
 public class MyLocationListener implements LocationListener {
  
  @Override
  public void onLocationChanged(Location loc) {
 
// 이 부분에 토스트가 아닌 내가 지나온 길을 polyline 으로 표시하고 싶습니다.

   String text = "현재위치 : \n" + "위도 : " + loc.getLatitude() + " \n"
     + "경도 : " + loc.getLongitude();
   
   Toast.makeText(getApplicationContext(),  text,  Toast.LENGTH_LONG).show();
  }
 
 @Override
 public void onProviderDisabled(String provider) {
  Toast.makeText(getApplicationContext(), "GPS 이용 불가 ",
    Toast.LENGTH_SHORT).show();
 }
 
 @Override
 public void onProviderEnabled(String provider) {
  Toast.makeText(getApplicationContext(), "GPS 이용 가능",
    Toast.LENGTH_SHORT).show();
 }
 
 @Override
 public void onStatusChanged(String provider, int status, Bundle extras) {
 } 
 }
}