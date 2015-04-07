package com.example.tmaptest;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapData.TMapPathType;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapTapi;
import com.skp.Tmap.TMapView;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends Activity {

	private RelativeLayout mMainRelativeLayout =null;
	 
	Location location = null;
	  LocationManager lm = null; 
	  
	  // 서울 광장 (WGS84)
	  double lat=37.5657321; // 위도 
	  double lon=126.9786599; // 경도
	  
	  // 한국항공대
	  double latKau = 37.600624;
	  double lonKau = 126.864388;
	  
	  
	  int zoom = 15; // defualt zoom
	  
	protected void onCreate(Bundle savedInstanceState)
	 
	{
	 
	      super.onCreate(savedInstanceState);
	 
	      setContentView(R.layout.activity_main);
	 
	      mMainRelativeLayout =(RelativeLayout)findViewById(R.id.mainRelativeLayout);
	 
	      final TMapView mMapView = new TMapView(this);        // TmapView생성
	      mMapView.setSKPMapApiKey("e2a7df79-5bc7-3f7f-8bca-2d335a0526e7");  //SDK 인증키입력
	 	 
	      
	      mMainRelativeLayout.addView(mMapView);
	 
	      TMapData tmapdata = new TMapData();
	     
	      mMapView.setCenterPoint(lon, lat);
	      mMapView.setLocationPoint(lon, lat);
	      
	      TMapPoint tpoint = mMapView.getLocationPoint();
	      
	      TMapPoint startpoint = new TMapPoint(lon, lat);
	      TMapPoint endpoint = new TMapPoint(37.4601, 128.0428);
	      

			TMapPolyLine polyLine = new TMapPolyLine();
				try {
						polyLine = tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH,
							  startpoint, endpoint);
						//mMapView.addTMapPath(polyLine);
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
				
			
			//mMapView.addTMapPath(polyLine);
			//mMapView.addTMapPolyLine("test",polyLine);
			
			//mMapView.setIconVisibility(false);
			

			
		      findViewById(R.id.button1).setOnClickListener(
		    		  new Button.OnClickListener() {
		    			  public void onClick(View v) {
		    				  finish();
		    			  }
		    		  }
		    		  );
		      findViewById(R.id.button2).setOnClickListener(
		    		  new Button.OnClickListener() {
		    			  public void onClick(View v) {
		    				  
		    				 
		    				//lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);  
		    			  	//lat = location.getLatitude();
		    			  	//lon = location.getLongitude();
		    			  	
		    			  Toast toast = Toast.makeText(MainActivity.this,"GPS (WGS84)\n위도 : " + lat + "\n경도: " + lon, Toast.LENGTH_LONG);
		    			  mMapView.setZoomLevel(15); // default zoom
		    			  mMapView.setCenterPoint(lon, lat);
		    			  mMapView.setIconVisibility(true);
		    			  toast.show();
			    			 
		    			  }
		    		  }
		    		  );
		      findViewById(R.id.button3).setOnClickListener(
		    		  new Button.OnClickListener() {
		    			  public void onClick(View v) {
		    				  mMapView.setIconVisibility(false);
		    				  mMapView.setZoomLevel(16);
		    				  mMapView.setCenterPoint(lonKau, latKau);
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
    
    
    
    
}

