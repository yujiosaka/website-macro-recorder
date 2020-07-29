package inc.proto.websitemacrorecorder.ui.list

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import inc.proto.websitemacrorecorder.R
import inc.proto.websitemacrorecorder.data.Macro
import inc.proto.websitemacrorecorder.databinding.FragmentListBinding
import inc.proto.websitemacrorecorder.repository.MacroRepository
import inc.proto.websitemacrorecorder.util.setOnSingleClickListener

class ListFragment : Fragment() {
    private val vm: ListViewModel by lazy {
        ViewModelProvider(this).get(ListViewModel::class.java)
    }
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val macroRepository = MacroRepository()
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
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
        return when (item.itemId) {
            R.id.action_tutorial -> {
                val action = ListFragmentDirections.actionListFragmentToTutorial1Fragment()
                findNavController().navigate(action)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showActionBar()
        val viewedTutorial = sharedPreferences.getBoolean("VIEWED_TUTORIAL", false)
        if (!viewedTutorial) {
            val action = ListFragmentDirections.actionListFragmentToTutorial1FragmentWithoutHistory()
            findNavController().navigate(action)
            return
        }
        bindViewModel()
    }

    private fun bindViewModel() {
        binding.editOrder.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.text_date_array,
            R.layout.item_order
        )
        binding.buttonAdd.setOnSingleClickListener {
            if (context == null) return@setOnSingleClickListener
            val macro = Macro()
            val action = ListFragmentDirections.actionListFragmentToEditUrlFragment(macro)
            findNavController().navigate(action)
        }
        if (firebaseAuth.currentUser == null) return
        val order = sharedPreferences.getInt("ORDER", 0)
        val adapter = ListAdapter(this, buildOptions(macroRepository.getAll(firebaseAuth.currentUser!!.uid).orderBy(order)))
        binding.recyclerMacros.adapter = adapter
        binding.recyclerMacros.layoutManager = LinearLayoutManager(activity)
        vm.order.value = order
        vm.order.observe(viewLifecycleOwner, Observer { currentOrder ->
            val previousOrder = sharedPreferences.getInt("ORDER", 0)
            if (previousOrder == currentOrder) return@Observer
            sharedPreferences.edit { putInt("ORDER", currentOrder) }
            adapter.updateOptions(buildOptions(macroRepository.getAll(firebaseAuth.currentUser!!.uid).orderBy(currentOrder)))
        })
    }

    private fun showActionBar() {
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    private fun buildOptions(query: Query): FirestoreRecyclerOptions<Macro> {
        return FirestoreRecyclerOptions.Builder<Macro>()
            .setQuery(query, Macro::class.java)
            .setLifecycleOwner(this)
            .build()
    }

    private fun Query.orderBy(order: Int): Query {
        return when (order) {
            Macro.ORDER_UPDATED_AT_DESC_VALUE -> this.orderBy("updatedAt", Query.Direction.DESCENDING)
            Macro.ORDER_UPDATED_AT_ASC_VALUE -> this.orderBy("updatedAt", Query.Direction.ASCENDING)
            Macro.ORDER_CREATED_AT_DESC_VALUE -> this.orderBy("createdAt", Query.Direction.DESCENDING)
            Macro.ORDER_CREATED_AT_ASC_VALUE -> this.orderBy("createdAt", Query.Direction.ASCENDING)
            else -> throw IllegalArgumentException("Unknown order! $order")
        }
    }
}
