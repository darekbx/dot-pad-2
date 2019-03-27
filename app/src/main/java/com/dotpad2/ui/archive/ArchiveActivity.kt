package com.dotpad2.ui.archive

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dotpad2.DotPadApplication
import com.dotpad2.R
import com.dotpad2.model.Dot
import com.dotpad2.viewmodels.DotViewModel
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArchiveActivity : AppCompatActivity() {

    companion object {
        val MINIMUM_QUERY_LENGTH = 2
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var dotViewModel: DotViewModel

    private var adapter: ArchiveAdapter? = null
    private val dots = mutableListOf<Dot>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        (application as DotPadApplication).appComponent.inject(this)

        archive_progress.visibility = View.VISIBLE
        archive_list.layoutManager = LinearLayoutManager(this)
        archive_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        initViewModel()
        handleSearch()
    }

    private fun initViewModel() {
        dotViewModel = ViewModelProviders.of(this, viewModelFactory)[DotViewModel::class.java]
        dotViewModel.archivedDots(Int.MAX_VALUE, 0).observe(this, Observer { dots ->
            if (this.dots.size == 0) {
                this.dots.addAll(dots)
                fillDots()
            }else {
                adapter?.replaceAll(dots)
            }
        })
    }

    private fun fillDots() {
        adapter = ArchiveAdapter(onItemClick).apply { addAll(dots) }
        archive_list.adapter = adapter
        archive_progress.visibility = View.GONE
    }

    private fun executeSearch(query: String) {
        val lowerCaseQuery = query.toLowerCase()
        val filteredList = mutableListOf<Dot>()

        dots.forEach { dot ->
            if (dot.text.contains(lowerCaseQuery, true)) {
                filteredList.add(dot)
            }
        }

        adapter?.replaceAll(filteredList)
    }

    private fun handleSearch() {
        query_input.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                newText
                    ?.takeIf { it.length >= MINIMUM_QUERY_LENGTH }
                    ?.let { executeSearch(it) }
                    ?: adapter?.replaceAll(dots)
                return true
            }
        })
    }

    private val onItemClick = { dot: Dot? ->
        dot?.let { showOptionsDialog(dot) }
        Unit
    }

    private fun showOptionsDialog(dot: Dot) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.options_title, dot.shortText(10)))
            .setPositiveButton(R.string.option_restore, { _, _ -> restoreDot(dot) })
            .setNegativeButton(R.string.option_delete, { _, _ -> deleteDot(dot) })
            .show()
    }

    private fun restoreDot(dot: Dot) {
        dot.isArchived = false
        GlobalScope.launch(Dispatchers.Main) {
            dotViewModel.saveDot(dot)
        }
    }

    private fun deleteDot(dot: Dot) {
        dotViewModel.deleteDot(dot)
    }
}