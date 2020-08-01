package inc.proto.websitemacrorecorder.ui.edit_record

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.databinding.FragmentEditRecordBinding
import inc.proto.websitemacrorecorder.util.Helper

class EditRecordFragment : Fragment() {
    private val vm: EditRecordViewModel by lazy {
        val factory = EditRecordViewModelFactory(args.macro)
        ViewModelProvider(this, factory).get(EditRecordViewModel::class.java)
    }
    private val args: EditRecordFragmentArgs by navArgs()
    private val webStorage = WebStorage.getInstance()
    private val cookieManager = CookieManager.getInstance()
    private var loading = true
    private lateinit var binding: FragmentEditRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        vm.resetEvents()
        binding = FragmentEditRecordBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_record, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_replay -> {
                vm.resetEvents()
                startRecording()
                if (activity == null) return false
                val root: View = requireActivity().findViewById(R.id.root)
                val text = root.resources.getString(R.string.notification_restart_recording)
                Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
                true
            }
            R.id.action_done -> {
                if (loading) return false
                vm.macro.value!!.userAgent = binding.webRecorder.settings.userAgentString
                vm.macro.value!!.height = binding.webRecorder.height
                vm.macro.value!!.width = binding.webRecorder.width
                findNavController().navigate(EditRecordFragmentDirections.actionEditRecordFragmentToEditEventsFragment(vm.macro.value!!))
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
        bindViewModel()
    }

    private fun bindViewModel() {
        binding.webRecorder.settings.allowFileAccess = false
        binding.webRecorder.settings.domStorageEnabled = true
        binding.webRecorder.settings.javaScriptEnabled = true
        binding.webRecorder.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webRecorder.addJavascriptInterface(this, "WebsiteMacroRecorder")
        binding.webRecorder.webViewClient = object : WebViewClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                view.loadUrl(request.url.toString(), mapOf("Accept-Language" to vm.macro.value!!.acceptLanguage))
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url, mapOf("Accept-Language" to vm.macro.value!!.acceptLanguage))
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (view.progress != 100) return
                if (!loading) {
                    vm.pushEvent(MacroEvent(name = "navigation", value = url))
                }
                finishLoading()
                if (vm.macro.value!!.name == "" && binding.webRecorder.title != "") {
                    vm.macro.value!!.name = binding.webRecorder.title
                }
                if (activity == null) return
                val stream = requireActivity().assets.open("recorder.js")
                val text = Helper.inputStreamToString(stream)
                binding.webRecorder.evaluateJavascript(text, null)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
                handler.proceed()
            }
        }
        startRecording()
    }

    private fun startRecording() {
        loading = true
        webStorage.deleteAllData()
        clearCookies(activity)
        binding.webRecorder.clearCache(true)
        binding.webRecorder.clearHistory()
        binding.webRecorder.loadUrl(vm.macro.value!!.url, mapOf("Accept-Language" to vm.macro.value!!.acceptLanguage))
    }

    private fun finishLoading() {
        binding.webRecorder.visibility = View.VISIBLE
        binding.shimmerLayout.visibility = View.GONE
        binding.shimmerLayout.stopShimmer()
        loading = false
    }

    @JavascriptInterface
    fun onClick(xPath: String, targetType: String, value: String) {
        vm.pushEvent(MacroEvent(name = "click", xPath = xPath, targetType = targetType, value = value))
        if (activity == null) return
        val root: View = requireActivity().findViewById(R.id.root)
        val text = if (value != "") {
            root.resources.getString(R.string.notification_click_value, getTargetTypeString(targetType), value)
        } else {
            root.resources.getString(R.string.notification_click, getTargetTypeString(targetType))
        }
        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun onType(xPath: String, targetType: String, value: String) {
        vm.pushEvent(MacroEvent(name = "type", xPath = xPath, targetType = targetType, value = value))
        if (activity == null) return
        val root: View = requireActivity().findViewById(R.id.root)
        val text = root.resources.getString(R.string.notification_type_value, getTargetTypeString(targetType), value)
        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun onSelect(xPath: String, targetType: String, value: String) {
        vm.pushEvent(MacroEvent(name = "select", xPath = xPath, targetType = targetType, value = value))
        if (activity == null) return
        val root: View = requireActivity().findViewById(R.id.root)
        val text = root.resources.getString(R.string.notification_select_value, getTargetTypeString(targetType), value)
        Snackbar.make(root, text, Snackbar.LENGTH_SHORT).show()
    }

    @Suppress("DEPRECATION")
    private fun clearCookies(context: Context?) {
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

    private fun getTargetTypeString(targetType: String): String {
        val resId = when (targetType) {
            "image" -> R.string.text_target_type_image
            "link" -> R.string.text_target_type_link
            "frame" -> R.string.text_target_type_frame
            "input" -> R.string.text_target_type_input
            "checkbox" -> R.string.text_target_type_checkbox
            "radio" -> R.string.text_target_type_radio
            "button" -> R.string.text_target_type_button
            "select" -> R.string.text_target_type_select
            "text" -> R.string.text_target_type_text
            else -> R.string.text_target_type_text
        }
        return resources.getString(resId)
    }
}
