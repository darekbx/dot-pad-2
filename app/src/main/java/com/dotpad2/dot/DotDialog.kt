package com.dotpad2.dot

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dotpad2.R
import com.dotpad2.DotPadApplication
import com.dotpad2.model.ColorWrapper
import com.dotpad2.model.Dot
import com.dotpad2.model.SizeWrapper
import com.dotpad2.dot.colorselect.ColorSelectorAdapter
import com.dotpad2.dot.colorselect.ColorSelectorView
import com.dotpad2.dot.sizeselect.SizeSelectorAdapter
import com.dotpad2.dot.sizeselect.SizeSelectorView
import com.dotpad2.repository.LocalPreferences
import com.dotpad2.utils.PermissionsHelper
import com.dotpad2.utils.TimeUtils
import com.dotpad2.viewmodels.DotViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

fun DotDialog.setDialogArguments(position:Point?, dot: Dot?) {
    arguments = Bundle().apply {
        dot?.id?.let {
            putLong(DotDialog.DOT_ID, it)
        }
        position?.let {
            putInt(DotDialog.DOT_POSITION_X, it.x)
            putInt(DotDialog.DOT_POSITION_Y, it.y)
        }
    }
}

class DotDialog : AppCompatDialogFragment() {

    companion object {
        val DOT_ID = "dot_id"
        val DOT_POSITION_X = "dot_position_x"
        val DOT_POSITION_Y = "dot_position_y"
    }

    @Inject
    lateinit var permissionsHelper: PermissionsHelper

    @Inject
    lateinit var localPreferences: LocalPreferences

    @Inject
    lateinit var dotReminder: DotReminder

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var dotViewModel: DotViewModel

    private var loadedDot: Dot? = null
    private var reminderTimestamp: Long? = null
    private var reminderChanged = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(context).inflate(R.layout.dialog_dot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as DotPadApplication)?.appComponent.inject(this)
        dotViewModel = ViewModelProviders.of(this, viewModelFactory)[DotViewModel::class.java]

        prepareColorSelect(view)
        prepareSizeSelect(view)

        displayDotIfExists()
        handleClickEvents()
    }

    private fun displayDotIfExists() {
        getDotId()?.let { dotId ->
            dotViewModel.loadDot(dotId).observe(this@DotDialog, Observer { dot ->
                loadedDot = dot
                udpateViews(dot)
                prepareResetButton()
            })
        }
    }

    private fun udpateViews(dot: Dot) {
        noteView.setText(dot.text)
        isStickedView.isChecked = dot.isSticked
        colorSelectorView.selectedColor = dot.color
        sizeSelectorView.selectedSize = dot.size
        dot.reminder?.run {
            reminderView.text = TimeUtils.formattedDate(this)
        }
    }

    private fun getDotId() =
        arguments
            ?.takeIf { it.containsKey(DOT_ID) }
            ?.getLong(DOT_ID)
            ?: null

    private fun getPosition() =
        arguments
            ?.takeIf { it.containsKey(DOT_POSITION_X) && it.containsKey(DOT_POSITION_Y) }
            ?.let {
                val x = it.getInt(DOT_POSITION_X)
                val y = it.getInt(DOT_POSITION_Y)
                Point(x, y)
            } ?: null

    private fun handleClickEvents() {
        view?.run {
            findViewById<View>(R.id.save_button).setOnClickListener { saveClick() }
            findViewById<View>(R.id.reminder_button).setOnClickListener { reminderClick() }
        }
    }

    private fun prepareResetButton() {
        if (loadedDot != null) {
            resetButton.setOnClickListener { resetClick() }
            resetButton.visibility = View.VISIBLE
        }
    }

    private fun canAddReminder(): Boolean {
        if (localPreferences.getEmailAddress() == null) return false
        view?.let {
            if (permissionsHelper.checkAllPermissionsGranted(it.context) == false) return false
        }
        return true
    }

    fun saveClick() {
        view?.run {
            saveTheDot()
        }
    }

    fun resetClick() {
        resetDotCreatedTime()
    }

    fun reminderClick() {
        view?.let { view ->
            if (!canAddReminder()) {
                Snackbar.make(view, R.string.cant_add_reminder, Snackbar.LENGTH_SHORT).show()
                return
            }

            if (loadedDot?.hasReminder ?: false) {
                confirmDeleteReminder(view)
            } else {
                TimeUtils.requestDateTime(view.context, {
                    reminderChanged = true
                    reminderTimestamp = it.timeInMillis
                    reminderView.text = TimeUtils.formattedDate(it.timeInMillis)
                })
            }
        }
    }

    private fun confirmDeleteReminder(view: View) {
        AlertDialog
            .Builder(view.context)
            .setNegativeButton(R.string.no, null)
            .setPositiveButton(R.string.yes, { dialog, which -> deleteReminder() })
            .setMessage(R.string.delete_reminder)
            .show()
    }

    private fun deleteReminder() {
        loadedDot?.let { loadedDot ->
            dotReminder.removeReminder(loadedDot)
            loadedDot.resetReminder()
            GlobalScope.launch(Dispatchers.Main) {
                dotViewModel.saveDot(loadedDot)
                reminderView.text = getString(R.string.dot_no_reminder)
            }
        }
    }

    private fun saveTheDot() {
        view?.run {
            val defaultColor = provideColors(context)[0]
            val defaultSize = provideSizes()[0]
            val dot = loadedDot ?: Dot()

            with (dot) {
                text = noteView.text.toString()
                size = sizeSelectorView.selectedSize ?: defaultSize
                color = colorSelectorView.selectedColor ?: defaultColor
                isSticked = isStickedView.isChecked
                position = loadedDot?.position ?: getPosition() ?: Point()
                reminder = reminderTimestamp ?: loadedDot?.reminder
            }

            GlobalScope.launch(Dispatchers.Main) {
                saveReminder(dot)
                dotViewModel.saveDot(dot)
                dismiss()
            }
        }
    }

    private fun saveReminder(dot: Dot) {
        with(dot) {
            if (reminderChanged && hasReminder) {
                val (eventId, reminderId) = dotReminder.addReminder(this)
                calendarEventId = eventId
                calendarReminderId = reminderId
            }
        }
    }

    private fun resetDotCreatedTime() {
        loadedDot?.let {
            it.createdDate = Calendar.getInstance().timeInMillis
            GlobalScope.launch(Dispatchers.Main) {
                dotViewModel.saveDot(it)
                dismiss()
            }
        }
    }

    private fun prepareSizeSelect(view: View) {
        val sizeAdapter = SizeSelectorAdapter(view.context)
        val sizeSelector = view.findViewById<SizeSelectorView>(R.id.size_selector)
        sizeSelector.adapter = sizeAdapter
        addSizes(sizeAdapter)
    }

    private fun addSizes(sizeAdapter: SizeSelectorAdapter) {
        val sizes = provideSizes().map { SizeWrapper(it) }
        sizes.first().selected = true
        sizeAdapter.addAll(sizes)
    }

    private fun prepareColorSelect(view: View) {
        val colorAdapter = ColorSelectorAdapter(view.context)
        val colorSelector = view.findViewById<ColorSelectorView>(R.id.color_selector)
        colorSelector.adapter = colorAdapter
        addColors(view.context, colorAdapter)
    }

    private fun addColors(context: Context, colorAdapter: ColorSelectorAdapter) {
        val colors = provideColors(context).map { ColorWrapper(it) }
        colors.first().selected = true
        colorAdapter.addAll(colors)
    }

    private fun provideColors(context: Context) = with(context) {
        arrayListOf(
            getColor(R.color.dotRed),
            getColor(R.color.dotTeal),
            getColor(R.color.dotBlue),
            getColor(R.color.dotPurple),
            getColor(R.color.dotOrange),
            getColor(R.color.dotYellowrange)
        )
    }

    private fun provideSizes() = arrayOf(10, 8, 6, 5)

    private val noteView by lazy { view?.findViewById(R.id.dot_note) as EditText }
    private val reminderView by lazy { view?.findViewById(R.id.dot_reminder) as TextView }
    private val isStickedView by lazy { view?.findViewById(R.id.dot_is_sticked) as CheckBox }
    private val colorSelectorView by lazy { view?.findViewById(R.id.color_selector) as ColorSelectorView }
    private val sizeSelectorView by lazy { view?.findViewById(R.id.size_selector) as SizeSelectorView }
    private val resetButton by lazy { view?.findViewById(R.id.reset_button) as View }
}