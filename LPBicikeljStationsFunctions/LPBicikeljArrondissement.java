/*
 * Luka Penger
 * Software & Hardware Development
 * http://lukapenger.eu
 */

package LPBicikeljStationsFunctions;

import org.json.JSONObject;
import org.w3c.dom.Element;

public class LPBicikeljArrondissement {

	// Variables
	
	public int number;
	public double minLatitude;
	public double minLongitude;
	public double maxLatitude;
	public double maxLongitude;
	
	// Class
	
	public LPBicikeljArrondissement()
	{
		
	}
	
	public LPBicikeljArrondissement(Element element)
	{
		try {
			if(element.hasAttribute("number"))
			{
				this.number = Integer.valueOf(element.getAttribute("number"));
			}
			
			if(element.hasAttribute("minLat"))
			{
				this.minLatitude = Double.valueOf(element.getAttribute("minLat"));
			}
			
			if(element.hasAttribute("minLng"))
			{
				this.minLongitude = Double.valueOf(element.getAttribute("minLng"));
			}
			
			if(element.hasAttribute("maxLat"))
			{
				this.maxLatitude = Double.valueOf(element.getAttribute("maxLat"));
			}
			
			if(element.hasAttribute("maxLng"))
			{
				this.maxLongitude = Double.valueOf(element.getAttribute("maxLng"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LPBicikeljArrondissement(JSONObject element)
	{
		try {
			if(element.has("number"))
			{
				this.number = element.getInt("number");
			}
			
			if(element.has("minLat"))
			{
				this.minLatitude = element.getDouble("minLat");
			}
			
			if(element.has("minLng"))
			{
				this.minLongitude = element.getDouble("minLng");
			}
			
			if(element.has("maxLat"))
			{
				this.maxLatitude = element.getDouble("maxLat");
			}
			
			if(element.has("maxLng"))
			{
				this.maxLongitude = element.getDouble("minLng");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LPBicikeljArrondissement clone()
	{
		LPBicikeljArrondissement object = new LPBicikeljArrondissement();
    	
        try {
        	object.number = this.number;
        	object.minLatitude = this.minLatitude;
        	object.minLongitude = this.minLongitude;
        	object.maxLatitude = this.maxLatitude;
        	object.maxLongitude = this.maxLongitude;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return object;
	}
	
	public JSONObject getJSONObject()
	{
		JSONObject object = new JSONObject();
		
		try {
			object.put("number", this.number);
			object.put("minLat", this.minLatitude);
			object.put("minLng", this.minLongitude);
			object.put("maxLat", this.maxLatitude);
			object.put("maxLng", this.maxLongitude);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return object;
	}
}
