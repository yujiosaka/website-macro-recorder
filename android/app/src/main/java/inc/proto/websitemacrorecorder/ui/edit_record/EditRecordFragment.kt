package inc.proto.websitemacrorecorder.ui.edit_record

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Event
import inc.proto.websitemacrorecorder.databinding.FragmentEditRecordBinding
import inc.proto.websitemacrorecorder.util.Helper


class EditRecordFragment : Fragment() {

    private val _args: EditRecordFragmentArgs by navArgs()

    private lateinit var _binding: FragmentEditRecordBinding
    private lateinit var _vm: EditRecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = EditRecordViewModelFactory(_args.macro)
        setHasOptionsMenu(true);
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_record, container, false)
        _vm = ViewModelProviders.of(this, factory).get(EditRecordViewModel::class.java)
        _vm.resetEvents()
        _binding.vm = _vm
        _binding.lifecycleOwner = this
        return _binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_record, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_replay -> {
                _vm.resetEvents()
                _startRecording()
                true
            }
            R.id.action_done -> {
                val action = EditRecordFragmentDirections.actionEditRecordFragmentToEditEventsFragment(_vm.macro)
                findNavController().navigate(action)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.webRecorder.settings.setAllowFileAccess(false)
        _binding.webRecorder.settings.setDomStorageEnabled(true)
        _binding.webRecorder.settings.setJavaScriptEnabled(true)
        _binding.webRecorder.settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        _binding.webRecorder.settings.setUserAgentString(_vm.userAgent)
        _binding.webRecorder.addJavascriptInterface(this, "WebsiteMacroRecorder")

        val customHeaders: Map<String, String> = mapOf("Accept-Language" to _vm.acceptLanguage)
        _binding.webRecorder.webViewClient = object : WebViewClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                view.loadUrl(request.url.toString(), customHeaders)
                return true
            }
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url, customHeaders)
                return true
            }
            override fun onPageFinished(view: WebView, url: String) {
                _binding.webRecorder.visibility = View.VISIBLE
                _binding.shimmerLayout.stopShimmer()
                _binding.shimmerLayout.visibility = View.GONE
                if (activity == null) return
                val stream = activity!!.assets.open("recorder.js")
                val text = Helper.inputStreamToString(stream)
                _binding.webRecorder.evaluateJavascript(text, null)

            }
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
                handler.proceed()
            }
        }

        _startRecording()
    }

    @JavascriptInterface
    fun onClick(xPath: String, targetType: String, value: String) {
        val event = Event(name = "click", xPath = xPath, targetType = targetType, value = value)
        _vm.pushEvent(event)
        if (activity == null) return
        val root: View = activity!!.findViewById(R.id.root)
        val targetTypeString = _getTargetTypeString(targetType)
        val text = if (value != "") {
            root.resources.getString(R.string.notification_click_value, targetTypeString, value)
        } else {
            root.resources.getString(R.string.notification_click, targetTypeString)
        }
        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun onChange(xPath: String, targetType: String, value: String) {
        val event = Event(name = "change", xPath = xPath, targetType = targetType, value = value)
        _vm.pushEvent(event)
        if (activity == null) return
        val root: View = activity!!.findViewById(R.id.root)
        val targetTypeString = _getTargetTypeString(targetType)
        val text = root.resources.getString(R.string.notification_change_value, targetTypeString, value)
        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun onSelect(xPath: String, targetType: String, value: String) {
        val event = Event(name = "select", xPath = xPath, targetType = targetType, value = value)
        _vm.pushEvent(event)
        if (activity == null) return
        val root: View = activity!!.findViewById(R.id.root)
        val targetTypeString = _getTargetTypeString(targetType)
        val text = root.resources.getString(R.string.notification_select_value, targetTypeString, value)
        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
    }

    @Suppress("DEPRECATION")
    private fun _clearCookies(context: Context?) {
        val cookieManager = CookieManager.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            cookieManager.removeAllCookies(null)
            cookieManager.flush()
        } else {
            val cookieSyncManager = CookieSyncManager.createInstance(context)
            cookieSyncManager.startSync()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncManager.stopSync()
            cookieSyncManager.sync()
        }
    }

    private fun _startRecording() {
        _clearCookies(activity)
        val customHeaders: Map<String, String> = mapOf("Accept-Language" to _vm.acceptLanguage)
        _binding.webRecorder.clearCache(true);
        _binding.webRecorder.clearHistory();
        _binding.webRecorder.loadUrl(_vm.url, customHeaders)
    }

    private fun _getTargetTypeString(targetType: String): String {
        return try {
            resources.getString(
                resources.getIdentifier(
                    "text_target_type_${targetType}",
                    "string",
                    activity!!.packageName
                )
            )
        } catch (e: Resources.NotFoundException) {
            resources.getString(R.string.text_target_type_text)
        }
    }
}
