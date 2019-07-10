package com.tebet.mojual.common.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.tebet.mojual.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthYearPickerDialog extends DialogFragment {

    public static final String MONTH_KEY = "monthValue";
    public static final String DAY_KEY = "dayValue";
    public static final String YEAR_KEY = "yearValue";
    int monthVal = -1, dayVal = -1, yearVal = -1;
    private DatePickerDialog.OnDateSetListener listener;
    private int daysOfMonth = 31;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private NumberPicker dayPicker;
    private Button select, cancel;
    private Calendar cal = Calendar.getInstance();

    public static MonthYearPickerDialog newInstance(int monthIndex, int daysIndex, int yearIndex) {
        MonthYearPickerDialog f = new MonthYearPickerDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(MONTH_KEY, monthIndex);
        args.putInt(DAY_KEY, daysIndex);
        args.putInt(YEAR_KEY, yearIndex);
        f.setArguments(args);

        return f;
    }

    public static boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    public static boolean isLeapYear2(int year) {
        if (year % 4 != 0) {
            return false;
        } else if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                    // Log.w("setNumberPickerTextColor", e);
                } catch (IllegalAccessException e) {
                    //Log.w("setNumberPickerTextColor", e);
                } catch (IllegalArgumentException e) {
                    //Log.w("setNumberPickerTextColor", e);
                }
            }
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        if (extras != null) {
            monthVal = extras.getInt(MONTH_KEY, -1);
            dayVal = extras.getInt(DAY_KEY, -1);
            yearVal = extras.getInt(YEAR_KEY, -1);
        }
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.month_year_picker, null);
        monthPicker = dialog.findViewById(R.id.picker_month);
        yearPicker = dialog.findViewById(R.id.picker_year);
        dayPicker = dialog.findViewById(R.id.picker_day);
        select = dialog.findViewById(R.id.btnselect);
        cancel = dialog.findViewById(R.id.btncancel);

        setDividerColor(monthPicker, ContextCompat.getColor(getActivity(), android.R.color.transparent));
        setDividerColor(dayPicker, ContextCompat.getColor(getActivity(), android.R.color.transparent));
        setDividerColor(yearPicker, ContextCompat.getColor(getActivity(), android.R.color.transparent));

        setNumberPickerTextColor(monthPicker, ContextCompat.getColor(getActivity(), R.color.greycolor));
        setNumberPickerTextColor(dayPicker, ContextCompat.getColor(getActivity(), R.color.greycolor));
        setNumberPickerTextColor(yearPicker, ContextCompat.getColor(getActivity(), R.color.greycolor));

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);


        if (monthVal != -1)// && (monthVal > 0 && monthVal < 13))
            monthPicker.setValue(monthVal);
        else
            monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

        monthPicker.setDisplayedValues(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "June", "July",
                "Aug", "Sep", "Oct", "Nov", "Dec"});


        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(daysOfMonth);
        final List<String> lablesday = new ArrayList<String>();
        for (int i = 1; i < (daysOfMonth + 1); i++) {
            lablesday.add(String.valueOf(i));
        }

        if (dayVal != -1) {
            dayPicker.setValue(dayVal);
        } else {
            dayPicker.setValue(cal.get(Calendar.DAY_OF_MONTH));
        }
        //final String[][] dayArray = new String[1][1];

        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (newVal) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        daysOfMonth = 31;
                        dayPicker.setMaxValue(daysOfMonth);
                        for (int i = 1; i < (daysOfMonth + 1); i++) {
                            lablesday.add(String.valueOf(i));
                        }
                        break;
                    case 2:
                        daysOfMonth = 28;
                        dayPicker.setMaxValue(daysOfMonth);
                        for (int i = 1; i < (daysOfMonth + 1); i++) {
                            lablesday.add(String.valueOf(i));
                        }
                        break;

                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        daysOfMonth = 30;
                        dayPicker.setMaxValue(daysOfMonth);
                        for (int i = 1; i < (daysOfMonth + 1); i++) {
                            lablesday.add(String.valueOf(i));
                        }
                        break;
                }
            }
        });

        String[] dayArray = new String[lablesday.size()];
        for (int i = 0; i < lablesday.size(); i++) {
            dayArray[i] = lablesday.get(i);
        }

        dayPicker.setDisplayedValues(dayArray);
        EditText input = findInput(dayPicker);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        int maxYear = cal.get(Calendar.YEAR);//2018
        final int minYear = 1945;
        int arraySize = maxYear - minYear + 1;

        final String[] tempArray = new String[arraySize];
        tempArray[0] = String.valueOf(yearVal);
        int tempYear = yearVal + 1;

        for (int i = 1; i < arraySize; i++) {
            if (tempYear <= maxYear) {
                tempArray[i] = String.valueOf(tempYear);
            } else {
                tempYear = minYear;
                tempArray[i] = String.valueOf(tempYear);
            }
            tempYear++;
        }
        yearPicker.setMinValue(1);
        yearPicker.setMaxValue(arraySize);
        yearPicker.setDisplayedValues(tempArray);
        EditText in = findInput(yearPicker);
        in.setInputType(InputType.TYPE_CLASS_NUMBER);

        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                try {
                    if (isLeapYear(picker.getValue())) {
                        daysOfMonth = 29;
                        dayPicker.setMaxValue(daysOfMonth);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearPicker.clearFocus();
                monthPicker.clearFocus();
                dayPicker.clearFocus();

                listener.onDateSet(null, Integer.valueOf(tempArray[yearPicker.getValue() - 1]), monthPicker.getValue(), dayPicker.getValue());
                MonthYearPickerDialog.this.getDialog().dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthYearPickerDialog.this.getDialog().cancel();
            }
        });

        builder.setView(dialog)
        // Add action buttons
                /*.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })*/;
        return builder.create();
    }

    private void setDividerColor(NumberPicker picker, int color) {

        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private EditText findInput(ViewGroup np) {
        int count = np.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = np.getChildAt(i);
            if (child instanceof ViewGroup) {
                findInput((ViewGroup) child);
            } else if (child instanceof EditText) {
                return (EditText) child;
            }
        }
        return null;
    }
}