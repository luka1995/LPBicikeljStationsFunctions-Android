/*
 * Luka Penger
 * Software & Hardware Development
 * http://lukapenger.eu
 */

package LPBicikeljStationsFunctions;

import java.security.KeyStore;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import si.rralur.atob.ABSharedManager;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.*;

import LPBicikeljStationsFunctions.LPBicikeljStationDetails.*;
import LPGoogleFunctions.MySSLSocketFactory;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class LPBicikeljStationsFunctions {

	// Variables

	public static final String bicikeljServiceStationsURL = "http://www.bicikelj.si/service/carto";

	public ArrayList<LPBicikeljStationMarker> stationsList;
	public ArrayList<LPBicikeljArrondissement> arrondissementsList;
		
	private Context context;
	
	public AsyncHttpClient client = null;
	
	int loadingStationsDetails = 0;
	int loadingStationsDetailsCount = 0;

	public boolean stationsShown = false;
	
	// Listeners
    
    public interface StationsListener
    {
        public void willLoadStations();
        public void didLoadStations();  
        public void errorLoadingStations(Throwable error);
	}
    
    public interface StationsDetailsListener
    {
        public void willLoadStationsDetails();
        public void didLoadStationsDetails();  
        public void errorLoadingStationsDetails(Throwable error);
	}
    
	// Class
	
	public LPBicikeljStationsFunctions(Context context) 
	{
	    this.context = context;
	    
		this.client = new AsyncHttpClient();
		this.setSSLSocketForClient(client);
		
		this.stationsList = new ArrayList<LPBicikeljStationMarker>();
    	this.arrondissementsList = new ArrayList<LPBicikeljArrondissement>();
	}
    
	// Functions
	
	public void setSSLSocketForClient(AsyncHttpClient client)
	{
		try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        client.setSSLSocketFactory(sf);
	    } catch (Exception e) {

	    }
	}
	
	/**
	 * @param responseHandler	
	 * @Override public void willLoadStations()
     * @Override public void didLoadStations()
     * @Override public void errorLoadingStations(Throwable error)
	 */
	
	public void loadStations(final StationsListener responseHandler)
	{
		try {
			if(responseHandler != null)	responseHandler.willLoadStations();

	    	this.stationsList = new ArrayList<LPBicikeljStationMarker>();
	    	this.arrondissementsList = new ArrayList<LPBicikeljArrondissement>();

	    	this.client.get(bicikeljServiceStationsURL, new AsyncHttpResponseHandler() {
	    	    @Override
	    	    public void onSuccess(String response) {

	    			try {
	    				XMLParser parser = new XMLParser();
	        			Document doc = parser.getDomElement(response);

	        			// Markers
	        			NodeList markers = doc.getElementsByTagName("marker");
	            		
	        	        for (int i=0; i<markers.getLength(); i++) 
	        	        {
	        	        	try {
	        	        		Element element = (Element) markers.item(i);
	        	        		
	        	        		LPBicikeljStationMarker marker = new LPBicikeljStationMarker(element);

	        	        		stationsList.add(marker);
	        	        	} catch (Exception e) {
	        	        		e.printStackTrace();
	        	        	}
	        	        }
	        	        
	        	        // Arrondissements
	        	        NodeList arrondissements = doc.getElementsByTagName("arrondissement");
	        	        
	        	        for (int i=0; i<arrondissements.getLength(); i++)
	        	        {
	        	        	try {
	        	        		Element element = (Element) arrondissements.item(i);
	        	        		
	        	        		LPBicikeljArrondissement arrondissement = new LPBicikeljArrondissement(element);

	        	        		arrondissementsList.add(arrondissement);
	        	        	} catch (Exception e) {
	        	        		e.printStackTrace();
	        	        	}
	        	        }
	        		} catch (Exception e) {
	        			e.printStackTrace();
	        			
	        			if(responseHandler != null)	responseHandler.errorLoadingStations(e.getCause());
	        		}     
	    			
	    			if(responseHandler != null)	responseHandler.didLoadStations();
	    			
	    			saveStations();
	    	    }

	    	    @Override
	    	    public void onFailure(Throwable error) {

	    	    	if(responseHandler != null)	responseHandler.errorLoadingStations(error);
	    	    }
	    	});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * @param responseHandler	
	 * @Override public void willLoadStationsDetails()
     * @Override public void didLoadStationsDetails()
     * @Override public void errorLoadingStationsDetails(Throwable error)
	 */
	
	public void loadStationsDetailsForStations(final StationsDetailsListener responseHandler) 
	{
		try {
			if(responseHandler != null)	responseHandler.willLoadStationsDetails();
			
			this.loadingStationsDetailsCount = this.stationsList.size();
			
			for(int i=0; i<this.loadingStationsDetailsCount; i++)
			{
				final LPBicikeljStationMarker marker = this.stationsList.get(i);

				marker.stationDetails = new LPBicikeljStationDetails();
				
				marker.stationDetails.loadStationDetailsForNumber(marker.number, new StationDetailsListener() {

					@Override
					public void willLoadStationDetails() {

					}

					@Override
					public void didLoadStationDetails() {
		    			loadingStationsDetails ++;

		    			if(loadingStationsDetails >= loadingStationsDetailsCount)
		    			{
		    				loadingStationsDetails = 0;
		    				if(responseHandler != null)	responseHandler.didLoadStationsDetails();
		    				
		    				saveStations();
		    			}
					}

					@Override
					public void errorLoadingStationDetails(Throwable error) {
		    			loadingStationsDetails ++;
		    			
		    			if(loadingStationsDetails >= loadingStationsDetailsCount)
		    			{
		    				loadingStationsDetails = 0;
		    				if(responseHandler != null)	responseHandler.didLoadStationsDetails();
		    				
		    				saveStations();
		    			}
					}
					
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			responseHandler.errorLoadingStationsDetails(e.getCause());
		}
	}

	public void loadSavedStations() 
	{
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences("LPBicikeljStationsFunctions", Activity.MODE_PRIVATE);
			
			// StationsList
			
			this.stationsList.clear();
			
			String jsonStationsList = sharedPreferences.getString("stationsList", "");
			
			if(jsonStationsList != null && jsonStationsList.length() > 0)
			{
				JSONArray array = new JSONArray(jsonStationsList);
				
				for(int i=0; i<array.length(); i++)
				{
					LPBicikeljStationMarker marker = new LPBicikeljStationMarker(array.getJSONObject(i));
					
					this.stationsList.add(marker);
				}
			}

			// ArrondissementsList
			
			this.arrondissementsList.clear();
			
			String jsonArrondissementsList = sharedPreferences.getString("arrondissementsList", "");

			if(jsonArrondissementsList != null && jsonArrondissementsList.length() > 0)
			{
				JSONArray array = new JSONArray(jsonArrondissementsList);
				
				for(int i=0; i<array.length(); i++)
				{
					LPBicikeljArrondissement arrondissement = new LPBicikeljArrondissement(array.getJSONObject(i));
					
					this.arrondissementsList.add(arrondissement);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveStations()
	{
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences("LPBicikeljStationsFunctions", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			
			JSONArray arrayStations = new JSONArray();
			
			for(LPBicikeljStationMarker marker : this.stationsList)
			{
				JSONObject object = marker.getJSONObject();

				arrayStations.put(object);
			}
			
	        editor.remove("stationsList");
	        editor.putString("stationsList", arrayStations.toString());
	        
	        JSONArray arrayArrondissements = new JSONArray();
			
			for(LPBicikeljArrondissement arrondissement : this.arrondissementsList)
			{
				JSONObject object = arrondissement.getJSONObject();

				arrayArrondissements.put(object);
			}
			
	        editor.remove("arrondissementsList");
	        editor.putString("arrondissementsList", arrayArrondissements.toString());
			
	        editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LPBicikeljStationMarker nearestStationForStartLocation(LatLng location)
	{
	    float minDistance = Float.MAX_VALUE;
	    int minDistanceNumber = Integer.MAX_VALUE;

	    if(this.stationsList != null)
	    {
	    	for(int i=0; i<this.stationsList.size(); i++)
	    	{
	    		LPBicikeljStationMarker stationMarker = this.stationsList.get(i);

	    		if(stationMarker != null)
	    		{
	    			LatLng point = new LatLng(stationMarker.latitude, stationMarker.longitude);
	    			
	    			float distance = ABSharedManager.getDistanceBetweenTwoCoordinates(location, point);
	    			
	    			if(stationMarker.stationDetails != null)
	    			{
		    			if(distance <= minDistance && stationMarker.open > 0 && stationMarker.stationDetails.available > 0)
		    			{
		    	            minDistance = distance;
		    	            minDistanceNumber = i;
		    	        }
	    			}
	    		}
	    	}
	    	
		    if (minDistanceNumber != Integer.MAX_VALUE)
		    {
				return this.stationsList.get(minDistanceNumber).clone();
			}
	    }

	    return null;
	}

	public LPBicikeljStationMarker nearestStationForEndLocation(LatLng location)
	{
	    float minDistance = Float.MAX_VALUE;
	    int minDistanceNumber = Integer.MAX_VALUE;

	    if(this.stationsList != null)
	    {
	    	for(int i=0; i<this.stationsList.size(); i++)
	    	{
	    		LPBicikeljStationMarker stationMarker = this.stationsList.get(i);
	    		
	    		if(stationMarker != null)
	    		{
	    			LatLng point = new LatLng(stationMarker.latitude, stationMarker.longitude);
	    			
	    			float distance = ABSharedManager.getDistanceBetweenTwoCoordinates(location, point);
	    			
	    			if(stationMarker.stationDetails != null)
	    			{
		    			if(distance <= minDistance && stationMarker.open > 0 && stationMarker.stationDetails.free > 0)
		    			{
		    	            minDistance = distance;
		    	            minDistanceNumber = i;
		    	        }
	    			}
	    		}
	    	}
	    	
		    if (minDistanceNumber != Integer.MAX_VALUE)
		    {
				return this.stationsList.get(minDistanceNumber).clone();
			}
	    }
	    
	    return null;
	}
}
