package com.nikola.premiere.domain.usecase

import com.nikola.premiere.data.model.Genre
import com.nikola.premiere.domain.repository.MovieRepository

class GetGenresUseCase (
    private val repository: MovieRepository
){
    suspend operator fun invoke(): List<Genre> {
        return repository.getGenres()
    }

}