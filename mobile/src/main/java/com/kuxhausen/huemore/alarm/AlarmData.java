package com.kuxhausen.huemore.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;

import com.kuxhausen.huemore.R;
import com.kuxhausen.huemore.persistence.Definitions;
import com.kuxhausen.huemore.persistence.Definitions.AlarmColumns;

import java.util.Calendar;

public class AlarmData {

  // must be kept in sync with AlarmData constructor
  public final static String[] QUERY_COLUMNS = {
      AlarmColumns._ID,
      AlarmColumns.COL_GROUP_ID,
      Definitions.GroupColumns.COL_GROUP_NAME,
      AlarmColumns.COL_MOOD_ID,
      Definitions.MoodColumns.COL_MOOD_NAME,
      AlarmColumns.COL_BRIGHTNESS,
      AlarmColumns.COL_IS_ENABLED,
      AlarmColumns.COL_REPEAT_DAYS,
      AlarmColumns.COL_YEAR,
      AlarmColumns.COL_MONTH,
      AlarmColumns.COL_DAY_OF_MONTH,
      AlarmColumns.COL_HOUR_OF_DAY,
      AlarmColumns.COL_MINUTE
  };

  private long mId = -1; //the immutable database ID, or -1 if not in database
  private long mGroupId;
  private String mGroupName; // this is only to be read by the UI, and never saved to database
  private long mMoodId;
  private String mMoodName; // this is only to be read by the UI, and never saved to database
  /**
   * null or 0 - 255
   */
  private Integer mBrightness;
  private boolean mIsEnabled;
  private DaysOfWeek mRepeatDays;
  private int mYear;
  private int mMonth;
  private int mDayOfMonth; // day of month
  private int mHourOfDay; //using 24 hour time
  private int mMinute;

  public AlarmData() {
    mRepeatDays = new DaysOfWeek();
  }

  /**
   * @param cursor already moved to the relevant row, ordered according to AlarmData.QUERY_COLUMNS
   */
  public AlarmData(Cursor cursor) {
    mId = cursor.getLong(0);

    setGroup(cursor.getLong(1), cursor.getString(2));

    setMood(cursor.getLong(3), cursor.getString(4));

    if (!cursor.isNull(5)) {
      setBrightness(cursor.getInt(5));
    }

    setEnabled(cursor.getInt(6) != 0);

    setRepeatDays(new DaysOfWeek((byte) cursor.getInt(7)));

    mYear = cursor.getInt(8);
    mMonth = cursor.getInt(9);
    mDayOfMonth = cursor.getInt(10);
    mHourOfDay = cursor.getInt(11);
    mMinute = cursor.getInt(12);
  }

  public ContentValues getValues() {
    ContentValues cv = new ContentValues();
    cv.put(AlarmColumns.COL_GROUP_ID, mGroupId);
    cv.put(AlarmColumns.COL_MOOD_ID, mMoodId);
    cv.put(AlarmColumns.COL_BRIGHTNESS, getBrightness());
    cv.put(AlarmColumns.COL_IS_ENABLED, isEnabled() ? 1 : 0);
    cv.put(AlarmColumns.COL_REPEAT_DAYS, getRepeatDays().getValue());

    Calendar calendar = getAlarmTime();
    cv.put(AlarmColumns.COL_YEAR, calendar.get(Calendar.YEAR));
    cv.put(AlarmColumns.COL_MONTH, calendar.get(Calendar.MONTH));
    cv.put(AlarmColumns.COL_DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
    cv.put(AlarmColumns.COL_HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
    cv.put(AlarmColumns.COL_MINUTE, calendar.get(Calendar.MINUTE));

    return cv;
  }

  public long getId() {
    return mId;
  }

  public void setId(long id) {
    mId = id;
  }

  public long getGroupId() {
    return mGroupId;
  }

  public String getGroupName() {
    return mGroupName;
  }

  public void setGroup(long id, String name) {
    mGroupId = id;
    mGroupName = name;
  }

  public long getMoodId() {
    return mMoodId;
  }

  public String getMoodName() {
    return mMoodName;
  }

  public void setMood(long id, String name) {
    mMoodId = id;
    mMoodName = name;
  }

  public Integer getBrightness() {
    return mBrightness;
  }

  public Integer getPercentBrightness() {
    if (mBrightness == null) {
      return null;
    }
    return (int) (mBrightness / 2.55);
  }

  public void setBrightness(Integer brightness) {
    mBrightness = brightness;
  }

  public boolean isEnabled() {
    return mIsEnabled;
  }

  public void setEnabled(boolean enabled) {
    mIsEnabled = enabled;
  }

  public DaysOfWeek getRepeatDays() {
    return mRepeatDays;
  }

  public void setRepeatDays(DaysOfWeek days) {
    if (days == null) {
      throw new IllegalArgumentException();
    }
    mRepeatDays = days;
  }

  public int getHourOfDay() {
    return mHourOfDay;
  }

  public int getMinute() {
    return mMinute;
  }

  public void setAlarmTime(Calendar calendar) {
    mYear = calendar.get(Calendar.YEAR);
    mMonth = calendar.get(Calendar.MONTH);
    mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    mHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
    mMinute = calendar.get(Calendar.MINUTE);
  }

  public Calendar getAlarmTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, mYear);
    calendar.set(Calendar.MONTH, mMonth);
    calendar.set(Calendar.DAY_OF_MONTH, mDayOfMonth);
    calendar.set(Calendar.HOUR_OF_DAY, mHourOfDay);
    calendar.set(Calendar.MINUTE, mMinute);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  public String getUserTimeString(Context c) {
    return DateFormat.getTimeFormat(c).format(getAlarmTime().getTime());
  }

  public String getSecondaryDescription(Context c) {
    String result = getGroupName() + " \u2192 " + getMoodName();

    if (!getRepeatDays().isNoDaysSet()) {
      result += "   " + repeatsToString(c, getRepeatDays());
    }
    return result;
  }

  public static String repeatsToString(Context c, DaysOfWeek repeats) {
    String result = "";
    String[] days = c.getResources().getStringArray(R.array.cap_short_repeat_days);

    if (repeats.isAllDaysSet()) {
      result = c.getResources().getString(R.string.cap_short_every_day);
    } else if (repeats.isNoDaysSet()) {
      result = c.getResources().getString(R.string.cap_short_none);
    } else {
      for (int i = 0; i < 7; i++) {
        if (repeats.isDaySet(i + 1)) {
          result += days[i] + " ";
        }
      }
    }
    return result;
  }
}
