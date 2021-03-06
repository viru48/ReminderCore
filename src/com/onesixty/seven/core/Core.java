package com.onesixty.seven.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.onesixty.seven.core.intefaces.ICore;
import com.onesixty.seven.core.intefaces.ILocation;
import com.onesixty.seven.core.intefaces.ILocation.LocationType;
import com.onesixty.seven.core.intefaces.IPlatform;
import com.onesixty.seven.core.intefaces.IStorage;
import com.onesixty.seven.core.objects.Notification;
import com.onesixty.seven.core.objects.PhoneSetting;
import com.onesixty.seven.core.objects.Reminder;

/**
 * This class is the only entry point for the platform to the core. The core is
 * responsible for keeping track of the saved locations related to the reminders
 * and firing off events when the current location enters or exits the saved
 * location. It does so by communicating with <code>ReminderManager</code>.
 * 
 * @author Anupam
 * 
 */
public class Core implements ICore {

	/** The saved locations. */
	private Map<Long, ILocation> savedLocations;

	/** The saved times. */
	private Map<Long, Long> savedTimes;

	/** The listener map. */
	private Map<ICore.Event, List<IListener>> listenerMap;

	/** The reminder manager. */
	private IStorage storage;

	/** The platform. */
	private IPlatform platform;

	/**
	 * Instantiates a new core.
	 * 
	 * @param platformStorage
	 *            the platform storage
	 */
	public Core(IPlatform platform) {
		this.savedLocations = new HashMap<Long, ILocation>();
		this.savedTimes = new HashMap<Long, Long>();
		this.listenerMap = new HashMap<ICore.Event, List<IListener>>();
		this.platform = platform;		
		this.storage = platform.getPlatformStorage();

		List<Notification> allNotifications = this.storage.getAllNotifications();
		for (Notification notification : allNotifications) {
			switch (notification.getType()) {
			case LOCATION_BASED:
				this.savedLocations.put(notification.getId(), notification.getLocation());
				break;
			case TIME_BASED:
				this.savedTimes.put(notification.getId(), notification.getTime());
			default:
				break;
			}
		}
	}

	@Override
	public void setCurrentLocation(ILocation newLocation) {

		Iterator<Long> it = savedLocations.keySet().iterator();
		Set<Long> idsToRemove = new HashSet<Long>();
		while (it.hasNext()) {
			long id = it.next();
			ILocation location = savedLocations.get(id);
			float distance = location.distanceTo(newLocation);

			if (distance <= location.getRadius()) {
				if (location.getLocationType().equals(LocationType.ENTER_LOCATION)) {
					idsToRemove.add(id);
					broadcastEvent(ICore.Event.EVENT_ENTER_LOCATION_RADIUS, getNotification(id));
				} else {

					Notification notification = getNotification(id);
					notification.setExitFlag(true);
					modifyNotification(id, notification);
				}
			} else {
				Boolean exitFlag = getNotification(id).getExitFlag();
				if (exitFlag != null && exitFlag) {
					idsToRemove.add(id);
					Notification notification = getNotification(id);
					notification.setExitFlag(null);
					modifyNotification(id, notification);
					broadcastEvent(ICore.Event.EVENT_EXIT_LOCATION_RADIUS, notification);
				}
			}
		}

		savedLocations.keySet().removeAll(idsToRemove);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.onesixty.seven.core.intefaces.ICore#addListener(com.onesixty.seven
	 * .core.intefaces.ICore.Event,
	 * com.onesixty.seven.core.intefaces.ICore.IListener)
	 */
	@Override
	public void addListener(Event type, IListener listener) {
		List<IListener> listeners = listenerMap.get(type);
		if (listeners == null) {
			listeners = new ArrayList<IListener>();
		}
		listeners.add(listener);
		listenerMap.put(type, listeners);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.onesixty.seven.core.intefaces.ICore#removeListener(com.onesixty.seven
	 * .core.intefaces.ICore.Event,
	 * com.onesixty.seven.core.intefaces.ICore.IListener)
	 */
	@Override
	public void removeListener(Event type, IListener listener) {
		List<IListener> listeners = listenerMap.get(type);
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.onesixty.seven.core.intefaces.ICore#broadcastEvent(com.onesixty.seven
	 * .core.intefaces.ICore.Event, java.lang.Object)
	 */
	@Override
	public void broadcastEvent(Event type, Object data) {

		List<IListener> listeners = listenerMap.get(Event.EVENT_ALL);
		if (listeners != null) {
			for (IListener listener : listeners) {
				listener.onCoreEvent(type, data);
			}
		}

		listeners = listenerMap.get(type);
		if (listeners != null) {
			for (IListener listener : listeners) {
				listener.onCoreEvent(type, data);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.onesixty.seven.core.intefaces.ICore#addNotification(com.onesixty.
	 * seven.core.objects.Notification)
	 */
	@Override
	public long addNotification(Notification item) {
		if (item.getType() == Notification.Type.LOCATION_BASED)
			this.savedLocations.put(item.getId(), item.getLocation());
		else {
			this.savedTimes.put(item.getId(), item.getTime());
		}
		return storage.addNotification(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.onesixty.seven.core.intefaces.ICore#modifyNotification(long,
	 * com.onesixty.seven.core.objects.Notification)
	 */
	@Override
	public boolean modifyNotification(long id, Notification item) {
		return storage.modifyNotification(id, item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.onesixty.seven.core.intefaces.ICore#deleteNotification(long)
	 */
	@Override
	public boolean deleteNotification(long id) {
		Notification item = storage.getNotification(id);
		if (item != null) {
			if (item.getType().equals(Notification.Type.LOCATION_BASED)) {
				this.savedLocations.remove(id);
			} else {
				savedTimes.remove(id);
			}
		}

		return storage.deleteNotification(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.onesixty.seven.core.intefaces.ICore#getNotification(long)
	 */
	@Override
	public Notification getNotification(long id) {
		return storage.getNotification(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.onesixty.seven.core.intefaces.ICore#getAllNotifications()
	 */
	@Override
	public List<Notification> getAllNotifications() {
		return storage.getAllNotifications();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.onesixty.seven.core.intefaces.ICore#getAllReminderNotifications()
	 */
	@Override
	public List<Reminder> getAllReminderNotifications() {
		return storage.getAllReminderNotifications();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.onesixty.seven.core.intefaces.ICore#getAllPhoneSettingNotifications()
	 */
	@Override
	public List<PhoneSetting> getAllPhoneSettingNotifications() {
		return storage.getAllPhoneSettingNotifications();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.onesixty.seven.core.intefaces.ICore#containsNotification(long)
	 */
	@Override
	public boolean containsNotification(long id) {
		return storage.containsNotification(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.onesixty.seven.core.intefaces.ICore#notifyAlarm()
	 */
	@Override
	public void notifyAlarm(long alarmTime) {
		Iterator<Long> it = savedTimes.keySet().iterator();
		Set<Long> idsToRemove = new HashSet<Long>();
		while (it.hasNext()) {
			long id = it.next();
			long savedTime = savedTimes.get(id);
			if (savedTime == alarmTime) {
				idsToRemove.add(id);
				broadcastEvent(ICore.Event.EVENT_TIME_REMINDER, getNotification(id));
			}
		}

		this.savedTimes.keySet().removeAll(idsToRemove);
	}
}
