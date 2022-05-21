package com.esteban.lopez.budget.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.esteban.lopez.budget.databinding.FragmentProfileBinding

class ProfileFragment: Fragment() {

    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentProfileBinding.inflate(layoutInflater)
        return view.root
    }
}