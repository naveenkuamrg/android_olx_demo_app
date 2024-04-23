package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.NotificationAdapter
import com.application.databinding.FragmentNotificationBinding
import com.application.helper.Utility
import com.application.model.NotificationType
import com.application.viewmodels.NotificationViewModel
import kotlinx.coroutines.launch

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    lateinit var binding: FragmentNotificationBinding

    private val notificationViewModel: NotificationViewModel by viewModels { NotificationViewModel.FACTORY }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationBinding.bind(view)
        setToolbar()
        setUpRecyclerView()
        setObserve()
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setUpRecyclerView() {
        val notificationRecyclerView = binding.notificationRecyclerView
        notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        notificationRecyclerView.adapter = NotificationAdapter { id, type ->
            notificationViewModel.updateNotificationIsReadStatus(id)
            showDetailFragment(id, type)
        }
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        requireContext().getDrawable(
            R.drawable.recycleview_divider
        )?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }
        notificationRecyclerView.addItemDecoration(dividerItemDecoration)
    }


    private fun setObserve() {
        notificationViewModel.notifications.observe(viewLifecycleOwner) {
            lifecycleScope.launch {

                (binding.notificationRecyclerView.adapter as NotificationAdapter).submitData(it)
            }
        }

    }

    private fun showDetailFragment(notificationId: Long, type: NotificationType) {
        parentFragmentManager.beginTransaction().apply {
            addToBackStack("showProductDetailFragment")
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in_pop,
                R.anim.slide_out_pop
            )
            replace(R.id.main_view_container, ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(
                        "notificationId",
                        notificationId
                    )
                    if (type == NotificationType.PRODUCT
                    ) {
                        putBoolean("isCurrentUserProduct", false)
                    } else {
                        putBoolean("isCurrentUserProduct", true)
                    }
                }
            })
            commit()
        }
    }
}