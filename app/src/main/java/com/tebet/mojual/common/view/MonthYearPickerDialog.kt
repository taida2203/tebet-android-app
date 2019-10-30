package com.tebet.mojual.common.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.tebet.mojual.R
import java.util.*

class MonthYearPickerDialog : DialogFragment() {
    internal var monthVal = -1
    internal var dayVal = -1
    internal var yearVal = -1
    private var listener: DatePickerDialog.OnDateSetListener? = null
    private var daysOfMonth = 31
    private var monthPicker: NumberPicker? = null
    private var yearPicker: NumberPicker? = null
    private var dayPicker: NumberPicker? = null
    private var select: Button? = null
    private var cancel: Button? = null
    private val cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = arguments
        if (extras != null) {
            monthVal = extras.getInt(MONTH_KEY, -1)
            dayVal = extras.getInt(DAY_KEY, -1)
            yearVal = extras.getInt(YEAR_KEY, -1)
        }
    }

    fun setListener(listener: DatePickerDialog.OnDateSetListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity!!)
        // Get the layout inflater
        val inflater = activity!!.layoutInflater

        val dialog = inflater.inflate(R.layout.month_year_picker, null)
        monthPicker = dialog.findViewById(R.id.picker_month)
        yearPicker = dialog.findViewById(R.id.picker_year)
        dayPicker = dialog.findViewById(R.id.picker_day)
        select = dialog.findViewById(R.id.btnselect)
        cancel = dialog.findViewById(R.id.btncancel)

        setDividerColor(
            monthPicker,
            ContextCompat.getColor(activity!!, android.R.color.transparent)
        )
        setDividerColor(dayPicker, ContextCompat.getColor(activity!!, android.R.color.transparent))
        setDividerColor(yearPicker, ContextCompat.getColor(activity!!, android.R.color.transparent))

        setNumberPickerTextColor(
            monthPicker!!,
            ContextCompat.getColor(activity!!, R.color.greycolor)
        )
        setNumberPickerTextColor(dayPicker!!, ContextCompat.getColor(activity!!, R.color.greycolor))
        setNumberPickerTextColor(
            yearPicker!!,
            ContextCompat.getColor(activity!!, R.color.greycolor)
        )

        monthPicker!!.minValue = 1
        monthPicker!!.maxValue = 12


        if (monthVal != -1)
        // && (monthVal > 0 && monthVal < 13))
            monthPicker!!.value = monthVal
        else
            monthPicker!!.value = cal.get(Calendar.MONTH) + 1

        monthPicker!!.displayedValues = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "June",
            "July",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )


        dayPicker!!.minValue = 1
        dayPicker!!.maxValue = daysOfMonth
        val lablesday = ArrayList<String>()
        for (i in 1 until daysOfMonth + 1) {
            lablesday.add(i.toString())
        }

        if (dayVal != -1) {
            dayPicker!!.value = dayVal
        } else {
            dayPicker!!.value = cal.get(Calendar.DAY_OF_MONTH)
        }
        //final String[][] dayArray = new String[1][1];

        monthPicker!!.setOnValueChangedListener { _, _, newVal ->
            when (newVal) {
                1, 3, 5, 7, 8, 10, 12 -> {
                    daysOfMonth = 31
                    dayPicker!!.maxValue = daysOfMonth
                    for (i in 1 until daysOfMonth + 1) {
                        lablesday.add(i.toString())
                    }
                }
                2 -> {
                    daysOfMonth = 28
                    dayPicker!!.maxValue = daysOfMonth
                    for (i in 1 until daysOfMonth + 1) {
                        lablesday.add(i.toString())
                    }
                }

                4, 6, 9, 11 -> {
                    daysOfMonth = 30
                    dayPicker!!.maxValue = daysOfMonth
                    for (i in 1 until daysOfMonth + 1) {
                        lablesday.add(i.toString())
                    }
                }
            }
        }

        val dayArray = arrayOfNulls<String>(lablesday.size)
        for (i in lablesday.indices) {
            dayArray[i] = lablesday[i]
        }

        dayPicker!!.displayedValues = dayArray
        val input = findInput(dayPicker!!)
        input!!.inputType = InputType.TYPE_CLASS_NUMBER

        val maxYear = cal.get(Calendar.YEAR)//2018
        val minYear = 1945
        val arraySize = maxYear - minYear + 1

        val tempArray = arrayOfNulls<String>(arraySize)
        tempArray[0] = yearVal.toString()
        var tempYear = yearVal + 1

        for (i in 1 until arraySize) {
            if (tempYear <= maxYear) {
                tempArray[i] = tempYear.toString()
            } else {
                tempYear = minYear
                tempArray[i] = tempYear.toString()
            }
            tempYear++
        }
        yearPicker!!.minValue = 1
        yearPicker!!.maxValue = arraySize
        yearPicker!!.displayedValues = tempArray
        val `in` = findInput(yearPicker!!)
        `in`!!.inputType = InputType.TYPE_CLASS_NUMBER

        yearPicker!!.setOnValueChangedListener { picker, _, _ ->
            try {
                if (isLeapYear(picker.value)) {
                    daysOfMonth = 29
                    dayPicker!!.maxValue = daysOfMonth
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        select!!.setOnClickListener {
            yearPicker!!.clearFocus()
            monthPicker!!.clearFocus()
            dayPicker!!.clearFocus()

            listener!!.onDateSet(
                null,
                Integer.valueOf(tempArray[yearPicker?.value ?: 0 - 1]),
                monthPicker!!.value,
                dayPicker!!.value
            )
            this@MonthYearPickerDialog.dialog?.dismiss()
        }

        cancel!!.setOnClickListener { this@MonthYearPickerDialog.dialog?.cancel() }

        builder.setView(dialog)// Add action buttons
        /*.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })*/
        return builder.create()
    }

    private fun setDividerColor(picker: NumberPicker?, color: Int) {

        val pickerFields = NumberPicker::class.java.declaredFields
        for (pf in pickerFields) {
            if (pf.name == "mSelectionDivider") {
                pf.isAccessible = true
                try {
                    val colorDrawable = ColorDrawable(color)
                    pf.set(picker, colorDrawable)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                break
            }
        }
    }

    private fun findInput(np: ViewGroup): EditText? {
        val count = np.childCount
        for (i in 0 until count) {
            val child = np.getChildAt(i)
            if (child is ViewGroup) {
                findInput(child)
            } else if (child is EditText) {
                return child
            }
        }
        return null
    }

    companion object {

        val MONTH_KEY = "monthValue"
        val DAY_KEY = "dayValue"
        val YEAR_KEY = "yearValue"

        fun newInstance(monthIndex: Int, daysIndex: Int, yearIndex: Int): MonthYearPickerDialog {
            val f = MonthYearPickerDialog()

            // Supply num input as an argument.
            val args = Bundle()
            args.putInt(MONTH_KEY, monthIndex)
            args.putInt(DAY_KEY, daysIndex)
            args.putInt(YEAR_KEY, yearIndex)
            f.arguments = args

            return f
        }

        fun isLeapYear(year: Int): Boolean {
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, year)
            return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365
        }

        fun isLeapYear2(year: Int): Boolean {
            return if (year % 4 != 0) {
                false
            } else if (year % 400 == 0) {
                true
            } else
                year % 100 != 0
        }

        fun setNumberPickerTextColor(numberPicker: NumberPicker, color: Int): Boolean {
            val count = numberPicker.childCount
            for (i in 0 until count) {
                val child = numberPicker.getChildAt(i)
                if (child is EditText) {
                    try {
                        val selectorWheelPaintField = numberPicker.javaClass
                            .getDeclaredField("mSelectorWheelPaint")
                        selectorWheelPaintField.isAccessible = true
                        (selectorWheelPaintField.get(numberPicker) as Paint).color = color
                        child.setTextColor(color)
                        numberPicker.invalidate()
                        return true
                    } catch (e: NoSuchFieldException) {
                        // Log.w("setNumberPickerTextColor", e);
                    } catch (e: IllegalAccessException) {
                        //Log.w("setNumberPickerTextColor", e);
                    } catch (e: IllegalArgumentException) {
                        //Log.w("setNumberPickerTextColor", e);
                    }

                }
            }
            return false
        }
    }
}