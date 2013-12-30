/*
 * Luka Penger
 * Software & Hardware Development
 * http://lukapenger.eu
 */

package LPBicikeljStationsFunctions;

import org.json.JSONObject;
import org.w3c.dom.Element;

import LPBicikeljStationsFunctions.LPBicikeljStationDetails;

public class LPBicikeljStationMarker {
	
	// Variables
	
	public String name = null;
	public int number = 0;
	public String address = null;
	public String fullAddress = null;
	public double latitude = 0;
	public double longitude = 0;
	public int open = 0;
	public int bonus = 0;
	public LPBicikeljStationDetails stationDetails = null;

	// Class
	
	public LPBicikeljStationMarker()
	{
		
	}
	
	public LPBicikeljStationMarker(Element element)
	{
		try {
			if(element.hasAttribute("name"))
			{
				this.name = element.getAttribute("name");
			}
			
			if(element.hasAttribute("number"))
			{
				this.number = Integer.valueOf(element.getAttribute("number"));
			}
			
			if(element.hasAttribute("address"))
			{
				this.address = element.getAttribute("address");
			}
			
			if(element.hasAttribute("fullAddress"))
			{
				this.fullAddress = element.getAttribute("fullAddress");
			}
			
			if(element.hasAttribute("lat"))
			{
				this.latitude = Double.valueOf(element.getAttribute("lat"));
			}
			
			if(element.hasAttribute("lng"))
			{
				this.longitude = Double.valueOf(element.getAttribute("lng"));
			}
			
			if(element.hasAttribute("open"))
			{
				this.open = Integer.valueOf(element.getAttribute("open"));
			}
			
			if(element.hasAttribute("bonus"))
			{
				this.bonus = Integer.valueOf(element.getAttribute("bonus"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LPBicikeljStationMarker(JSONObject element)
	{
		try {
			if(element.has("name"))
			{
				this.name = element.getString("name");
			}
			
			if(element.has("number"))
			{
				this.number = element.getInt("number");
			}
			
			if(element.has("address"))
			{
				this.address = element.getString("address");
			}
			
			if(element.has("fullAddress"))
			{
				this.fullAddress = element.getString("fullAddress");
			}
			
			if(element.has("lat"))
			{
				this.latitude = element.getDouble("lat");
			}
			
			if(element.has("lng"))
			{
				this.longitude = element.getDouble("lng");
			}
			
			if(element.has("open"))
			{
				this.open = element.getInt("open");
			}
			
			if(element.has("bonus"))
			{
				this.bonus = element.getInt("bonus");
			}
			
			if(element.has("stationDetails"));
			{
				this.stationDetails = new LPBicikeljStationDetails(element.getJSONObject("stationDetails"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadDetails()
	{
		this.stationDetails = new LPBicikeljStationDetails();
		this.stationDetails.loadStationDetailsForNumber(this.number, null);
	}
	
	public LPBicikeljStationMarker clone()
	{
		LPBicikeljStationMarker object = new LPBicikeljStationMarker();
    	
        try {
        	object.name = this.name;
        	object.number = this.number;
        	object.address = this.address;
        	object.fullAddress = this.fullAddress;
        	object.latitude = this.latitude;
        	object.longitude = this.longitude;
        	object.open = this.open;
        	object.bonus = this.bonus;
        	object.stationDetails = this.stationDetails;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return object;
	}
	
	public JSONObject getJSONObject()
	{
		JSONObject object = new JSONObject();
		
		try {
			object.put("name", this.name);
			object.put("number", this.number);
			object.put("address", this.address);
			object.put("fullAddress", this.fullAddress);
			object.put("lat", this.latitude);
			object.put("lng", this.longitude);
			object.put("open", this.open);
			object.put("bonus", this.bonus);
			object.put("stationDetails", this.stationDetails.getJSONObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return object;
	}
}
