package com.franciscovm.todolist

import android.os.Bundle
import android.text.Layout.Directions
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgument
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.franciscovm.todolist.data.Item
import com.franciscovm.todolist.databinding.FragmentHomeBinding
import com.franciscovm.todolist.databinding.ItemListItemBinding
import com.franciscovm.todolist.ui.ItemAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private val viewModel: InventoryViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private var items = mutableListOf<Item>()
    private val binding get() = _binding!!
    private lateinit var item:Item
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemAdapter{
            val args = HomeFragmentDirections.actionFirstFragmentToSecondFragment(it.date)
            findNavController().navigate(args)
        }

        viewModel.lista.observe(this.viewLifecycleOwner){
            binding.homeNote.isVisible = it.isEmpty()
            adapter.submitList(it)
        }
        binding.apply {
            if (items.isNotEmpty()) {
                homeText.text = items[1].note
            }
            homeRecyclerview.adapter = adapter
            homeRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
           fab.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
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
                deleteItem(item.path)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.homeRecyclerview)
    }

    private fun deleteItem(path:String) {
        Firebase.firestore.document("user/$path").delete().addOnSuccessListener {
            Toast.makeText(context,"Delete",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}