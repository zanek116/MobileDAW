//package com.example.mobiledaw.Instruments
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import com.example.mobiledaw.Database.Song
//import com.example.mobiledaw.Database.SongRepository
//
//import kotlinx.coroutines.launch
//
//class PianoActivityViewModel (private val repository: SongRepository) : ViewModel() {
//
//    // Using LiveData and caching what allWords returns has several benefits:
//    // - We can put an observer on the data (instead of polling for changes) and only update the
//    //   the UI when the data actually changes.
//    // - Repository is completely separated from the UI through the ViewModel.
//    val allWords: LiveData<List<Song>> = repository.allSongs.asLiveData()
//
//    fun update(song: Song) {
//        viewModelScope.launch {
//            repository.update(song)
//        }
//    }
//
//}
//
//    //MapsFact
//    class MapsViewModelFactory(private val repository: SongRepository) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return MapsViewModel(repository) as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
//
//}