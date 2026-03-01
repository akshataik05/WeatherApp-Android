package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    val apiKey = "d09c8d6958916ae9a1fb66d7b9f0cfb3"


    var city by remember { mutableStateOf("Bangalore") }


    LaunchedEffect(Unit) {
        viewModel.loadWeather("Bangalore", apiKey)
    }

    val weather = viewModel.weatherData.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val error = viewModel.errorMessage.collectAsState().value

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF5B3EAE),
            Color(0xFF8A4FFF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Enter City Name") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        cursorColor = Color.White
                    )
                )

                Button(
                    onClick = {
                        viewModel.loadWeather(city, apiKey)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF432AA8)
                    )
                ) {
                    Text("Search", color = Color.White)
                }
            }


            if (isLoading) {
                Text(
                    text = "Loading...",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                return@Column
            }

            if (error != null) {
                Text(
                    text = error,
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                return@Column
            }

            Text(
                text = weather?.weather?.firstOrNull()?.main ?: "Loading...",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 20.dp)
            )


            Image(
                painter = painterResource(id = R.drawable.ic_cloud),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 10.dp),
                contentScale = ContentScale.Fit
            )


            Text(
                text = weather?.dt?.let { formatDateTime(it) } ?: "Loading...",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 10.dp)
            )


            Text(
                text = weather?.main?.temp?.toInt()?.toString()?.plus("°") ?: "--°",
                fontSize = 70.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp)
            )


            Text(
                text = "H: ${weather?.main?.temp_max?.toInt() ?: "--"}°  L: ${weather?.main?.temp_min?.toInt() ?: "--"}°",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 6.dp)
            )


            WeatherStatsRow(weather)


            TodayForecastRow()


            FutureForecastList()
        }
    }
}


@Composable
fun WeatherStatsRow(weather: com.example.weatherapp.model.WeatherResponse?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        WeatherStatItem(
            icon = R.drawable.ic_rain,
            value = "0%",
            label = "Rain"
        )

        WeatherStatItem(
            icon = R.drawable.ic_wind,
            value = "${weather?.wind?.speed ?: "--"} m/s",
            label = "Wind"
        )

        WeatherStatItem(
            icon = R.drawable.ic_humidity,
            value = "${weather?.main?.humidity ?: "--"}%",
            label = "Humidity"
        )
    }
}

@Composable
fun WeatherStatItem(icon: Int, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(40.dp)
        )

        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = label,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
    }
}


@Composable
fun TodayForecastRow() {

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = "Today",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(top = 20.dp, start = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TodayForecastItem("9 PM", R.drawable.ic_today_1, "28°")
            TodayForecastItem("10 PM", R.drawable.ic_today_2, "29°")
            TodayForecastItem("11 PM", R.drawable.ic_today_3, "30°")
            TodayForecastItem("12 PM", R.drawable.ic_today_4, "31°")
        }
    }
}

@Composable
fun TodayForecastItem(time: String, icon: Int, temperature: String) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .background(
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 10.dp, horizontal = 6.dp)
    ) {

        Text(
            text = time,
            fontSize = 14.sp,
            color = Color.White
        )

        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .padding(top = 8.dp)
        )

        Text(
            text = temperature,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}


@Composable
fun FutureForecastList() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {

        Text(
            text = "Next 7 Days",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 10.dp, start = 8.dp)
        )

        FutureForecastItem("Mon", R.drawable.ic_today_1, "18°", "25°")
        FutureForecastItem("Tue", R.drawable.ic_today_2, "20°", "27°")
        FutureForecastItem("Wed", R.drawable.ic_today_3, "19°", "26°")
        FutureForecastItem("Thu", R.drawable.ic_today_4, "21°", "28°")
        FutureForecastItem("Fri", R.drawable.ic_today_2, "18°", "24°")
        FutureForecastItem("Sat", R.drawable.ic_today_3, "17°", "23°")
        FutureForecastItem("Sun", R.drawable.ic_today_1, "16°", "22°")
    }
}

@Composable
fun FutureForecastItem(
    day: String,
    icon: Int,
    min: String,
    max: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(
                Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = day,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )

        Image(
            painter = painterResource(id = icon),
            contentDescription = day,
            modifier = Modifier
                .size(35.dp)
                .weight(1f),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "$min / $max",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}


fun formatDateTime(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val sdf = SimpleDateFormat("EEE, MMM dd | hh:mm a", Locale.getDefault())
    return sdf.format(date)
}
