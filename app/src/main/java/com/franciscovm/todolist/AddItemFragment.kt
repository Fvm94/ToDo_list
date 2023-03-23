package com.franciscovm.todolist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageAndVideo.equals
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.franciscovm.todolist.data.Item
import com.franciscovm.todolist.databinding.AdditemFragmentBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddItemFragment : Fragment() {

    private var _binding: AdditemFragmentBinding? = null
    private val viewModel: InventoryViewModel by activityViewModels()
    lateinit var item: Item
    private val navigationArgs : AddItemFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = AdditemFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard()

        if (navigationArgs.itemDate != "" ){
            Toast.makeText(context,"date = ${navigationArgs.itemDate}",Toast.LENGTH_SHORT).show()
        }

        binding.outlinedButton.setOnClickListener {
            addNewItem()
        }
        binding.addText.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId){
                EditorInfo.IME_ACTION_GO->{
                    addNewItem()
                    true
                }
                else -> false
            }
        }


    }

    private fun isEntryValid():Boolean{
        return viewModel.isEntryValid(binding.addText.text.toString())
    }

    fun addNewItem() {
        if (isEntryValid()){
            viewModel.saveOnFirebase(
                binding.addText.text.toString(),
                requireContext()
            )
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun showKeyboard() {
        binding.addText.requestFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.addText, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}