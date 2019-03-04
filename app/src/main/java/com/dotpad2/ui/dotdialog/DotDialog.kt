package com.dotpad2.ui.dotdialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dotpad2.DotPadApplication
import com.dotpad2.R
import com.dotpad2.model.ColorWrapper
import com.dotpad2.model.Dot
import com.dotpad2.model.SizeWrapper
import com.dotpad2.ui.dotdialog.colorselect.ColorSelectorAdapter
import com.dotpad2.ui.dotdialog.colorselect.ColorSelectorView
import com.dotpad2.ui.dotdialog.sizeselect.SizeSelectorAdapter
import com.dotpad2.ui.dotdialog.sizeselect.SizeSelectorView
import com.dotpad2.viewmodels.DotViewModel
import javax.inject.Inject

fun DotDialog.setDialogArguments(dot: Dot?) {
    dot?.id?.let {
        arguments = Bundle(1).apply { putLong(DotDialog.DOT_ID, it) }
    }
}

class DotDialog : AppCompatDialogFragment() {

    companion object {
        val DOT_ID = "dot_id"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var dotViewModel: DotViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(context).inflate(R.layout.dialog_dot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as DotPadApplication)?.appComponent.inject(this)
        dotViewModel = ViewModelProviders.of(this, viewModelFactory)[DotViewModel::class.java]

        prepareColorSelect(view)
        prepareSizeSelect(view)

        handleClickEvents()
        displayDotIfExists()
    }

    private fun displayDotIfExists() {
        getDotId()?.let { dotId ->
            dotViewModel.loadDot(dotId).observe(this@DotDialog, Observer { dot -> })
        }
    }

    private fun getDotId() =
        arguments
            ?.takeIf { it.containsKey(DOT_ID) }
            ?.getLong(DOT_ID)
            ?: null

    private fun handleClickEvents() {
        view?.run {
            findViewById<View>(R.id.save_button).setOnClickListener { saveClick() }
            findViewById<View>(R.id.reset_button).setOnClickListener { resetClick() }
            findViewById<View>(R.id.reminder_button).setOnClickListener { reminderClick() }
        }
    }

    fun saveClick() {
        view?.run {
            saveTheDot()
        }
    }

    fun resetClick() {

    }

    fun reminderClick() {

    }

    private fun saveTheDot() {
        view?.run {
            val message = findViewById<EditText>(R.id.dot_note).text.toString()
            val isSticked = findViewById<CheckBox>(R.id.dot_is_sticked).isChecked
            val color = findViewById<ColorSelectorView>(R.id.color_selector).getSelectedColor()
            val size = findViewById<SizeSelectorView>(R.id.size_selector).getSelectedSize()

            Log.v("----------", "Color: $color, size: $size")
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
}