package com.example.rferreira.testebrq

import android.app.TabActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.TabHost
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.TextView
import android.R.attr.host
import android.view.View
import android.widget.TabWidget
import android.R.attr.host






class MainActivity :  TabActivity()  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val tabHost = findViewById<TabHost>(android.R.id.tabhost)
        if (tabHost != null) {
            tabHost.getTabWidget().setStripEnabled(false);
            addTab(tabHost, "Investimentos", "Investimentos", TabInvestimentosActivity::class.java)
            addTab(tabHost, "Contatos", "Contatos", TabContatoActivity::class.java)
            tabHost.currentTab = 0
            val widget = tabHost.getTabWidget()
            for (i in 0 until widget.getChildCount()) {
                val v = widget.getChildAt(i)
                val tv = v.findViewById<TextView>(android.R.id.title) as TextView ?: continue
                v.setBackgroundResource(R.drawable.tab_selector)
                tv.setTextColor(Color.parseColor("#ffffff"));
            }

        }

    }

    private fun addTab(tabHost: TabHost, name: String, indicator: String, className: Class<*>) {
        val tabSpec = tabHost.newTabSpec(name)
        tabSpec.setIndicator(indicator)
        val intent  = Intent(this, className)
        tabSpec.setContent(intent)
        tabHost.addTab(tabSpec)
    }
}
