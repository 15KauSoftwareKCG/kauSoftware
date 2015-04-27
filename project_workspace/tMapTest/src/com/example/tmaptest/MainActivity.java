package com.example.tmaptest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.LogManager;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapData.FindPathDataListenerCallback;
import com.skp.Tmap.TMapData.TMapPathType;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapGpsManager.onLocationChangedCallback;
import com.skp.Tmap.TMapMarkerItem2;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapTapi;
import com.skp.Tmap.TMapView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;



public class MainActivity extends Activity {

	private RelativeLayout mMainRelativeLayout = null;
	 
	private BluetoothService btService = null;
	
	private static final String TAG = "Main";

	// Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
	
    //Location location = null;
	//LocationManager lm = null; 
   
    // 서울 시청 (WGS84) 기본 위치
	double lat=37.5657321; // 위도
	double lon=126.9786599; // 경도
	  
	// 시청역
	double latCityhall = 37.56544;
	double lonCityhall = 126.977119;
	  
	// 한국항공대
	double latKau = 37.600624;
	double lonKau = 126.864388;
	  
	// 공덕역
	double latGongduk = 37.5426981;
	double lonGongduk = 126.9516781;
	  
	// 자신의 위치
	double latMe=37.5657321;	// 위도
	double lonMe=126.9786599;	// 경도

	// 수원역
	double latSoowon = 37.265669;
	double lonSoowon = 127.000223;
		
	String textEndpoint = "";
	
	TMapPOIItem item = new TMapPOIItem();
	ArrayList<TMapPOIItem> POIItem;
	int zoom = 15; // defualt zoom
	  
	private final Handler mHandler = new Handler() {
		  
		@Override
	    public void handleMessage(Message msg) {
				super.handleMessage(msg);
	        }
	         
	    };
	  
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	         
		switch (requestCode) {          
	        case REQUEST_ENABLE_BT:
	            // When the request to enable Bluetooth returns
	            if (resultCode == Activity.RESULT_OK) {
	                // 확인 눌렀을 때
	                //Next Step
	            }
	            else {
	                // 취소 눌렀을 때
	                Log.d(TAG, "Bluetooth is not enabled");
	            }
	            break;
	        }
	}
	
	protected void onCreate(Bundle savedInstanceState)
	{
	
		super.onCreate(savedInstanceState);

	    setContentView(R.layout.activity_main);
	 
	    mMainRelativeLayout =(RelativeLayout)findViewById(R.id.mainRelativeLayout);
	 
	    // BluetoothService 클래스 생성
	    if(btService == null) {
	    	btService = new BluetoothService(this, mHandler);
	    }
	      
	    final TMapView mMapView = new TMapView(this);						// TmapView 생성
	    mMapView.setSKPMapApiKey("e2a7df79-5bc7-3f7f-8bca-2d335a0526e7");	// SDK api key
	 	 
	    final TMapGpsManager gps = new TMapGpsManager(this);
	    gps.setMinTime(1000);
	    gps.setMinDistance(5);
	    gps.setProvider(gps.GPS_PROVIDER);
	     	      
	      
	    mMainRelativeLayout.addView(mMapView);
	 
	    final TMapData tmapdata = new TMapData();
	     
	    // *setCenterPoint(lon, lat)
	    mMapView.setCenterPoint(lon, lat);
	    mMapView.setLocationPoint(lon, lat);
	      
	    mMapView.setZoomLevel(16);
	      	      
	    //TMapPoint tpoint = mMapView.getLocationPoint();
	    
	    // *TmapPoint(lat, lon)
	    final TMapPoint startpoint = new TMapPoint(latCityhall, lonCityhall);
	    final TMapPoint endpoint = new TMapPoint(latKau, lonKau);
	    //final TMapPoint endpoint = new TMapPoint(latSoowon, lonSoowon);
	    
	    TMapPolyLine polyLine= new TMapPolyLine();
		
	      
	    polyLine.setLineWidth(3);
	    //polyLine.setLineColor(Color.BLUE);
	    //mMapView.addTMapPath(polyLine); // 경로
	    //mMapView.addTMapPolyLine("testID",polyLine); // 라인

	    /*
	    tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH, startpoint, endpoint,
	    	new FindPathDataListenerCallback() {
	    		public void onFindPathData(TMapPolyLine polyLine) {
	    			polyLine.setLineColor(Color.BLUE);
	    			mMapView.addTMapPath(polyLine);
	    		}
	    	});
	    */
	            
	      
		//mMapView.setBicycleInfo(true);
			
	    //mMapView.setIconVisibility(false);
			

	  	// 종료 버튼
		findViewById(R.id.button1).setOnClickListener(
				new Button.OnClickListener() {
		    			public void onClick(View v) {
		    				gps.CloseGps();
		    				finish();
		    			  }
		    		  }
		    		  );
		      
		// GPS 버튼
		findViewById(R.id.button2).setOnClickListener(
				new Button.OnClickListener() {
		    			public void onClick(View v) {

		    			//lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);  
		    			//lat = location.getLatitude();
		    			//lon = location.getLongitude();
		    			  	
		    			int Satellite = gps.getSatellite();
		    				  
		    			if(Satellite==0)
		    			{
		    				Toast toastNoSatellite = Toast.makeText(MainActivity.this,
		    							  "연결된 GPS 위성이 없습니다.", Toast.LENGTH_LONG);
		    				toastNoSatellite.show();
		    			}
		    			else
		    			{
		    				gps.OpenGps();
		    				TMapPoint point = gps.getLocation();
		    				latMe = point.getLatitude();
		    				lonMe = point.getLongitude();
		    			}
		    				  
		    				  
		    			Toast toast = Toast.makeText(MainActivity.this,
		    					  "GPS (WGS84)\n위도: " + latMe + "\n경도: " + lonMe, Toast.LENGTH_LONG);
		    			mMapView.setZoomLevel(16); 
		    			mMapView.setCenterPoint(lonMe, latMe);
		    			mMapView.setIconVisibility(true);
		    			toast.show();
			    			 
		    			}
				}
		    	);
		 
		final Dialog dia = new Dialog(MainActivity.this);

		dia.setContentView(R.layout.set_endpoint);

		dia.setTitle("장소 검색");

		Button btn = (Button)dia.findViewById(R.id.cbutton1);

		final EditText et = (EditText)dia.findViewById(R.id.editText1);

		btn.setOnClickListener(new Button.OnClickListener() {


		    @Override

		    public void onClick(View v) {

		        //do something here
		    	textEndpoint = "";
		    	//textEndpoint = et.getText();
		        
		    	textEndpoint = "항공대";
		    	
		    	dia.dismiss();
		    	
		    	/*
		    	try {
					POIItem = tmapdata.findTitlePOI(textEndpoint);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FactoryConfigurationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    	mMapView.addTMapPOIItem(POIItem);
		    	*/
		    	
		    	try {
					//ArrayList<TMapPOIItem> POIItem = tmapdata.findAroundNamePOI(poiPoint, textEndpoint);
					POIItem = tmapdata.findTitlePOI(textEndpoint);
					int i=0;
					for(i=0; i<POIItem.size(); i++)
					{
						//TMapPOIItem item = POIItem.get(i);
						item = POIItem.get(i);
						
						TMapMarkerItem2 tMapMarkerItem2 = new TMapMarkerItem2();
						TMapPoint tpoint = new TMapPoint(item.getPOIPoint().getLatitude(),
															item.getPOIPoint().getLongitude());
						tMapMarkerItem2.setTMapPoint(tpoint);
						
						tMapMarkerItem2.setID(item.getPOIID());
						
						mMapView.addMarkerItem2("TestID", tMapMarkerItem2);
						
						//tMapMarkerItem2.setCanShowCallout(true);
					}
				
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FactoryConfigurationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
				
				
				
		    	Toast toastSetEndpoint = Toast.makeText(MainActivity.this,
		        		"입력한 내용 : "+ textEndpoint, Toast.LENGTH_LONG);
		        toastSetEndpoint.show();
		    }

		});
		
		
		// 이동 버튼
		findViewById(R.id.button3).setOnClickListener(
				new Button.OnClickListener() {
		    			public void onClick(View v) {
		    				mMapView.setIconVisibility(false);
		    				mMapView.setZoomLevel(16);
		    				mMapView.setCenterPoint(lonKau, latKau);
		    				
		    				//dia.show();
		    				//dia.getWindow().setSoftInputMode(
		    				//		WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		    			}
				}
				);
	
		
		
		// 경로 버튼
		findViewById(R.id.button4).setOnClickListener(		
				new Button.OnClickListener() {
		    			public void onClick(View v) {
		    				mMapView.setIconVisibility(false);
		    				mMapView.setZoomLevel(12);
		    				tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH, startpoint, endpoint,
		    			    		new FindPathDataListenerCallback() {
		    			    		public void onFindPathData(TMapPolyLine polyLine) {
		    			    			polyLine.setLineColor(Color.BLUE);
		    			    			mMapView.addTMapPath(polyLine);
		    			    		}
		    			    }
		    				);
		    				  
		    				mMapView.setCenterPoint((lonCityhall + lonKau)/2, (latCityhall + latKau)/2);
		    				
		    				//mMapView.setCenterPoint((lonCityhall + lonSoowon)/2, (latCityhall + latSoowon)/2);
		    			}
		    		  
				}
				);

		// 확대 버튼
		findViewById(R.id.button5).setOnClickListener(
				new Button.OnClickListener() {
		    			public void onClick(View v) {
		    				mMapView.setIconVisibility(false);
		    				mMapView.MapZoomIn();
		    			}
		    		  
				}
		    	);

		// 축소 버튼
		findViewById(R.id.button6).setOnClickListener(
				new Button.OnClickListener() {
		    			public void onClick(View v) {
		    				mMapView.setIconVisibility(false);
		    				mMapView.MapZoomOut();
		    			}
				}
				);
		  
		// 블루투스 버튼
		findViewById(R.id.button7).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						if(btService.getDeviceState()) {
							// 블루투스가 지원 가능한 기기일 때
							btService.enableBluetooth();
						}
						else {
							Toast toastBluetooth = Toast.makeText(MainActivity.this,
									"블루투스가 지원되지 않는 기기입니다.", Toast.LENGTH_LONG);
							toastBluetooth.show();
		    			    //finish();
						}
					}
				}
				);
	
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
   	public void onLocationChange(Location location) {
    	
   	}
    	
    
}

