package com.application.adapter

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.model.Profile
import com.application.model.ProfileSummary
import com.application.repositories.impl.ProfileImageRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileSummaryAdapter(
    val context: Context,
    val onItemClickListener: (profileId: Long) -> Unit
) :
    RecyclerView.Adapter<ProfileSummaryAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userDp = itemView.findViewById<ImageView>(R.id.user_dp)
        val userName = itemView.findViewById<TextView>(R.id.user_name)
        val phoneBtn = itemView.findViewById<Button>(R.id.phoneBtn)
        val profileBackGround =
            itemView.findViewById<FrameLayout>(R.id.profile_back_ground)
    }

    fun setData(data: List<ProfileSummary>) {
        asyncDiffUtil.submitList(data)
    }

    private val diffUtil =
        object : DiffUtil.ItemCallback<ProfileSummary>() {
            override fun areItemsTheSame(
                oldItem: ProfileSummary,
                newItem: ProfileSummary
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProfileSummary,
                newItem: ProfileSummary
            ): Boolean {
                return oldItem == newItem
            }

        }
    private val asyncDiffUtil = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.profile_summary,
                parent, false
            )
        return ProfileViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return asyncDiffUtil.currentList.count()
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val data = asyncDiffUtil.currentList[position]
        if (data.profileImage != null) {
            data.profileImage?.prepareToDraw()
            holder.userDp.setImageBitmap(data.profileImage)
        }
        holder.userName.text = data.name
        if (data.isContented) {
            holder.profileBackGround.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.md_theme_surfaceContainer
                )
            )
        }
        holder.phoneBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onItemClickListener(data.id)
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:" + data.phoneNumber)
                context.startActivities(arrayOf(intent))
                holder.profileBackGround.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.md_theme_surfaceContainer
                    )
                )
            } else {
                AlertDialog.Builder(context).apply {
                    setMessage(
                        "Please enable call permissions to make  calls.Tap Setting -> " +
                                "Permission -> Phone -> Allow"
                    )
                    setPositiveButton("ok", null)
                    show()
                }
            }

        }
    }
}