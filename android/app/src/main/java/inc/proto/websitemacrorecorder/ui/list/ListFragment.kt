package inc.proto.websitemacrorecorder.ui.list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctionsException
import dagger.hilt.android.AndroidEntryPoint
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.data.MacroHistory
import inc.proto.websitemacrorecorder.databinding.FragmentListBinding
import inc.proto.websitemacrorecorder.factory.AppAdapterFactory
import inc.proto.websitemacrorecorder.factory.ListAdapterArgs
import inc.proto.websitemacrorecorder.ui.BaseFragment
import inc.proto.websitemacrorecorder.util.Helper
import inc.proto.websitemacrorecorder.ui.ext.setOnSingleClickListener
import inc.proto.websitemacrorecorder.util.TextFormatter

@AndroidEntryPoint
class ListFragment(
    private val applicationContext: Context,
    private val appAdapterFactory: AppAdapterFactory,
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth,
    private val textFormatter: TextFormatter,
    private val helper: Helper
) : BaseFragment(), ListAdapter.Listener {
    private val vm by viewModels<ListViewModel>()
    private val adapter by lazy {
        val options = buildOptions(vm.getAllMacros(firebaseAuth.currentUser!!.uid))
        val args = ListAdapterArgs(options, this)
        appAdapterFactory.instantiate(ListAdapter::class.java.name, args) as ListAdapter
    }
    private lateinit var binding: FragmentListBinding

    private var loading = false

    override fun onEditMacro(macro: Macro) {
        if (loading) return

        findNavController().navigate(
            ListFragmentDirections.actionListFragmentToEditFragment(macro)
        )
    }

    override fun onViewHistories(macro: Macro) {
        if (loading) return

        findNavController().navigate(
            ListFragmentDirections.actionListFragmentToViewHistoriesFragment(macro)
        )
    }

    override fun onOpenWebsite(macro: Macro) {
        if (loading) return

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(macro.url))
        startActivity(browserIntent)
    }

    override fun onExecuteMacro(macro: Macro) {
        if (loading || activity == null) return

        loading = true
        binding.progress.visibility = View.VISIBLE

        vm.executeMacro(macro.id).addOnCompleteListener(requireActivity()) {
            binding.progress.visibility = View.GONE
            loading = false

            if (!it.isSuccessful) {
                val exception = it.exception as FirebaseFunctionsException
                notify(textFormatter.firebaseFunctionExceptionToMessage(exception))
                return@addOnCompleteListener
            }

            val history = helper.mapToObject<MacroHistory>(it.result!!.data)

            val text = when {
                !history.isEntirePageUpdated && !history.isSelectedAreaUpdated -> resources.getString(R.string.notification_macro_succeeded)
                !history.isEntirePageUpdated -> resources.getString(R.string.notification_entire_page_changed)
                else -> resources.getString(R.string.notification_selected_area_changed)
            }
            notify(text)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentListBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (loading) return false

        return when (item.itemId) {
            R.id.action_tutorial -> goToTutorial()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewedTutorial = sharedPreferences.getBoolean("VIEWED_TUTORIAL", false)
        if (!viewedTutorial) {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToTutorial1FragmentWithoutHistory()
            )
            return
        }

        showActionBar()
        setContents()
        setListeners()
        setObservers()
    }

    private fun setContents() {
        binding.editOrder.adapter = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.text_date_array,
            R.layout.item_order
        )

        if (firebaseAuth.currentUser == null) return

        binding.recyclerMacros.adapter = adapter
        binding.recyclerMacros.layoutManager = LinearLayoutManager(activity)
    }

    private fun setListeners() {
        binding.buttonAdd.setOnSingleClickListener {
            if (loading) return@setOnSingleClickListener
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToEditUrlFragment(vm.buildMacro())
            )
        }
    }

    private fun setObservers() {
        vm.order.observe(viewLifecycleOwner, Observer { currentOrder ->
            val previousOrder = sharedPreferences.getInt("ORDER", 0)
            if (previousOrder == currentOrder) return@Observer

            sharedPreferences.edit { putInt("ORDER", currentOrder) }
            adapter.updateOptions(buildOptions(vm.getAllMacros(firebaseAuth.currentUser!!.uid)))
        })
    }

    private fun buildOptions(query: Query): FirestoreRecyclerOptions<Macro> {
        return FirestoreRecyclerOptions.Builder<Macro>()
            .setQuery(query, Macro::class.java)
            .setLifecycleOwner(this)
            .build()
    }

    private fun goToTutorial(): Boolean {
        findNavController().navigate(
            ListFragmentDirections.actionListFragmentToTutorial1Fragment()
        )
        return true
    }
}
