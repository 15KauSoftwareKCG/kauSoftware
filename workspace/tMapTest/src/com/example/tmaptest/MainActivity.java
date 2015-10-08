package com.example.tmaptest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.logging.LogManager;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.skp.Tmap.TMapAddressInfo;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapData.FindPathDataListenerCallback;
import com.skp.Tmap.TMapData.FindTitlePOIListenerCallback;
import com.skp.Tmap.TMapData.TMapPathType;
import com.skp.Tmap.TMapData.reverseGeocodingListenerCallback;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapGpsManager.onLocationChangedCallback;
import com.skp.Tmap.TMapInfo;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapMarkerItem2;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapTapi;
import com.skp.Tmap.TMapView;
import com.skp.Tmap.TMapView.OnCalloutRightButtonClickCallback;
import com.skp.Tmap.TMapView.OnClickListenerCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
	  
	// 자신의 위치 (현 서울시청)
	double latMe=37.5657321;	// 위도
	double lonMe=126.9786599;	// 경도

	// 수원역
	double latSoowon = 37.265669;
	double lonSoowon = 127.000223;
		
	// 서울역
	double latSeoul = 37.554705;
	double lonSeoul = 126.970688223;
	
	String textEndPoint = "";
	
	public ArrayList<TMapPoint> pointt = new ArrayList<TMapPoint>();
	
	public TMapPoint myLocation = new TMapPoint(latMe, lonMe);
	
	
	
	
	
	
	
	
	
	TMapPOIItem item = new TMapPOIItem();
	ArrayList<TMapPOIItem> POIItem = new ArrayList<TMapPOIItem>();
	
	
	int zoom = 15; // defualt zoom
	  
	
	
	TMapAddressInfo addressInfoSave = new TMapAddressInfo();
	
	TMapView mMapView;;
	LocationManager mLocMgr;
	String locProv;
	boolean islocation = false;
	
	
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
	 
	 
	    // BluetoothService 클래스 생성
	    if(btService == null) {
	    	btService = new BluetoothService(this, mHandler);
	    }
	    
	    
	      
	    mLocMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locProv = mLocMgr.getBestProvider(getCriteria(), true);
	    mMapView = new TMapView(this);						// TmapView 생성
	    mMapView.setSKPMapApiKey("e2a7df79-5bc7-3f7f-8bca-2d335a0526e7");	// SDK api key

	    
	    
	    final TMapGpsManager gps = new TMapGpsManager(this);
	    gps.setMinTime(1000);
	    gps.setMinDistance(5);
	    gps.setProvider(TMapGpsManager.GPS_PROVIDER);
	    
	    final Dialog dia = new Dialog(MainActivity.this);
	 
	    
	    
	    final TMapData tmapdata = new TMapData();
	     
	    
	    // 시작 위치
	    // *setCenterPoint(lon, lat)
	    mMapView.setCenterPoint(lon, lat);
	    mMapView.setLocationPoint(lon, lat);
	      
	    mMapView.setZoomLevel(16);
	      	      
	    //TMapPoint tpoint = mMapView.getLocationPoint();
	    
	    // *TmapPoint(lat, lon)
	    
	    
	    final TMapPoint startPoint = new TMapPoint(latCityhall, lonCityhall);
	    final TMapPoint endPoint = new TMapPoint(latKau, lonKau);
	    //final TMapPoint endPoint = new TMapPoint(latSoowon, lonSoowon);
	    
	    TMapPolyLine polyLine= new TMapPolyLine();
		
	      
	    polyLine.setLineWidth(3);
	    //polyLine.setLineColor(Color.BLUE);
	    //mMapView.addTMapPath(polyLine); // 경로
	    //mMapView.addTMapPolyLine("testID",polyLine); // 라인

	    
	    /*
	    // 경료 요청
	    tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH, startPoint, endPoint,
	    	new FindPathDataListenerCallback() {
	    		public void onFindPathData(TMapPolyLine polyLine) {
	    			polyLine.setLineColor(Color.BLUE);
	    			mMapView.addTMapPath(polyLine);
	    		}
	    	});
	    */
     
		//mMapView.setBicycleInfo(true);
			
	    //mMapView.setIconVisibility(false);


	    final Dialog popMarker = new Dialog(MainActivity.this);
	    popMarker.setContentView(R.layout.marker_popup);
	    popMarker.setTitle("상세 정보");

		Button popOk = (Button)popMarker.findViewById(R.id.pbutton1);
		Button popCancle = (Button)popMarker.findViewById(R.id.pbutton2);
		
		//final TextView textview = (TextView)findViewById(R.id.ptext);
		final TextView pName = (TextView)popMarker.findViewById(R.id.pName);
		final TextView pPhone = (TextView)popMarker.findViewById(R.id.pPhone);
		final TextView pAddress = (TextView)popMarker.findViewById(R.id.pAddress);
			
	    // 롱클릭 이벤트
	    mMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
	    	@Override
	    	public void onLongPressEvent(ArrayList<TMapMarkerItem>
	    	markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point) {
	    	
	    		if(markerlist.size()>0)
	    		{
	    			int check=0;
	    			TMapMarkerItem temp = new TMapMarkerItem();
	    			for(int i=0; i<markerlist.size(); i++)
    				{
	    				temp = markerlist.get(i);
	    				for(int j=0; j<POIItem.size(); j++)
	    				{
	    					if(temp.getID()==POIItem.get(j).id)
	    					{
	    						check=1;
	    						pName.setText("　이름: "+POIItem.get(j).name);
	    						
	    						if(POIItem.get(j).telNo==null) pPhone.setText("　전화번호: ");
	    						else pPhone.setText("　전화번호: "+POIItem.get(j).telNo);
	    						
	    						if(POIItem.get(j).detailAddrName==null)
	    						{
	    							pAddress.setText("　주소: "+POIItem.get(j).upperAddrName 
	    									+ " " + POIItem.get(j).middleAddrName 
	    									+ " " + POIItem.get(j).lowerAddrName);
	    						}
	    						else
	    						{
	    							pAddress.setText("　주소: "+POIItem.get(j).upperAddrName 
	    									+ " " + POIItem.get(j).middleAddrName 
	    									+ " " + POIItem.get(j).lowerAddrName 
	    									+ " " + POIItem.get(j).detailAddrName);
	    						}
    						}
	    					if(check==1) break;
	    				}
    				}
	    			if(check==1) popMarker.setTitle("상세 정보");
	    			else
	    			{
	    				// 사용자 지정 마커 롱클릭
	    				popMarker.setTitle("사용자 지정");
	    				pName.setText("");
						pPhone.setText("");
						//pAddress.setText("");
						
						pAddress.setText("　주소: "+ addressInfoSave.strFullAddress);
						
						Log.d(TAG, "사용자 마커 클릭");
					}
	    			endPoint.setLatitude(temp.latitude);
					endPoint.setLongitude(temp.longitude);
					
										
    				popMarker.show();
	    			
	    		} // if(markerlist.size()>0)
	    		
	    		// 사용자 마커 추기하기
	    		else
	    		{
	    			TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
	    			tMapMarkerItem.setID("userSelect");
			    	tMapMarkerItem.setName("사용자 지정");
			    	tMapMarkerItem.setVisible(tMapMarkerItem.VISIBLE);
			    	tMapMarkerItem.setTMapPoint(point);
			    	Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pin_b_bm_o);
			    	tMapMarkerItem.setIcon(bitmap);
			    	tMapMarkerItem.setPosition((float)0.5, (float)1.0);
			    	
			    	tmapdata.reverseGeocoding(point.getLatitude(), point.getLongitude(), "A03",
			    			new reverseGeocodingListenerCallback() {
			    			@Override
			    			public void onReverseGeocoding(TMapAddressInfo addressInfo) {
			    				//LogManager.printLog("선택한 위치의 주소는 " + addressInfo.strFullAddress);
			    				//pAddress.setText(""+ addressInfo.strFullAddress);
			    				addressInfoSave = addressInfo;
			    			}
			    	});
			    	
			    	mMapView.addMarkerItem(tMapMarkerItem.getID(), tMapMarkerItem);
			    	pName.setText("");
					pPhone.setText("");
					//pAddress.setText("");
					pAddress.setText("　주소: "+ addressInfoSave.strFullAddress);
			    	popMarker.setTitle("사용자 지정");
			    	
			    	Log.d(TAG, "사용자 마커 생성");
			    	
			    	endPoint.setLatitude(tMapMarkerItem.latitude);
					endPoint.setLongitude(tMapMarkerItem.longitude);
			    	popMarker.show();
	    		}
	    	}
	    	
	    	});
	    
	    // 상세정보 버튼
	    popOk.setOnClickListener(new Button.OnClickListener() {

		    public void onClick(View v) {

		    	popMarker.dismiss();
		    	//startPoint = TMapPoint(latCityhall, lonCityhall);
		    	startPoint.setLatitude(latMe);
		    	startPoint.setLongitude(lonMe);
		    	
		    	tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH, startPoint, endPoint,
			    		new FindPathDataListenerCallback() {
			    		public void onFindPathData(TMapPolyLine polyLine) {
			    			polyLine.setLineColor(Color.BLUE);
			    			mMapView.addTMapPath(polyLine);
			    		}
			    }
				);
		    	//mMapView.setCenterPoint((startPoint.getLongitude() + endPoint.getLongitude())/2
		    	//		,(startPoint.getLatitude() + endPoint.getLatitude())/2);
		    	
		    	// startPoint, endPoint에 맞는 줌, 중심 구하기
		    	ArrayList<TMapPoint> point = new ArrayList<TMapPoint>();
		    	point.add(startPoint);
		    	point.add(endPoint);
		    	TMapInfo info = mMapView.getDisplayTMapInfo(point);
		    	
		    	
		    	mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
		    	mMapView.setZoomLevel(info.getTMapZoomLevel()); 
		    	dia.dismiss();
		    }

		});
	    popCancle.setOnClickListener(new Button.OnClickListener() {

		    public void onClick(View v) {

		    	popMarker.dismiss();
		    	
		    }

		});
	    
	    mMainRelativeLayout =(RelativeLayout)findViewById(R.id.mainRelativeLayout);
		mMainRelativeLayout.addView(mMapView);
		// 종료 버튼
		findViewById(R.id.button1).setOnClickListener(
				new Button.OnClickListener() {
		    			public void onClick(View v) {
		    				gps.CloseGps();
		    				islocation=false;
		    				finish();
		    			  }
		    		  }
		    		  );
		      
		
		// GPS 버튼
		findViewById(R.id.button2).setOnClickListener(
				new Button.OnClickListener() {
		    			public void onClick(View v) {
		    			
		    			if(!mLocMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		    		            alertCheckGPS();
		    			}
	    				else if(!islocation){
		    		        mLocMgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, 3000, 3, mLocListener );
		    				
		    				Toast toast;
		    	        	toast = Toast.makeText(MainActivity.this,
		    						"위치를 탐색합니다.", Toast.LENGTH_LONG);
		    					
		    	        	toast.show();
		    				islocation=true;
	    				}
	    				else{
	    					mLocMgr.removeUpdates(mLocListener);
	    					Toast toast;
	    		        	toast = Toast.makeText(MainActivity.this,
	    							"위치탐색을 종료합니다.", Toast.LENGTH_LONG);
	    						
	    		        	toast.show();
	    					islocation=false;
	    				}
		    		}
				}
			   	);

		// 입력창

		dia.setContentView(R.layout.set_endpoint);

		dia.setTitle("장소 검색");

		final Button btn = (Button)dia.findViewById(R.id.cbutton1);

		final EditText et = (EditText)dia.findViewById(R.id.editText1);

		final ArrayList<TMapPoint> point = new ArrayList<TMapPoint>();
		
		
		dia.findViewById(R.id.map1).setOnClickListener( //지도보기 클릭
				new Button.OnClickListener() {
	    			public void onClick(View v) {
	    				TMapInfo info = mMapView.getDisplayTMapInfo(point);
				    	mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
				    	mMapView.setZoomLevel(info.getTMapZoomLevel());
				    	
	    				dia.dismiss();
	    			}
				}
		);

        final ListView list = (ListView)dia.findViewById(R.id.delist);
        final ListAdapter listadapter = new ListAdapter(dia.getContext(),popMarker,dia,endPoint);

	    
		// 확인창 버튼 클릭
		btn.setOnClickListener(new Button.OnClickListener() {

		    public void onClick(View v) {

		    	//textEndPoint = et.getText().toString(); // 가상휴대폰에서 한글입력 지원안됨, 추후 확인
		    	textEndPoint="서울역";
		    	
		    	listadapter.clear();
		    	tmapdata.findTitlePOI(textEndPoint, new FindTitlePOIListenerCallback() { 
		    		
		    		@Override
		    		public void onFindTitlePOI(ArrayList<TMapPOIItem> poiItem) {

		    		// 이전 마커 지우기
		    		if(POIItem.isEmpty()==false)
    				{
    					for(int i=0; i<POIItem.size(); i++)
    					{
    						TMapPOIItem item = POIItem.get(i);
    						mMapView.removeMarkerItem(item.getPOIID());
    					}
    					POIItem.clear();
    				}
		    		
		    		for(int k=0; k<poiItem.size(); k++)
		    		{
		    		POIItem.add(poiItem.get(k));
		    		}
		    		point.clear();
		    		for(int i = 0; i < poiItem.size(); i++) {
		    		TMapPOIItem item = poiItem.get(i);
		    		 
		    		TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
		    		
		    		
		    		//TMapPoint markerPoint = new TMapPoint(item.getPOIPoint().getLatitude(), item.getPOIPoint().getLongitude());
		    		
		    		//tMapMarkerItem.latitude = item.getPOIPoint().getLatitude();
		    		//tMapMarkerItem.longitude = item.getPOIPoint().getLongitude();
		    		tMapMarkerItem.setTMapPoint(item.getPOIPoint());
		    		tMapMarkerItem.setID(item.getPOIID());
			    	tMapMarkerItem.setName(item.getPOIName());
		    		tMapMarkerItem.setCalloutTitle(item.getPOIName());
		    		if(item.telNo==null) tMapMarkerItem.setCalloutSubTitle("☎:"); 
		    		else tMapMarkerItem.setCalloutSubTitle("☎: "+item.telNo);
		    		tMapMarkerItem.setVisible(tMapMarkerItem.VISIBLE);
		    		tMapMarkerItem.setCanShowCallout(true);
		    		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pin_r_bm_o);
			    	tMapMarkerItem.setIcon(bitmap);
		    		tMapMarkerItem.setPosition((float)0.5, (float)1.0);
		    		
		    		listadapter.add(item);
		    		point.add(item.getPOIPoint());
		    		mMapView.bringMarkerToFront(tMapMarkerItem);
		    				 
		    		
		    		//mMapView.addTMapPOIItem(poiItem);
		    		
		    		//if(i==0) mMapView.setCenterPoint(item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude());
		    		mMapView.addMarkerItem(item.getPOIID(), tMapMarkerItem);
		    		//mMapView.addMarkerItem2(item.getPOIID(), tMapMarkerItem2);
			    	//mMapView.addTMapPOIItem(poiItem);
		    		
		    		//mMapView.setZoomLevel(14);
		    		
		    		//TMapInfo info = mMapView.getDisplayTMapInfo();

				    	
		    		} // for

		            }	    				    	   
		    		
		    	});	
		    	
	            list.setAdapter(listadapter);	    		 
	    		 dia.dismiss();
	    		 dia.show();
		    }	    				    
		});
	
		
		// 검색 버튼 (구.이동 버튼)
		findViewById(R.id.button3).setOnClickListener(
				new Button.OnClickListener() {
		    			public void onClick(View v) {
		    				
		    				TMapPoint startLocation = pointt.get(0);
		    				
		    				
		    				int tracker = pointt.size();
		    				TMapPoint endLocation = pointt.get(tracker-1);
		    			
		    		    	tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH, startLocation, endLocation,
		    			    		new FindPathDataListenerCallback() {
		    			    		public void onFindPathData(TMapPolyLine polyLine) {
		    			    			polyLine.setLineColor(Color.BLUE);
		    			    			mMapView.addTMapPath(polyLine);
		    			    		}
		    			    }
		    				);
		    		    	
		    		    	ArrayList<TMapPoint> point = new ArrayList<TMapPoint>();
		    		    	point.add(startLocation);
		    		    	point.add(endLocation);
		    		    	
		    		    	TMapInfo info = mMapView.getDisplayTMapInfo(point);
		    		    	
		    		    	mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
		    		    	mMapView.setZoomLevel(info.getTMapZoomLevel()); 
		    	
		    	        	/////////////////////////////////////////////////////////
		    			}
				}
				);
	
		
		
		// 경로 버튼
		findViewById(R.id.button4).setOnClickListener(		
				new Button.OnClickListener() {
		    			public void onClick(View v) {
		    				/*
		    				mMapView.setIconVisibility(false);
		    				mMapView.setZoomLevel(12);
		    				tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH, startPoint, endPoint,
		    			    		new FindPathDataListenerCallback() {
		    			    		public void onFindPathData(TMapPolyLine polyLine) {
		    			    			polyLine.setLineColor(Color.BLUE);
		    			    			mMapView.addTMapPath(polyLine);
		    			    		}
		    			    }
		    				);
		    				  
		    				//mMapView.setCenterPoint((startPoint.getLongitude() + endPoint.getLongitude())/2,
		    				//		(startPoint.getLatitude() + endPoint.getLongitude()/2));
		    				mMapView.setCenterPoint((lonCityhall + lonKau)/2, (latCityhall + latKau)/2);
		    				//mMapView.setCenterPoint((lonCityhall + lonSoowon)/2, (latCityhall + latSoowon)/2);
		    				*/
		    				
		    				Thread thread = new Thread() {
		                        @Override
		                        public void run() {
		                            HttpClient httpClient = new DefaultHttpClient();


		                            //String urlString = "https://apis.skplanetx.com/tmap/routes/bicycle?callback=&bizAppId=&version=1";
		                            
		                            String urlString = "https://apis.skplanetx.com/tmap/routes/bicycle?callback=&bizAppId=&version=1&format=json&appKey=e2a7df79-5bc7-3f7f-8bca-2d335a0526e7";
		                            //String urlString = "https://apis.skplanetx.com/tmap/routes/pedestrian?callback=&bizAppId=&version=1&format=json&appKey=e2a7df79-5bc7-3f7f-8bca-2d335a0526e7";
		                            
		                            TMapPolyLine jsonPolyline = new TMapPolyLine();
							        jsonPolyline.setLineColor(Color.RED);
							        jsonPolyline.setLineWidth(2);
							        		                            
		                            // &format={xml 또는 json}
		                            try {
		                                URI url = new URI(urlString);

		                                HttpPost httpPost = new HttpPost();
		                                httpPost.setURI(url);

		                                List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		                                //nameValuePairs.add(new BasicNameValuePair("startX", Double.toString(126.9786599)));
		                                //nameValuePairs.add(new BasicNameValuePair("startY", Double.toString(37.5657321)));
		                                nameValuePairs.add(new BasicNameValuePair("startX", Double.toString(startPoint.getLongitude())));
		                                nameValuePairs.add(new BasicNameValuePair("startY", Double.toString(startPoint.getLatitude())));
		                                
		                                
		                                //nameValuePairs.add(new BasicNameValuePair("endX", Double.toString(126.9516781)));
		                                //nameValuePairs.add(new BasicNameValuePair("endY", Double.toString(37.5426981)));
		                                nameValuePairs.add(new BasicNameValuePair("endX", Double.toString(endPoint.getLongitude())));
		                                nameValuePairs.add(new BasicNameValuePair("endY", Double.toString(endPoint.getLatitude())));
		                                
		                                
		                                
		                                nameValuePairs.add(new BasicNameValuePair("reqCoordType", "WGS84GEO"));
		                                nameValuePairs.add(new BasicNameValuePair("resCoordType", "WGS84GEO"));
		                                
		                                nameValuePairs.add(new BasicNameValuePair("startName", "서울시청"));
		                                nameValuePairs.add(new BasicNameValuePair("endName", "공덕역"));
		                                
		                                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


		                                HttpResponse response = httpClient.execute(httpPost);
		                                String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

		                                //Log.d(TAG, responseString);

		                                String strData = "";
		                             	
		                                Log.d(TAG, "0\n");
								        JSONObject jAr = new JSONObject(responseString);
								        
		                                Log.d(TAG, "1\n");
										// 개별 객체를 하나씩 추출
		                                JSONArray features = jAr.getJSONArray("features");
								        
								        Log.d(TAG, "2,"+ features.length() +"\n");
								        // 객체에서 하위 객체를 추출
								        
								    	
								        for(int i=0; i<features.length(); i++)
								        {
								        	 JSONObject test2 = features.getJSONObject(i);
								        	 JSONObject properties = test2.getJSONObject("properties");

								        	 
								        	 JSONObject geometry = test2.getJSONObject("geometry");
								        	 JSONArray coordinates = geometry.getJSONArray("coordinates");
								        	
								        	 String geoType = geometry.getString("type");
								        	 if(geoType.equals("Point"))
								        	 {
								        		 double lonJson = coordinates.getDouble(0);
									        	 double latJson = coordinates.getDouble(1);
									        	 
									        	 Log.d(TAG, "-");
									        	 Log.d(TAG, lonJson+","+latJson+"\n"); 
									        	 TMapPoint jsonPoint = new TMapPoint(latJson, lonJson);
									        	
									        	 jsonPolyline.addLinePoint(jsonPoint);
									        	 Log.d(TAG, jsonPoint.getLatitude()+"-"+jsonPoint.getLongitude());
									        	 
									        	 
								        	 }
								        	 
								        	 								        			 
								        	 String nodeType = properties.getString("nodeType");
								        	 
								        	 if(nodeType.equals("POINT")){
										        String turnType = properties.getString("turnType");
										        	
										        Log.d(TAG, "회전방향: "+turnType+"\n");
										     }
								        	 else if(nodeType.equals("LINE")){
								        		 String description = properties.getString("description");
								        		 Log.d(TAG, description+"\n");
								        	 }
								        	 
								        }
								        DashPathEffect dashPath = new DashPathEffect(new float[]{20,10}, 1); //점선
								        //DashPathEffect dashPath2 = new DashPathEffect(new float[]{0,0}, 0); //실선
								        
								        jsonPolyline.setPathEffect(dashPath); //점선
								        //jsonPolyline.setPathEffect(dashPath2); //실선
								        jsonPolyline.setLineColor(Color.GREEN);
								        jsonPolyline.setLineAlpha(0);
								        mMapView.addTMapPolyLine("jsonPolyline",jsonPolyline);
								        /*
								        JSONObject test = features.getJSONObject(0);
								        
								        JSONObject properties = test.getJSONObject("properties");
								        
								        Log.d(TAG, "3\n");
								        //JSONObject index = properties.getJSONObject("index");
								        String nodeType = properties.getString("nodeType");
								        
								        
								        Log.d(TAG, "4 " + nodeType+"\n");
								        
								        if(nodeType.equals("POINT")){
								        	String turnType = properties.getString("turnType");
								        	
								        	Log.d(TAG, "5 " + turnType+"\n");
								        }
								        */
								        
								        // 하위 객체에서 데이터를 추출
								        //strData += features.getString("name") + "\n";
										

		                            } catch (URISyntaxException e) {
		                                Log.e(TAG, e.getLocalizedMessage());
		                                e.printStackTrace();
		                            } catch (ClientProtocolException e) {
		                                Log.e(TAG, e.getLocalizedMessage());
		                                e.printStackTrace();
		                            } catch (IOException e) {
		                                Log.e(TAG, e.getLocalizedMessage());
		                                e.printStackTrace();
		                            } catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

		                        }
		                    };

		                    thread.start();
		    			}
		    		  
				}
				);

		/*
		 * 모바일 환경에서 멀티 터치로 화면을 확대축소할 수 있음을 확인, 테스트용 버튼 삭제 -20150428
		 * 
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
		*/  
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
	
/*
	    //////////////////////////////////////////////////지도보기를 눌리면 이쪽으로 이동
		findViewById(R.id.map).setOnClickListener(
				new Button.OnClickListener() {
	    			public void onClick(View v) {


      		    setContentView(R.layout.activity_main);
	    			 

	    			   
	    			  }
	    		  }
	    		  );
	      		*/
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
    
    public void markerClick(TMapMarkerItem markerItem)
    {
    	Toast toastMarkerClick = Toast.makeText(MainActivity.this,
				"POI Name: " + markerItem.getName().toString() + ", " +
    			"Point: " + markerItem.latitude + ", " + markerItem.longitude, Toast.LENGTH_LONG);
		toastMarkerClick.show();
    }
    
    
   	
   	LocationListener mLocListener = new LocationListener() {

        public void onLocationChanged(Location location) {
        	
        	double latMee = location.getLatitude();
        	double lonMee = location.getLongitude();
        
    		TMapPoint myLocation = new TMapPoint(latMee, lonMee);

	    	pointt.add(myLocation);

	    }



        public void onProviderDisabled(String provider) {
        }

 

        public void onProviderEnabled(String provider) {
        }

 

        public void onStatusChanged(String provider, int status, Bundle extras) {
        	
        }

        

    };

 

    public static Criteria getCriteria() {

        Criteria criteria = new Criteria();

        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setAltitudeRequired(true);

        criteria.setBearingRequired(true);

        criteria.setSpeedRequired(true);

        criteria.setCostAllowed(true);

        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        return criteria;

    }

    private void alertCheckGPS() { //gps꺼져있으면 켤꺼지 체크
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS가 꺼져있습니다.(켜시겟습니까?)")
                .setCancelable(false)
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveConfigGPS();
                            }
                    })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                    });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // GPS 설정화면으로 이동
   private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }
   	
}
