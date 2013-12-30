/*
 * Luka Penger
 * Software & Hardware Development
 * http://lukapenger.eu
 */

package LPBicikeljStationsFunctions;

import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import LPGoogleFunctions.MySSLSocketFactory;

import com.loopj.android.http.*;

public class LPBicikeljStationDetails {

	public static final String bicikeljServiceStationDetailsURL = "http://www.bicikelj.si/service/stationdetails/ljubljana/%d";

	// Variables
	
	public int available = 0;
	public int free = 0;
	public int total = 0;
	public int ticket = 0;
	public int open = 0;
	public int updated = 0;
	public int connected = 0;
	
	public AsyncHttpClient client = null;
	
	// Listeners
    
    public interface StationDetailsListener
    {
        public void willLoadStationDetails();
        public void didLoadStationDetails();  
        public void errorLoadingStationDetails(Throwable error);
	}

	// Class

	public LPBicikeljStationDetails()
	{
		this.client = new AsyncHttpClient();
		this.setSSLSocketForClient(client);
	}
	
	public LPBicikeljStationDetails(Element element)
	{
		try {
			if(element.hasAttribute("available"))
			{
				this.available = Integer.valueOf(element.getAttribute("available"));
			}
			
			if(element.hasAttribute("free"))
			{
				this.free = Integer.valueOf(element.getAttribute("free"));
			}
			
			if(element.hasAttribute("total"))
			{
				this.total = Integer.valueOf(element.getAttribute("total"));
			}
			
			if(element.hasAttribute("ticket"))
			{
				this.ticket = Integer.valueOf(element.getAttribute("ticket"));
			}
			
			if(element.hasAttribute("open"))
			{
				this.open = Integer.valueOf(element.getAttribute("open"));
			}
			
			if(element.hasAttribute("updated"))
			{
				this.updated = Integer.valueOf(element.getAttribute("updated"));
			}
			
			if(element.hasAttribute("connected"))
			{
				this.connected = Integer.valueOf(element.getAttribute("connected"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public LPBicikeljStationDetails(JSONObject element)
	{
		try {
			if(element.has("available"))
			{
				this.available = element.getInt("available");
			}
			
			if(element.has("free"))
			{
				this.free = element.getInt("free");
			}
			
			if(element.has("total"))
			{
				this.total = element.getInt("total");
			}
			
			if(element.has("ticket"))
			{
				this.ticket = element.getInt("ticket");
			}
			
			if(element.has("open"))
			{
				this.open = element.getInt("open");
			}
			
			if(element.has("updated"))
			{
				this.updated = element.getInt("updated");
			}
			
			if(element.has("connected"))
			{
				this.connected = element.getInt("connected");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LPBicikeljStationDetails clone()
	{
		LPBicikeljStationDetails object = new LPBicikeljStationDetails();
    	
        try {
        	object.available = this.available;
        	object.free = this.free;
        	object.total = this.total;
        	object.ticket = this.ticket;
        	object.open = this.open;
        	object.updated = this.updated;
        	object.connected = this.connected;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return object;
	}
	
	public JSONObject getJSONObject()
	{
		JSONObject object = new JSONObject();
		
		try {
			object.put("available", this.available);
			object.put("free", this.free);
			object.put("total", this.total);
			object.put("ticket", this.ticket);
			object.put("open", this.open);
			object.put("updated", this.updated);
			object.put("connected", this.connected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return object;
	}
	
	// Functions
	
	public void setSSLSocketForClient(AsyncHttpClient client)
	{
		try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        client.setSSLSocketFactory(sf);
	    } catch (Exception e) {

	    }
	}
	
	/**
	 * @param responseHandler	
	 * @Override public void willLoadStationDetails()
     * @Override public void didLoadStationsDetailsMessage()
     * @Override public void errorLoadingStationsDetailsMessage(Throwable error)
	 */
	
	public void loadStationDetailsForNumber(final int number, final StationDetailsListener responseHandler)
	{
		if(responseHandler != null)	responseHandler.willLoadStationDetails();
		
		String URL = String.format(bicikeljServiceStationDetailsURL, number);

		this.client.get(URL, new AsyncHttpResponseHandler() {
    		@Override
    	    public void onSuccess(String response) {
    			try {
    				XMLParser parser = new XMLParser();
        			Document doc = parser.getDomElement(response);

        			NodeList stations = doc.getElementsByTagName("station");
            		
        			if(stations.getLength()>0)
        			{
        				Element station = (Element) stations.item(0);

        				available = Integer.valueOf(station.getElementsByTagName("available").item(0).getTextContent());
        				free = Integer.valueOf(station.getElementsByTagName("free").item(0).getTextContent());
        				total= Integer.valueOf(station.getElementsByTagName("total").item(0).getTextContent());
        				ticket = Integer.valueOf(station.getElementsByTagName("ticket").item(0).getTextContent());
        				open = Integer.valueOf(station.getElementsByTagName("open").item(0).getTextContent());
        				updated = Integer.valueOf(station.getElementsByTagName("updated").item(0).getTextContent());
        				connected = Integer.valueOf(station.getElementsByTagName("connected").item(0).getTextContent());
        			}        			
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
    			
    			if(responseHandler != null)	responseHandler.didLoadStationDetails();
    	    }

    	    @Override
    	    public void onFailure(Throwable error) {
    	    	if(responseHandler != null)	responseHandler.errorLoadingStationDetails(error);
    	    }
    	});
	}
}
