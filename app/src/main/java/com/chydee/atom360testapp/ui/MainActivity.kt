package com.chydee.atom360testapp.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.chydee.atom360testapp.R
import com.chydee.atom360testapp.data.dataStoreArchitecture.NetworkRepository
import com.chydee.atom360testapp.databinding.ActivityMainBinding
import com.chydee.atom360testapp.ui.viewModel.MainActivityViewModel
import com.chydee.atom360testapp.ui.viewModel.MainActivityViewModelFactory
import com.chydee.atom360testapp.utils.Atom360App
import com.chydee.atom360testapp.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName

    @Inject
    lateinit var mainViewModel: MainActivityViewModel

    @Inject
    lateinit var repo: NetworkRepository

    private lateinit var binding: ActivityMainBinding

    private val viewModelFactory: MainActivityViewModelFactory by lazy {
        MainActivityViewModelFactory(repo)
    }

    val adapter: SummaryAdapter by lazy {
        SummaryAdapter(applicationContext)
    }

    private lateinit var recyclerView: RecyclerView

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as Atom360App).appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainVM = this.mainViewModel
        binding.title.setText(R.string.atom360_covid_app)
        setSupportActionBar(binding.appBarLayout)
        progressBar = binding.progressHorizontal
        progressBar.isIndeterminate = true
        recyclerView = binding.countriesRecyclerView
        recyclerView.adapter = adapter
        getSummary()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        return true
    }

    private fun getSummary() {
        if (Atom360App.hasNetwork(applicationContext)) {
            mainViewModel.getSummary()
        } else {
            binding.progressGroup.isGone = true
            Toast.makeText(
                applicationContext,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            mainViewModel.getSummary()
        }
        loadSummary()
    }

    private fun loadSummary() {
        mainViewModel.covidSummary.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressGroup.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressGroup.isGone = true
                    adapter.setItems(it.data)
                }

                is Resource.Failure -> {
                    binding.progressGroup.isGone = true
                    Log.d(TAG, "Error!!!\n Cause: ${it.message}")
                    Toast.makeText(
                        applicationContext,
                        "Error: Cannot connect with server",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}
