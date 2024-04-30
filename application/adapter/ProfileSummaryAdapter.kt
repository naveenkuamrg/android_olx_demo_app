package com.application.adapter

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
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.model.Profile

class ProfileSummaryAdapter(val data: List<Profile>,val context: Context) : RecyclerView.Adapter<ProfileSummaryAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userDp = itemView.findViewById<ImageView>(R.id.user_dp)
        val userName = itemView.findViewById<TextView>(R.id.user_name)
        val phoneBtn = itemView.findViewById<ImageView>(R.id.phoneBtn)
        val profileBackGround = itemView.findViewById<FrameLayout>(R.id.profile_back_ground)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.profile_summary,
                parent, false
            )
        return ProfileViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return data.count()
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        if(data[position].profileImage != null) {
            holder.userDp.setImageBitmap(data[position].profileImage)
        }
        holder.userName.text = data[position].name
        holder.phoneBtn.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:"+data[position].phoneNumber)
                context.startActivities(arrayOf(intent))
                holder.profileBackGround.setBackgroundColor(R.color.md_theme_surfaceContainer)
            }

        }
    }
}