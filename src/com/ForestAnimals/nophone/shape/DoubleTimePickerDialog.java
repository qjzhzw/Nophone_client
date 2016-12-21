/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ForestAnimals.nophone.shape;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import com.ForestAnimals.nophone.R;

/**
 * A dialog that prompts the user for the time of day using a {@link TimePicker}.
 * <p>
 * <p>See the <a href="{@docRoot}resources/tutorials/views/hello-timepicker.html">Time Picker
 * tutorial</a>.</p>
 */

public class DoubleTimePickerDialog extends AlertDialog
        implements OnClickListener, OnTimeChangedListener {

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnTimeSetListener {

        /**
         * @param view_start      The view associated with this listener.
         * @param hourOfDay_start The hour that was set.
         * @param minute_start    The minute that was set.
         */
        void onTimeSet(TimePicker view_start,TimePicker view_finish, int hourOfDay_start, int minute_start, int hourOfDay_finish, int minute_finish);
    }

    private static final String HOUR_START = "hour";
    private static final String MINUTE_START = "minute";
    private static final String IS_24_HOUR_START = "is24hour";
    private static final String HOUR_FINISH = "hour";
    private static final String MINUTE_FINISH = "minute";
    private static final String IS_24_HOUR_FINISH = "is24hour";

    private final TimePicker timePicker_start;
    private final TimePicker timePicker_finish;

    private final OnTimeSetListener mCallback;

    int mInitialHourOfDay_start;
    int mInitialMinute_start;
    int mInitialHourOfDay_finish;
    int mInitialMinute_finish;
    boolean mIs24HourView;

    /**
     * @param context          Parent.
     * @param callBack         How parent is notified.
     * @param hourOfDay_start  The initial hour.
     * @param minute_start     The initial minute.
     * @param hourofDay_finish The initial hour.
     * @param minute_finish    The initial minute.
     * @param is24HourView     Whether this is a 24 hour view, or AM/PM.
     */
    public DoubleTimePickerDialog(Context context,
                                  OnTimeSetListener callBack,
                                  int hourOfDay_start, int minute_start, int hourofDay_finish, int minute_finish, boolean is24HourView) {
        this(context, 0, callBack, hourOfDay_start, minute_start, hourofDay_finish, minute_finish, is24HourView);
    }

    /**
     * @param context      Parent.
     * @param theme        the theme to apply to this dialog
     * @param callBack     How parent is notified.
     * @param hourOfDay_start    The initial hour.
     * @param minute_start       The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public DoubleTimePickerDialog(Context context,
                                  int theme,
                                  OnTimeSetListener callBack,
                                  int hourOfDay_start, int minute_start, int hourOfDay_finish, int minute_finish,boolean is24HourView) {
        super(context, theme);
        mCallback = callBack;
        mInitialHourOfDay_start = hourOfDay_start;
        mInitialMinute_start = minute_start;
        mInitialHourOfDay_finish = hourOfDay_finish;
        mInitialMinute_finish = minute_finish;
        mIs24HourView = is24HourView;

        setCanceledOnTouchOutside(false);
        setIcon(0);

        setButton(BUTTON_POSITIVE, context.getText(R.string.OK), this);
        setButton(BUTTON_NEGATIVE, context.getText(R.string.cancel),
                (OnClickListener) null);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.doubletimepicker_dialog, null);
        setView(view);
        timePicker_start = (TimePicker) view.findViewById(R.id.timePicker_start);
        timePicker_finish = (TimePicker) view.findViewById(R.id.timePicker_finish);

        // initialize state
        timePicker_start.setIs24HourView(mIs24HourView);
        timePicker_start.setCurrentHour(mInitialHourOfDay_start);
        timePicker_start.setCurrentMinute(mInitialMinute_start);
        timePicker_start.setOnTimeChangedListener(this);
        timePicker_finish.setIs24HourView(mIs24HourView);
        timePicker_finish.setCurrentHour(mInitialHourOfDay_finish);
        timePicker_finish.setCurrentMinute(mInitialMinute_finish);
        timePicker_finish.setOnTimeChangedListener(this);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            timePicker_start.clearFocus();
            timePicker_finish.clearFocus();
            mCallback.onTimeSet(timePicker_start,timePicker_finish, timePicker_start.getCurrentHour(),
                    timePicker_start.getCurrentMinute(),timePicker_finish.getCurrentHour(),timePicker_finish.getCurrentMinute());
        }
    }

    public void updateTime(int hourOfDay_start, int minutOfHour_start,int hourofDay_finish,int minuteOfHour_finish) {
        timePicker_start.setCurrentHour(hourOfDay_start);
        timePicker_start.setCurrentMinute(minutOfHour_start);
        timePicker_finish.setCurrentHour(hourofDay_finish);
        timePicker_finish.setCurrentMinute(minuteOfHour_finish);
    }

    public void onTimeChanged(TimePicker view, int hourOfDay,int minute) {
        /* do nothing */
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR_START, timePicker_start.getCurrentHour());
        state.putInt(MINUTE_START, timePicker_start.getCurrentMinute());
        state.putBoolean(IS_24_HOUR_START, timePicker_start.is24HourView());
        state.putInt(HOUR_FINISH, timePicker_finish.getCurrentHour());
        state.putInt(MINUTE_FINISH, timePicker_finish.getCurrentMinute());
        state.putBoolean(IS_24_HOUR_FINISH, timePicker_finish.is24HourView());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour_start = savedInstanceState.getInt(HOUR_START);
        int minute_start = savedInstanceState.getInt(MINUTE_START);
        int hour_finish = savedInstanceState.getInt(HOUR_START);
        int minute_finish = savedInstanceState.getInt(MINUTE_START);
        timePicker_start.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR_START));
        timePicker_start.setCurrentHour(hour_start);
        timePicker_start.setCurrentMinute(minute_start);
        timePicker_finish.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR_START));
        timePicker_finish.setCurrentHour(hour_finish);
        timePicker_finish.setCurrentMinute(minute_finish);
    }
}
