package com.amaurypm.videogamesrf.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.ui.adapters.ModalDialogAdapter
import com.google.android.material.tabs.TabLayout


class DialogTabBar : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_dialog, null)

        // Configurar TabLayout y ViewPager
        val tabLayout = dialogView.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = dialogView.findViewById<ViewPager>(R.id.viewPager)

        // Crear un adaptador de fragmentos para ViewPager
        val pagerAdapter = ModalDialogAdapter(childFragmentManager, listOf(
            getString(R.string.tab_title_1),
            getString(R.string.tab_title_2)
        ))
        viewPager.adapter = pagerAdapter

        // Conectar TabLayout con ViewPager
        tabLayout.setupWithViewPager(viewPager)

        builder.setView(dialogView)

        return builder.create()
    }
}
