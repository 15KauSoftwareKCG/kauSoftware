package com.example.tmaptest;

import java.util.ArrayList;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapInfo;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;
import com.skp.Tmap.TMapData.FindPathDataListenerCallback;
import com.skp.Tmap.TMapData.TMapPathType;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ListAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private ArrayList <TMapPOIItem> data = new ArrayList<TMapPOIItem>();
	private TMapPoint MarkerPoint, StartPoint, EndPoint;
	private Dialog popMarker,dia;
	private TMapData tmapdata;
	//private TMapView mMapView;

	public ListAdapter(Context context,Dialog popMarker,Dialog dia,TMapData tmapdata, TMapView mMapView,
			TMapPoint MarkerPoint,TMapPoint StartPoint,TMapPoint EndPoint) {
		mContext = context; this.popMarker=popMarker; this.dia=dia; this.tmapdata=tmapdata;
		this.MarkerPoint=MarkerPoint; this.StartPoint=StartPoint; this.EndPoint=EndPoint;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int mposition, View convertView, ViewGroup parent) {
		
		final int position = mposition;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater ) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_detail,null);
			TextView text = (TextView) convertView.findViewById(R.id.poiName);
			text.setText(data.get(position).getPOIName());
			Button detailbutton = (Button)convertView.findViewById(R.id.detail);
			detailbutton.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					final TextView pName = (TextView)popMarker.findViewById(R.id.pName);
					final TextView pPhone = (TextView)popMarker.findViewById(R.id.pPhone);
					final TextView pAddress = (TextView)popMarker.findViewById(R.id.pAddress);
					
					pName.setText("　이름: "+data.get(position).name);
					if(data.get(position).telNo==null) pPhone.setText("　전화번호: ");
					else pPhone.setText("　전화번호: "+data.get(position).telNo);
					
					if(data.get(position).detailAddrName==null)
					{
						pAddress.setText("　주소: "+data.get(position).upperAddrName 
								+ " " + data.get(position).middleAddrName 
								+ " " + data.get(position).lowerAddrName);
					}
					else
					{
						pAddress.setText("　주소: "+data.get(position).upperAddrName 
								+ " " + data.get(position).middleAddrName 
								+ " " + data.get(position).lowerAddrName 
	    									+ " " + data.get(position).detailAddrName);
					
		    		}   
	    			popMarker.setTitle("상세 정보");
	    			MarkerPoint.setLatitude(data.get(position).getPOIPoint().getLatitude());
					MarkerPoint.setLongitude(data.get(position).getPOIPoint().getLongitude());
    				popMarker.show();
				}
			});
			
			Button popStart = (Button)popMarker.findViewById(R.id.pbutton3);
			Button popOk = (Button)popMarker.findViewById(R.id.pbutton1);
			Button popCancle = (Button)popMarker.findViewById(R.id.pbutton2);
			
			 //출발지 설정버튼
			popStart.setOnClickListener(new Button.OnClickListener(){
	    		public void onClick(View v) {
	    			popMarker.dismiss();
	    			StartPoint.setLatitude(MarkerPoint.getLatitude());
	    			StartPoint.setLongitude(MarkerPoint.getLongitude());
	    		}
	    	});
			
			//길찾기 버튼 클릭
			 popOk.setOnClickListener(new Button.OnClickListener() {

				    public void onClick(View v) {

				    	popMarker.dismiss();
				    	//startPoint = TMapPoint(latCityhall, lonCityhall);
				    	//startPoint.setLatitude(latMe);
				    	//startPoint.setLongitude(lonMe);

				    	EndPoint.setLatitude(MarkerPoint.getLatitude());
				    	EndPoint.setLongitude(MarkerPoint.getLongitude());
				    	
				    	
				    	tmapdata.findPathDataWithType(TMapPathType.BICYCLE_PATH, StartPoint, EndPoint,
					    		new FindPathDataListenerCallback() {
					    		public void onFindPathData(TMapPolyLine polyLine) {
					    			polyLine.setLineColor(Color.GREEN);
					    			MainActivity.mMapView.addTMapPath(polyLine);
					    		}
					    }
						);
				    	
				    	MainActivity.getJsonData(StartPoint, EndPoint);

				    	//mMapView.setCenterPoint((startPoint.getLongitude() + endPoint.getLongitude())/2
				    	//		,(startPoint.getLatitude() + endPoint.getLatitude())/2);
				    	
				    	// startPoint, endPoint에 맞는 줌, 중심 구하기
				    	ArrayList<TMapPoint> point = new ArrayList<TMapPoint>();
				    	point.add(StartPoint);
				    	point.add(EndPoint);
				    	TMapInfo info = MainActivity.mMapView.getDisplayTMapInfo(point);
				    	MainActivity.mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
				    	MainActivity.mMapView.setZoomLevel(info.getTMapZoomLevel()); 
				    	dia.dismiss();
				    }

				});
			//상세정보 닫기버튼 클릭시
			    popCancle.setOnClickListener(new Button.OnClickListener() {

				    public void onClick(View v) {

				    	popMarker.dismiss();
				    	
				    }

				});
			    
		}
		return convertView;
	}
	public void clear(){
		data.clear();
	}

	public void add(TMapPOIItem poiName) {
		data.add(poiName);
	}
	

}
