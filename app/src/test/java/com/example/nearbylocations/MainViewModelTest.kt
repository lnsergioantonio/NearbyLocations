package com.example.nearbylocations

import com.example.nearbylocations.domain.FetchNearbyLocationsUseCase
import com.example.nearbylocations.domain.UpdateFavoritePlaceUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class MainViewModelTest{
    @MockK
    private lateinit var fetchNearbyLocationsUseCase: FetchNearbyLocationsUseCase
    @MockK
    private lateinit var updateFavoritePlaceUseCase: UpdateFavoritePlaceUseCase

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        mainViewModel = MainViewModel(fetchNearbyLocationsUseCase, updateFavoritePlaceUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter(){
        Dispatchers.resetMain()
    }

    @Test
    fun test() = runTest{

    }
}