package com.example.nearbylocations.domain

import com.example.nearbylocations.domain.base.State
import com.example.nearbylocations.domain.entities.NearbyPlace
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import io.mockk.MockKAnnotations as MockKAnnotations1

internal class FetchNearbyLocationsUseCaseTest{

    // si no se defina una respuesta de la clase creada la genera en automatico
    // If doesn't define some response of DirectionsRepository it'll generate
    // @RelaxedMockK
    @MockK
    private lateinit var directionsRepository:DirectionsRepository

    lateinit var fetchNearbyLocationsUseCase: FetchNearbyLocationsUseCase

    @Before
    fun obBefore(){
        MockKAnnotations1.init(this)
        fetchNearbyLocationsUseCase = FetchNearbyLocationsUseCase(directionsRepository)
    }

    @Test
    fun `when fetch locations return a list`()
    = runBlocking {
        // GIVEN
        val params = FetchNearbyLocationsUseCase.Params(0.0,0.0)
        // coEvery for coroutines (suspend function) Every (without suspend)
        coEvery {
            directionsRepository.fetchNearbyLocations(
                params.latitude,
                params.longitude
            )
        } returns flowOf(State.Success(emptyList<NearbyPlace>()))

        // when
        fetchNearbyLocationsUseCase.run(params)

        // then
        coVerify(exactly = 1) {
            directionsRepository.fetchNearbyLocations(
                params.latitude,
                params.longitude
            )
        }

    }
}