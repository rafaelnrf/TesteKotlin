package com.example.rferreira.testebrq

import android.app.TabActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.TabHost
import android.content.Intent


class MainActivity :  TabActivity()  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val tabHost = findViewById<TabHost>(android.R.id.tabhost)
        if (tabHost != null) {
            addTab(tabHost, "Investimentos", "Investimentos", TabInvestimentosActivity::class.java)
            addTab(tabHost, "Contatos", "Contatos", TabContatoActivity::class.java)
            tabHost.currentTab = 0
            tabHost.setOnTabChangedListener { tabId -> Toast.makeText(applicationContext, tabId, Toast.LENGTH_SHORT).show() }
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
