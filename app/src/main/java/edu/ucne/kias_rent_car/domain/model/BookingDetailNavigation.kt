package edu.ucne.kias_rent_car.domain.model

data class BookingDetailNavigation(
    val onNavigateBack: () -> Unit,
    val onNavigateToModify: (String) -> Unit,
    val onNavigateToHome: () -> Unit,
    val onNavigateToBookings: () -> Unit,
    val onNavigateToSupport: () -> Unit,
    val onNavigateToProfile: () -> Unit
)