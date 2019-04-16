package com.example.demo.nerdlauncher

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.util.Log
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import java.util.*
import android.widget.TextView
import android.support.v4.content.ContextCompat.startActivity
import android.content.pm.ActivityInfo


class NerdLauncherFragment : Fragment() {
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false)
        mRecyclerView = v.findViewById(R.id.app_recycler_view)
        setupAdapter()
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        return v
    }

    private fun setupAdapter() {
        val startupIntent = Intent(Intent.ACTION_MAIN)
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pm = activity!!.packageManager
        val activities = pm.queryIntentActivities(startupIntent, 0)
        activities.sortWith(Comparator { a, b ->
            val pm = activity!!.packageManager
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(pm).toString(),
                b.loadLabel(pm).toString()
            )
        })
        mRecyclerView.adapter = ActivityAdapter(activities)
        Log.i(TAG, "Found " + activities.size + " activities.")
    }

    private inner class ActivityHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var mResolveInfo: ResolveInfo? = null
        private val mNameTextView: TextView = itemView as TextView

        fun bindActivity(resolveInfo: ResolveInfo) {
            mResolveInfo = resolveInfo
            val pm = activity!!.packageManager
            val appName = mResolveInfo!!.loadLabel(pm).toString()
            mNameTextView.text = appName
            mNameTextView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val activityInfo = mResolveInfo!!.activityInfo
            val i = Intent(Intent.ACTION_MAIN)
                .setClassName(
                    activityInfo.applicationInfo.packageName,
                    activityInfo.name
                )
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
    }

    private inner class ActivityAdapter(private val mActivities: List<ResolveInfo>) :
        RecyclerView.Adapter<ActivityHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return ActivityHolder(view)
        }

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val resolveInfo = mActivities[position]
            holder.bindActivity(resolveInfo)
        }

        override fun getItemCount(): Int {
            return mActivities.size
        }
    }

    companion object {
        private const val TAG = "NerdLauncherFragment"
        fun newInstance(): NerdLauncherFragment {
            return NerdLauncherFragment()
        }
    }
}