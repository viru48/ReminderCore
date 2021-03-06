package com.onesixty.seven.core.objects;

import com.onesixty.seven.core.intefaces.ILocation;

/**
 * The Class PhoneSettingObject.
 */
public class PhoneSetting extends Notification {

	/** The vibration. */
	private boolean vibration;

	/** The wifi. */
	private boolean wifi;

	/** The bluetooth. */
	private boolean bluetooth;

	/** The mobile data. */
	private boolean mobileData;

	/** The sound level. */
	private int soundLevel;

	/**
	 * Instantiates a new phone setting object based on time.
	 *
	 * @param id the id
	 * @param title the title
	 * @param time the time
	 * @param vibration the vibration
	 * @param wifi the wifi
	 * @param bluetooth the bluetooth
	 * @param mobileData the mobile data
	 * @param soundLevel the sound level
	 * @param pDistnace the distnace
	 * @param notifyUser the notify user
	 */
	public PhoneSetting(final long id, final String title, long time, boolean vibration, boolean wifi, boolean bluetooth, boolean mobileData,
			int soundLevel, float pDistnace, boolean notifyUser) {

		super(id, title, time);
		this.vibration = vibration;
		this.wifi = wifi;
		this.bluetooth = bluetooth;
		this.mobileData = mobileData;
		this.soundLevel = soundLevel;
	}

	/**
	 * Constructor to initialize phone setting notification based on location.
	 *
	 * @param id the id
	 * @param title the title
	 * @param location the location
	 * @param vibration the vibration
	 * @param wifi the wifi
	 * @param bluetooth the bluetooth
	 * @param mobileData the mobile data
	 * @param soundLevel the sound level
	 * @param pDistnace the distnace
	 * @param notifyUser the notify user
	 */
	public PhoneSetting(final long id, final String title, ILocation location, boolean vibration, boolean wifi, boolean bluetooth,
			boolean mobileData, int soundLevel, float pDistnace, boolean notifyUser) {

		super(id, title, location);
		this.vibration = vibration;
		this.wifi = wifi;
		this.bluetooth = bluetooth;
		this.mobileData = mobileData;
		this.soundLevel = soundLevel;
	}
	
	/**
	 * Constructor to initialize phone setting notification based neither on location nor time.
	 *
	 * @param id the id
	 * @param title the title
	 * @param vibration the vibration
	 * @param wifi the wifi
	 * @param bluetooth the bluetooth
	 * @param mobileData the mobile data
	 * @param soundLevel the sound level
	 * @param pDistnace the distnace
	 * @param notifyUser the notify user
	 */
	public PhoneSetting(final long id, final String title, boolean vibration, boolean wifi, boolean bluetooth,
			boolean mobileData, int soundLevel, float pDistnace, boolean notifyUser) {

		super(id, title);
		this.vibration = vibration;
		this.wifi = wifi;
		this.bluetooth = bluetooth;
		this.mobileData = mobileData;
		this.soundLevel = soundLevel;
	}

	/**
	 * Checks if vibration is ON or OFF.
	 * 
	 * @return true, if is vibration
	 */
	public boolean isVibration() {
		return vibration;
	}

	/**
	 * Sets the vibration.
	 * 
	 * @param vibration
	 *            the new vibration
	 */
	public void setVibration(boolean vibration) {
		this.vibration = vibration;
	}

	/**
	 * Checks if wifi is ON or OFF.
	 * 
	 * @return true, if is wifi
	 */
	public boolean isWifi() {
		return wifi;
	}

	/**
	 * Sets the wifi.
	 * 
	 * @param wifi
	 *            the new wifi
	 */
	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}

	/**
	 * Checks if bluetooth is ON or OFF..
	 * 
	 * @return true, if is bluetooth
	 */
	public boolean isBluetooth() {
		return bluetooth;
	}

	/**
	 * Sets the bluetooth.
	 * 
	 * @param bluetooth
	 *            the new bluetooth
	 */
	public void setBluetooth(boolean bluetooth) {
		this.bluetooth = bluetooth;
	}

	/**
	 * Checks if mobile data is ON or OFF..
	 * 
	 * @return true, if is mobile data
	 */
	public boolean isMobileData() {
		return mobileData;
	}

	/**
	 * Sets the mobile data.
	 * 
	 * @param mobileData
	 *            the new mobile data
	 */
	public void setMobileData(boolean mobileData) {
		this.mobileData = mobileData;
	}

	/**
	 * Gets the sound level.
	 * 
	 * @return the sound level
	 */
	public int getSoundLevel() {
		return soundLevel;
	}

	/**
	 * Sets the sound level.
	 * 
	 * @param soundLevel
	 *            the new sound level
	 */
	public void setSoundLevel(int soundLevel) {
		this.soundLevel = soundLevel;
	}
}
