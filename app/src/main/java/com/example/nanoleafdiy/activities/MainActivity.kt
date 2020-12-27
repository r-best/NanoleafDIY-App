package com.example.nanoleafdiy.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nanoleafdiy.R
import com.example.nanoleafdiy.activities.lightpanels.LightPanelsActivity
import com.example.nanoleafdiy.utils.*


class MainActivity : AppCompatActivity() {
    private lateinit var nsdManager: NsdManager

    private val VALID_SERVICE_NAMES: List<String> = listOf("lightpanels")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ToastManager.init(this)

        nsdManager = (getSystemService(Context.NSD_SERVICE) as NsdManager).apply {
            discoverServices("_services._dns-sd._udp", NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        }

        val pullToRefresh: SwipeRefreshLayout = findViewById(R.id.services_pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            refreshList()
            pullToRefresh.isRefreshing = false
        }
    }

    override fun onStart() {
        super.onStart()
        refreshList()
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList(){
        runOnUiThread{
            findViewById<RecyclerView>(R.id.service_list).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ServiceListAdapter()
            }
        }
    }

    private val discoveryListener = object : NsdManager.DiscoveryListener {
        // Called as soon as service discovery begins.
        override fun onDiscoveryStarted(regType: String) {
            println("Service discovery started")
        }

        override fun onServiceFound(service: NsdServiceInfo) {
            // A service was found! Do something with it.
            println("Service discovery success$service")
            if(service.serviceName !in connectedServices && service.serviceName.split('-')[0] in VALID_SERVICE_NAMES)
                nsdManager.resolveService(service, resolveListener)
        }

        override fun onServiceLost(service: NsdServiceInfo) {
            println("service lost: $service")
            connectedServices.remove(service.serviceName)
            refreshList()
        }

        override fun onDiscoveryStopped(serviceType: String) {
            println("Discovery stopped: $serviceType")
        }

        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            println("Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            println("Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }
    }

    private val resolveListener = object : NsdManager.ResolveListener {
        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Called when the resolve fails. Use the error code to debug.
            println("Resolve failed: $errorCode")
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            println("Resolve Succeeded. $serviceInfo")
            connectedServices[serviceInfo.serviceName] = ApiService(serviceInfo.serviceName, serviceInfo.host, serviceInfo.port)
            refreshList()
        }
    }
}

class ServiceListAdapter: RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        return ServiceViewHolder(ServiceListViewItem(parent.context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setPadding(0, 20, 0, 20)
            gravity = Gravity.CENTER
        })
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, i: Int) {
        holder.itemLayoutView.textView.text = connectedServices.keys.elementAt(i)
        holder.itemLayoutView.setOnClickListener { v: View -> run {
            val intent = Intent(v.context, LightPanelsActivity::class.java)
            val bundle = Bundle()
            bundle.putString("serviceName", connectedServices.keys.elementAt(i))
            intent.putExtras(bundle)
            (v.context as MainActivity).startActivity(intent)
        }}
        println(holder.itemLayoutView.image.layoutParams.height)
        holder.itemLayoutView.image.layoutParams.height = holder.itemLayoutView.textView.height
        holder.itemLayoutView.image.requestLayout()
        println(holder.itemLayoutView.image.layoutParams.height)
    }

    override fun getItemCount(): Int { return connectedServices.size }

    class ServiceViewHolder(var itemLayoutView: ServiceListViewItem) : RecyclerView.ViewHolder(itemLayoutView)
}

class ServiceListViewItem : LinearLayout {
    var image: SquareImageView
    var textView: TextView

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes){
        inflate(context, R.layout.service_listview_item, this)
        textView = findViewById(R.id.service_listviewitem_text)
        textView.textSize = 30f
        gravity = Gravity.CENTER
        image = findViewById(R.id.service_listviewitem_icon)
        image.setImageDrawable(getDrawable(resources, R.drawable.panels_icon, null))
    }
}
