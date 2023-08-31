package com.john_halaka.notes.ui.presentaion.daily_quote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john_halaka.notes.feature_daily_quote.domain.model.Quote
import com.john_halaka.notes.feature_daily_quote.domain.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DailyQuoteViewModel @Inject constructor(
    private val repository: QuoteRepository
) : ViewModel() {
//    private val repository = QuoteRepositoryImpl()

    private val _quote = MutableLiveData<List<Quote>>()
    val quote: LiveData<List<Quote>> = _quote

    fun fetchQuote() {
        viewModelScope.launch {
            try {
                Log.d("QuoteViewModel", "fetchQuote is called")
                val dailyQuote = repository.getQuote()
                _quote.value = dailyQuote
            } catch (e: IOException) {
                Log.e("QuoteViewModel", e.message.toString())
            } catch (e: HttpException) {
                Log.e("QuoteViewModel", e.message.toString())
            }


        }
    }
}