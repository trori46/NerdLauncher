package com.example.demo.nerdlauncher

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View


class NerdLauncherActivity : BaseFragmentActivity() {
    override fun createFragment(): Fragment {
        return NerdLauncherFragment.newInstance()
    }



}
