package com.esteban.lopez.budget.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.esteban.lopez.budget.R
import com.esteban.lopez.budget.databinding.FragmentHomeBinding
import com.esteban.lopez.budget.view.fragments.dialogs.NewValueDialogFragment

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root;

        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        setupFab(binding)

        return view
    }

    private fun setupFab(binding: FragmentHomeBinding) {
        binding.fab.setOnClickListener {
            NewValueDialogFragment.newInstance().show(childFragmentManager,"NewValueDialogFragment")
        }
    }


}