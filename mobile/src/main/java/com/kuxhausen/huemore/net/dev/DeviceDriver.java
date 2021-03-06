package com.kuxhausen.huemore.net.dev;

import android.content.Context;

public interface DeviceDriver {

  public abstract boolean initialize(Context c, DeviceListener listener);

  /**
   * @return list of (bulb id, connectivity)
   */
  public abstract ConnectivityMessage getBulbConnectivity();

  /**
   * @return list of (connection id, connectivity)
   */
  public abstract ConnectivityMessage getConnectionConnectivity();

  public abstract void targetStateChanged(StateMessage targetState);

  public abstract void bulbNameChanged(BulbNameMessage targetName);

  public abstract StateMessage getState();

  public abstract void launchOnboarding();
}
