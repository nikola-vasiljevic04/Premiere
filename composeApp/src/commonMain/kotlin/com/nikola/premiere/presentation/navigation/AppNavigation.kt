package com.nikola.premiere.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nikola.premiere.presentation.details.DetailsScreen
import com.nikola.premiere.presentation.details.DetailsViewModel
import com.nikola.premiere.presentation.filters.FiltersScreen
import com.nikola.premiere.presentation.filters.FiltersViewModel
import com.nikola.premiere.presentation.movies_list.MoviesScreen
import com.nikola.premiere.presentation.movies_list.MoviesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation (
    startDestination: String = "movies" ,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(
            route = "movies",
        ) {
            val viewModel = koinViewModel<MoviesViewModel>()
            MoviesScreen(
                viewModel= viewModel,
                onThemeToggle = onThemeToggle,
                onNavigateToDetails = {
                    movieId ->navController.navigateToMovieDetails(movieId)
                },
                onNavigateToFilters = {
                    navController.navigate("filters")
                }
            )
        }

        composable(
            route = "filters",
        ) {
            val filtersViewModel = koinViewModel<FiltersViewModel>()
            FiltersScreen(
                viewModel = filtersViewModel,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = "details/{$MOVIE_ID}",
            arguments = listOf(
                navArgument(MOVIE_ID) {
                    type = NavType.StringType
                    nullable = false
                }
            ),
        ) {
            val viewModel = koinViewModel<DetailsViewModel>()
            DetailsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
private fun NavController.navigateToMovieDetails(movieId: String) {
    navigate("details/$movieId")
}
const val MOVIE_ID = "movieId"
inline val SavedStateHandle.movieId: String? get() = get(MOVIE_ID)
inline val SavedStateHandle.movieIdOrThrow: String get() = get(MOVIE_ID)
    ?: throw IllegalStateException("$MOVIE_ID is mandatory and can not be null")