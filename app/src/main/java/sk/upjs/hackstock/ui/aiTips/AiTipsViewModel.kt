package sk.upjs.hackstock.ui.aiTips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AiTipsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is ai tips Fragment"
    }
    val text: LiveData<String> = _text
}