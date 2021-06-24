package com.assignment.brewapps.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.brewapps.databinding.FragmentDetailBinding
import com.assignment.brewapps.pojo.KeyLinks
import com.assignment.brewapps.viewmodel.ViewModelDetail
import com.assignment.brewapps.viewmodel.ViewModelFactory

private const val TAG = "DetailFragment"
class DetailFragment : Fragment() {

    private var _binding:FragmentDetailBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModelDetailFragment: ViewModelDetail
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelDetailFragment=activity?.run {
            ViewModelProvider(viewModelStore, ViewModelFactory(this))[ViewModelDetail::class.java]
        }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId=DetailFragmentArgs.fromBundle(requireArguments()).id
        viewModelDetailFragment.setMovieId(movieId)

        binding.recylerViewYoutube.apply {
            layoutManager=LinearLayoutManager(context)
            adapter= DetailFragmentAdapter{

            }
            hasFixedSize()
        }

        viewModelDetailFragment.getMovies.observe(viewLifecycleOwner, Observer {
            if (it.linksToVideo == null) {
                viewModelDetailFragment.fetchFromNetwork(movieId)
            } else {
                val mutableList = mutableListOf<KeyLinks>()
                it.linksToVideo.split(":").forEach {
                    mutableList.add(KeyLinks(it))
                }
                Log.d(TAG, "onViewCreated 2: $mutableList")
                Log.d(TAG, "onViewCreated: 3"+mutableList[1].key)
                (binding.recylerViewYoutube.adapter as DetailFragmentAdapter).submitList(mutableList)
            }
        })
    }


}