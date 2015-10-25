package com.example.tmaptest;

import java.util.ArrayList;

import com.skp.Tmap.TMapInfo;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
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

public class ListAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private ArrayList <TMapPOIItem> data = new ArrayList<TMapPOIItem>();
	private TMapPoint endPoint;
	private Dialog popMarker,dia;

	public ListAdapter(Context context,Dialog popMarker,Dialog dia,TMapPoint endPoint) {
		mContext = context; this.popMarker=popMarker; this.dia=dia; this.endPoint=endPoint;
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
	    			endPoint.setLatitude(data.get(position).getPOIPoint().getLatitude());
					endPoint.setLongitude(data.get(position).getPOIPoint().getLongitude());
    				popMarker.show();
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
