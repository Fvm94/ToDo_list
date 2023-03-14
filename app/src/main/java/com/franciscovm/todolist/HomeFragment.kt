package com.franciscovm.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.franciscovm.todolist.data.InventoryViewModel
import com.franciscovm.todolist.data.InventoryViewModelFactory
import com.franciscovm.todolist.data.Item
import com.franciscovm.todolist.databinding.FragmentHomeBinding
import com.franciscovm.todolist.ui.ItemAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: FragmentHomeBinding? = null
    lateinit var item: Item

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemAdapter()

        binding.homeRecyclerview.adapter = adapter
        viewModel.allItems.observe(this.viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        binding.homeRecyclerview.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                item = adapter.currentList[viewHolder.adapterPosition]
                deleteItem()
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.homeRecyclerview)


    }

   /* private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Deletar?")
            .setMessage("Você esta certo que quer deletar ${item.item}?")
            .setCancelable(false)
            .setNegativeButton("Nâo") { _, _ -> }
            .setPositiveButton("Sim") { _, _ ->
                deleteItem()
            }
            .show()
    }*/


    private fun deleteItem() {
        viewModel.deleteItem(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}