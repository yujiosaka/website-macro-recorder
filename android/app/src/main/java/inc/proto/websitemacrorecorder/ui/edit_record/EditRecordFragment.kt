package inc.proto.websitemacrorecorder.ui.edit_record

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.MacroEvent
import inc.proto.websitemacrorecorder.databinding.FragmentEditRecordBinding
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.util.Helper

@AndroidEntryPoint
class EditRecordFragment(
    private val applicationContext: Context,
    private val helper: Helper
) : BaseFragment() {
    companion object {
        private const val FILENAME = "recorder.js"
    }

    private val vm by viewModels<EditRecordViewModel>()
    private lateinit var binding: FragmentEditRecordBinding

    private var loading = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        vm.resetEvents()

        binding = FragmentEditRecordBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_record, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_replay -> replay()
            R.id.action_done -> confirm()
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setWebView()
        startRecording()
    }

    @JavascriptInterface
    fun onClick(xPath: String, targetType: String, value: String) {
        vm.pushEvent(MacroEvent(name = "click", xPath = xPath, targetType = targetType, value = value))

        val text = if (value != "") {
            resources.getString(R.string.notification_click_value, getTargetTypeString(targetType), value)
        } else {
            resources.getString(R.string.notification_click, getTargetTypeString(targetType))
        }
        notify(text)
    }

    @JavascriptInterface
    fun onType(xPath: String, targetType: String, value: String) {
        vm.pushEvent(MacroEvent(name = "type", xPath = xPath, targetType = targetType, value = value))

        notify(resources.getString(R.string.notification_type_value, getTargetTypeString(targetType), value))
    }

    @JavascriptInterface
    fun onSelect(xPath: String, targetType: String, value: String) {
        vm.pushEvent(MacroEvent(name = "select", xPath = xPath, targetType = targetType, value = value))

        notify(resources.getString(R.string.notification_select_value, getTargetTypeString(targetType), value))
    }

    private fun setWebView() {
        binding.webRecorder.settings.allowFileAccess = false
        binding.webRecorder.settings.domStorageEnabled = true
        binding.webRecorder.settings.javaScriptEnabled = true
        binding.webRecorder.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webRecorder.addJavascriptInterface(this, "WebsiteMacroRecorder")
        binding.webRecorder.webViewClient = object : WebViewClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                view.loadUrl(request.url.toString(), mapOf(
                    "Accept-Language" to vm.macro.value!!.acceptLanguage)
                )
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url, mapOf(
                    "Accept-Language" to vm.macro.value!!.acceptLanguage)
                )
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (view.progress != 100) return

                if (!loading) {
                    vm.pushEvent(MacroEvent(name = "page", value = url))

                    notify(resources.getString(R.string.notification_wait_for_navigation, url))
                }

                finishLoading()

                if (vm.macro.value!!.name == "" && binding.webRecorder.title != "") {
                    vm.macro.value!!.name = binding.webRecorder.title
                }
                val stream = applicationContext.assets.open(FILENAME)
                val text = helper.inputStreamToString(stream)
                binding.webRecorder.evaluateJavascript(text, null)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
                handler.proceed()
            }
        }
    }

    private fun startRecording() {
        loading = true
        helper.clearStorage()
        binding.webRecorder.clearCache(true)
        binding.webRecorder.clearHistory()
        binding.webRecorder.loadUrl(vm.macro.value!!.url, mapOf(
            "Accept-Language" to vm.macro.value!!.acceptLanguage)
        )
    }

    private fun finishLoading() {
        binding.webRecorder.visibility = View.VISIBLE
        binding.shimmerLayout.visibility = View.GONE
        binding.shimmerLayout.stopShimmer()
        loading = false
    }

    private fun replay(): Boolean {
        vm.resetEvents()
        startRecording()
        notify(resources.getString(R.string.notification_restart_recording))

        return true
    }

    private fun confirm(): Boolean {
        if (loading) return false

        vm.macro.value!!.userAgent = binding.webRecorder.settings.userAgentString
        vm.macro.value!!.viewportHeight = binding.webRecorder.height
        vm.macro.value!!.viewportWidth = binding.webRecorder.width

        findNavController().navigate(
            EditRecordFragmentDirections.actionEditRecordFragmentToEditEventsFragment(vm.macro.value!!)
        )

        return true
    }

    private fun getTargetTypeString(targetType: String): String {
        val resId = when (targetType) {
            "image" -> R.string.text_target_type_image
            "link" -> R.string.text_target_type_link
            "frame" -> R.string.text_target_type_frame
            "password" -> R.string.text_target_type_password
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
