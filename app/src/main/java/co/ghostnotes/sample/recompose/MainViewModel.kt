package co.ghostnotes.sample.recompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    private val _count = MutableSharedFlow<Int>()
    val count: SharedFlow<Int> = _count
    private var countInternal: Int = 0

    private val mutex: Mutex = Mutex()

    fun countUp() = viewModelScope.launch(defaultDispatcher) {
        mutex.withLock {
            if (countInternal > 0) return@withLock

            while (countInternal < MAX_COUNT) {
                countInternal++
                val threadName = Thread.currentThread().name
                Timber.d("### $threadName: count=$countInternal")
                _count.emit(countInternal)
                delay(1000L)
            }
        }
    }

    companion object {

        private const val MAX_COUNT = Integer.MAX_VALUE

    }
}