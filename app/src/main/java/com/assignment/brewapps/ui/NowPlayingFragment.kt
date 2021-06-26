package com.assignment.brewapps.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.brewapps.R
import com.assignment.brewapps.databinding.FragmentNowPlayingBinding
import com.assignment.brewapps.pojo.ErrorCode
import com.assignment.brewapps.pojo.Status
import com.assignment.brewapps.viewmodel.ListViewModel
import com.assignment.brewapps.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

private const val TAG = "NowPlayingFragment"
class NowPlayingFragment : Fragment() {

    private var _binding:FragmentNowPlayingBinding?=null
    private val binding get() = _binding!!
    lateinit var listViewModel: ListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listViewModel=activity?.run {
            ViewModelProvider(viewModelStore, ViewModelFactory(this))[ListViewModel::class.java]
        }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentNowPlayingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listRecycler.apply {
            layoutManager= LinearLayoutManager(context)
            adapter=MovieListAdapter{
                findNavController().navigate(
                    NowPlayingFragmentDirections.actionNowPlayingFragmentToDetailFragment(
                        it
                    )
                )
            }

            isNestedScrollingEnabled = true;
            setHasFixedSize(false);
            hasFixedSize()
        }
        val swipeRight = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               listViewModel.deleteFromListAndMovieDb(viewHolder.adapterPosition)
            }

        })
        swipeRight.attachToRecyclerView(binding.listRecycler)

        listViewModel.getList.observe(viewLifecycleOwner, Observer {
            (binding.listRecycler.adapter as MovieListAdapter).submitList(it)
            if (it.isEmpty()) {
                listViewModel.fetchFromNetwork()
            }
        })

        listViewModel.searchResult.observe(viewLifecycleOwner, Observer {
            (binding.listRecycler.adapter as MovieListAdapter).submitList(it)
        })



        listViewModel.status.observe(viewLifecycleOwner, Observer { loadingStatus ->
            when (loadingStatus?.status) {
                (Status.LOADING) -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d(TAG, "onViewCreated: Status loading")
                }
                (Status.SUCCESS) -> {
                    binding.progressBar.visibility = View.GONE
                }
                (Status.ERROR) -> {
                    binding.loadingStatusText.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    showError(loadingStatus.error, loadingStatus.message)
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
            binding.swipeUp.isRefreshing = false
        })
        binding.swipeUp.setOnRefreshListener {
            Log.d(TAG, "onViewCreated: " + listViewModel.getList.value?.isEmpty())
            if (listViewModel.getList.value?.isEmpty() != true && isOnline(requireActivity())){
                listViewModel.deleteData()
            }else{
                Snackbar.make(binding.root, getString(R.string.network_error), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.ok)) {

                    }
                    .show()
                binding.swipeUp.isRefreshing=false
            }

        }


        binding.typeMovieName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG, "onTextChanged: $s  $start $before $count")
                listViewModel.setSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun showError(errorCode: ErrorCode?, message: String?) {
        Log.d(TAG, "showError: ")
        when (errorCode) {
            ErrorCode.NO_DATA -> binding.loadingStatusText.text = getString(R.string.error_no_data)
            ErrorCode.NETWORK_ERROR -> binding.loadingStatusText.text =
                getString(R.string.error_network)
            ErrorCode.UNKNOWN_ERROR -> binding.loadingStatusText.text =
                getString(R.string.error_unknown, message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}