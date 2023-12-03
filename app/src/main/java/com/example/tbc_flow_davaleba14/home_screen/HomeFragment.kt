package com.example.tbc_flow_davaleba14.home_screen

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tbc_flow_davaleba14.BaseFragment
import com.example.tbc_flow_davaleba14.databinding.FragmentHomeBinding
import com.example.tbc_flow_davaleba14.databinding.ItemUpdateDialogBinding
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var itemAdapter: ItemRecyclerAdapter

    override fun setUp() {
        setUpRv()
        listeners()
        bindObservers()
    }

    private fun setUpRv() {
        itemAdapter = ItemRecyclerAdapter()
        binding.rvItems.layoutManager = GridLayoutManager(context, 2)
        binding.rvItems.adapter = itemAdapter
    }

    private fun listeners() {
        with(binding) {
            btnAddItem.setOnClickListener {
                if (etFieldName.text!!.isNotEmpty()) {
                    viewModel.addNewItem(
                        Item(
                            Random.nextInt(),
                            binding.etFieldName.text.toString()
                        )
                    )
                } else {
                    etFieldName.error = "Please, enter a card title"
                }
            }
            swipeRefreshLayout.setOnRefreshListener {
                bindObservers()
                swipeRefreshLayout.isRefreshing = false
            }

        }
        itemAdapter.setOnItemClickListener(object : ItemRecyclerAdapter.OnItemClickListener {

            override fun onUpdateClick(position: Int, item: Item) {
                openUpdateDialog(position, item)
            }

            override fun onDeleteClick(position: Int) {
                openDeleteDialog(position)
            }
        })
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.itemFlow.collect {
                    itemAdapter.submitList(it)
                }
            }
        }
    }

    private fun openDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(view?.context)
        builder.setTitle("Are You Sure You Want To Delete This Item?")
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteItem(position)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun openUpdateDialog(position: Int, item: Item) {
        val dialog = AlertDialog.Builder(view?.context)
        val dialogBinding = ItemUpdateDialogBinding.inflate(layoutInflater)
        val dialogView = dialogBinding.root

        dialog.setView(dialogView)
        dialog.setTitle("Updating ${item.title}")
        val alertDialog = dialog.create()

        with(dialogBinding) {
            etUpdateTitle.setText(item.title)

            btnUpdate.setOnClickListener {
                if (etUpdateTitle.text.isNotEmpty()) {
                    viewModel.updateItem(
                        position,
                        Item(Random.nextInt(), etUpdateTitle.text.toString())
                    )
                    alertDialog.dismiss()
                } else {
                    etUpdateTitle.error = "Please, enter a new title"
                }
            }
            tvCancel.setOnClickListener {
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }
}

