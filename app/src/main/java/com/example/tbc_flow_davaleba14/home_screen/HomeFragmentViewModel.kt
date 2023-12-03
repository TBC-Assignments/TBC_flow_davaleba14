package com.example.tbc_flow_davaleba14.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel : ViewModel() {

    private val _itemFlow = MutableStateFlow<List<Item>>(emptyList())
    val itemFlow: SharedFlow<List<Item>> = _itemFlow.asStateFlow()

    fun addNewItem(item: Item) {
        viewModelScope.launch {
            _itemFlow.value = _itemFlow.value.toMutableList().apply {
                add(item)
            }
        }
    }

    fun deleteItem(position: Int){
        viewModelScope.launch {
            _itemFlow.value = _itemFlow.value.toMutableList().apply {
                removeAt(position)
            }
        }
    }

    fun updateItem(position: Int, item: Item){
        viewModelScope.launch {
            _itemFlow.value = _itemFlow.value.toMutableList().apply {
                set(position, item)
            }
        }
    }
}