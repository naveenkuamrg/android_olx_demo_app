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
import com.application.helper.Utility.commitWithSlideAnimation
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
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.mark_everything_as_read->{
                    notificationViewModel.updateAllNotificationIsRead()
                    return@setOnMenuItemClickListener true
                }
            }

            return@setOnMenuItemClickListener false
        }
    }

    private fun setUpRecyclerView() {
        val notificationRecyclerView = binding.notificationRecyclerView
        notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = NotificationAdapter { id->
            notificationViewModel.updateNotificationIsReadStatus(id)
            showDetailFragment(id,)
        }
        notificationRecyclerView.adapter = adapter

        adapter.addLoadStateListener {
            binding.noDataView.imageView2.setImageResource(R.drawable.ic_notifications_off)
            binding.noDataView.errorText.text = "No Notices right now"
            if (it.append.endOfPaginationReached) {
                if (adapter.itemCount < 1) {
                    binding.noDataView.noDataLayout.visibility = View.VISIBLE
                } else {
                    binding.noDataView.noDataLayout.visibility = View.GONE
                }
            }
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

    private fun showDetailFragment(notificationId: Long) {
        parentFragmentManager.commitWithSlideAnimation(
            "showProductDetailFragment",
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(
                        "notificationId", notificationId
                    )
                }
            },
            R.id.main_view_container
        )
    }
}