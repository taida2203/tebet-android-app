package co.common.view.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by heo_local on 1/31/2016.
 */
public class DateDialog extends MasbroDialog {
    private Calendar minDate;
    private Calendar maxDate;
    private Calendar defaultCalendar;

    DatePickerDialog.OnDateSetListener onDateSetListener;
    DatePickerDialog datePickerDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (datePickerDialog == null) {
            Calendar minDateTime = defaultCalendar;
            if (maxDate != null && defaultCalendar != null) {
                minDateTime = Calendar.getInstance().compareTo(maxDate) < 0 ? defaultCalendar : maxDate;
            }

            int selectedYear = minDateTime != null ? minDateTime.get(Calendar.YEAR) : Calendar.getInstance().get(Calendar.YEAR);
            int selectedMonth = minDateTime != null ? minDateTime.get(Calendar.MONTH) : Calendar.getInstance().get(Calendar.MONTH);
            int selectedDayOfMonth = minDateTime != null ? minDateTime.get(Calendar.DAY_OF_MONTH) : Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            try {
                datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, (datePicker, year, month, dayOfMonth) -> {
                    if (onDateSetListener != null) {
                        onDateSetListener.onDateSet(datePicker, year, month, dayOfMonth);
                    }
                }, selectedYear, selectedMonth, selectedDayOfMonth);
            } catch (Exception e) {
                datePickerDialog = new DatePickerDialog(getActivity(), (datePicker, year, month, dayOfMonth) -> {
                    if (onDateSetListener != null) {
                        onDateSetListener.onDateSet(datePicker, year, month, dayOfMonth);
                    }
                }, selectedYear, selectedMonth, selectedDayOfMonth);
            }

            if (minDate != null) {
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            }

            if (maxDate != null) {
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            }
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return datePickerDialog;
    }

    public DateDialog setMinDate(Calendar minDate) {
        this.minDate = minDate;
        return this;
    }

    public DateDialog setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public DateDialog setDefaultDate(Calendar defaultDate) {
        this.defaultCalendar = defaultDate;
        return this;
    }

    public DateDialog setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
        return this;
    }
}
