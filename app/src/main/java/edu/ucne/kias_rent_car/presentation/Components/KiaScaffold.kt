package edu.ucne.kias_rent_car.presentation.Components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KiaScaffold(
    title: String = "KIA'S RENT CAR",
    showBackButton: Boolean = true,
    onNavigateBack: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        containerColor = scrimLight,
        content = content
    )
}