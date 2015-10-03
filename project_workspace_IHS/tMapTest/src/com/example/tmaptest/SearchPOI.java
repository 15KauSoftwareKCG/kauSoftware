package com.example.tmaptest;


import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapData.FindTitlePOIListenerCallback;
import com.skp.Tmap.TMapInfo;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

public class SearchPOI extends Activity{
	private Dialog popMarker, dia;
	private TMapPoint MarkerPoint, StartPoint, EndPoint;
	private TMapData tmapdata;
	private Resources resources;
	//private TMapView mMapView;
	private ArrayList<TMapPOIItem> POIItem;
	
	public SearchPOI(Dialog Dia, Dialog popMarker, 
			TMapPoint MarkerPoint, TMapPoint StartPoint, TMapPoint EndPoint,
			TMapData tmapdata, Resources r, TMapView mMapView,ArrayList<TMapPOIItem> p){
			this.dia = Dia;
			this.popMarker = popMarker;
			this.MarkerPoint = MarkerPoint;
			this.StartPoint = StartPoint;
			this.EndPoint = EndPoint;
			this.tmapdata = tmapdata;
			this.resources = r;
			//this.mMapView= mMapView;
			POIItem =p;
	}

	public void SearchIt(){
	dia.setContentView(R.layout.set_endpoint);

	dia.setTitle("검색할 장소");
	
	dia.show();

	final Button btn = (Button)dia.findViewById(R.id.cbutton1);

	final EditText et = (EditText)dia.findViewById(R.id.editText1);

	final ArrayList<TMapPoint> point = new ArrayList<TMapPoint>();
	
	
	dia.findViewById(R.id.map1).setOnClickListener( //지도보기 클릭
			new Button.OnClickListener() {
    			public void onClick(View v) {
    				TMapInfo info = MainActivity.mMapView.getDisplayTMapInfo(point);
    				MainActivity.mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
    				MainActivity.mMapView.setZoomLevel(info.getTMapZoomLevel());
			    	
    				dia.dismiss();
    			}
			}
	);

    final ListView list = (ListView)dia.findViewById(R.id.delist);
    final ListAdapter listadapter = new ListAdapter(dia.getContext(),popMarker,dia, tmapdata, MainActivity.mMapView, MarkerPoint,StartPoint,EndPoint);

    
	// 장소검색 창의 검색 버튼 클릭
	btn.setOnClickListener(new Button.OnClickListener() {

	    public void onClick(View v) {

	    	String textEndPoint = et.getText().toString(); // 가상휴대폰에서 한글입력 지원안됨, 추후 확인
	    	//textEndPoint="서울역";
	    	
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
						MainActivity.mMapView.removeMarkerItem(item.getPOIID());
					}
					POIItem.clear();
				}
	    		
//	    		for(TMapPOIItem item : poiItem){
//	    			POIItem.add(item);
//		    	}
	    		
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
	    		Bitmap bitmap = BitmapFactory.decodeResource(resources,R.drawable.pin_r_bm_o);
		    	tMapMarkerItem.setIcon(bitmap);
	    		tMapMarkerItem.setPosition((float)0.5, (float)1.0);
	    		
	    		listadapter.add(item);
	    		point.add(item.getPOIPoint());
	    
	    		MainActivity.mMapView.bringMarkerToFront(tMapMarkerItem);
	    				 
	    		
	    		//mMapView.addTMapPOIItem(poiItem);
	    		
	    		//if(i==0) mMapView.setCenterPoint(item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude());
	    		MainActivity.mMapView.addMarkerItem(item.getPOIID(), tMapMarkerItem);
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
	    }}
	);}}

