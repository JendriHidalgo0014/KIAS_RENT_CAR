package edu.ucne.kias_rent_car.presentation.Components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimDark
import edu.ucne.kias_rent_car.ui.theme.surfaceBrightDark
import edu.ucne.kias_rent_car.ui.theme.surfaceContainerDark
import edu.ucne.kias_rent_car.ui.theme.tertiaryContainerDark

@Composable
fun VehicleCard(
    vehicle: Vehicle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = scrimDark
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Imagen del vehículo
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        if (vehicle.imagenUrl.isNotEmpty()) {
                            Uri.parse(vehicle.imagenUrl)
                        } else {
                            null
                        }
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = vehicle.modelo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                loading = {
                    // Mientras carga
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(surfaceContainerDark),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = onErrorDark,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                error = {
                    // Si falla o no hay imagen
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(surfaceContainerDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Car placeholder",
                            tint = surfaceBrightDark,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            )

            // Información del vehículo
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = vehicle.modelo,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = vehicle.transmision.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = surfaceBrightDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$${vehicle.precioPorDia.toInt()}/day",
                    style = MaterialTheme.typography.titleSmall,
                    color = tertiaryContainerDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}