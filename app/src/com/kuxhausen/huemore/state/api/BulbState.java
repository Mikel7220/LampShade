package com.kuxhausen.huemore.state.api;

import com.google.gson.Gson;

public class BulbState {

	/**
	 * On/Off state of the light. On=true, Off=false
	 */
	public Boolean on;
	/**
	 * The brightness value to set the light to. Brightness is a scale from 0
	 * (the minimum the light is capable of) to 255 (the maximum). Note: a
	 * brightness of 0 is not off.
	 */
	public Integer bri;
	/**
	 * The hue value to set light to. The hue value is a wrapping value between
	 * 0 and 65535. Both 0 and 65535 are red, 25500 is green and 46920 is blue.
	 * e.g. �hue�: 50000 will set the light to a specific hue.
	 */
	private Integer hue;
	/**
	 * Saturation of the light. 255 is the most saturated (colored) and 0 is the
	 * least saturated (white).
	 */
	private Short sat;
	/**
	 * The x and y coordinates of a color in CIE color space. The first entry is
	 * the x coordinate and the second entry is the y coordinate. Both x and y
	 * must be between 0 and 1. If the specified coordinates are not in the CIE
	 * color space, the closest color to the coordinates will be chosen.
	 */
	public Float[] xy;
	/**
	 * The Mired Color temperature of the light. 2012 connected lights are
	 * capable of 153 (6500K) to 500 (2000K).
	 */
	public Integer ct;

	/**
	 * The alert effect, is a temporary change to the bulb�s state, and has one
	 * of the following values: �none� � The light is not performing an alert
	 * effect. �select� � The light is performing one breathe cycle. �lselect� �
	 * The light is performing breathe cycles for 30 seconds or until an
	 * "alert": "none" command is received.
	 */
	public String alert;

	/**
	 * The dynamic effect of the light, can either be "none" or "colorloop"
	 */
	public String effect;

	/**
	 * The duration of the transition from the light�s current state to the new
	 * state. This is given as a multiple of 100ms and defaults to 4 (400ms).
	 * For example, setting transistiontime:10 will make the transition last 1
	 * second.
	 */
	public Integer transitiontime;

	public BulbState() {
	}

	/**
	 * Must ensure uniqueness for HueUrlEncoder
	 */
	@Override
	public String toString() {
		String result = "";
		if (on != null)
			result += "on:" + (on ? "true" : "false") + " ";
		if (bri != null)
			result += "bri:" + bri + " ";
		if (hue != null)
			result += "hue:" + hue + " ";
		if (sat != null)
			result += "sat:" + sat + " ";
		if (xy != null)
			result += "xy:" + xy[0] + " " + xy[1] + " ";
		if (ct != null)
			result += "ct:" + ct + " ";
		if (alert != null)
			result += "alert:" + alert + " ";
		if (effect != null)
			result += "effect:" + effect + " ";
		if (transitiontime != null)
			result += "transitiontime:" + transitiontime + " ";
		return result;
	}
	
	/* when in doubt, override, only allow 1 of three color modes**/
	public void merge(BulbState other){
		on = (other.on!=null) ? other.on : on;
		bri = (other.bri!=null) ? other.bri : bri;
		alert = (other.alert!=null) ? other.alert : alert;
		effect = (other.effect!=null) ? other.effect : effect;
		transitiontime = (other.transitiontime!=null) ? other.transitiontime : transitiontime;
		
		if(other.xy!=null){
			xy = other.xy;
			hue = null;
			sat = null;
			ct = null;
		} else if(other.hue!=null && other.sat!=null){
			xy = null;
			hue = other.hue;
			sat = other.sat;
			ct = null;
		} else if(other.ct!=null) {
			xy = null;
			hue = null;
			sat = null;
			ct = other.ct;
		}
	}
	
	/** return a BulbState with the values of o when the differ from this object */
	public BulbState delta(BulbState o){
		BulbState delta = new BulbState();
		
		if(o.on!=null && (on==null || !on.equals(o.on)))
			delta.on = o.on;
		if(o.bri!=null && (bri==null || !bri.equals(o.bri)))
			delta.bri = o.bri;
		if(o.alert!=null && (alert==null || !alert.equals(o.alert)))
			delta.alert = o.alert;
		if(o.effect!=null && (effect==null || !effect.equals(o.effect)))
			delta.effect = o.effect;
		if(o.transitiontime!=null && (transitiontime==null || !transitiontime.equals(o.transitiontime)))
			delta.transitiontime = o.transitiontime;
		
		
		if(o.xy!=null && (xy==null || !xy.equals(o.xy))){
			delta.xy = o.xy;
		} else if(o.ct!=null && (ct==null || !ct.equals(o.ct))){
			delta.ct = o.ct;
		}
		return delta;
	}
	
	public String getCT(){
		return (1000000 / ct)+"K";
		
	}
	
	public BulbState clone(){
		Gson gson = new Gson();
		try{
			return gson.fromJson(gson.toJson(this), BulbState.class);
		} catch (Exception e){
			return new BulbState();
		}
	}
}
