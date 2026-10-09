package org.insbaixcamp.imca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.insbaixcamp.imca.ui.theme.ImcATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImcATheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                        BMIScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun BMIScreen(){
    var name:String by remember { mutableStateOf("") }
    var weight:Int by remember { mutableStateOf(80) }
    var height:Int by remember { mutableStateOf(180) }
    var bmi: Double by remember { mutableStateOf(0.0) }
    Column() {
        Text(text = "BMI Calculator")
        TextField(
            value = name,
            onValueChange = {name = it},
            label = {
                Text(text = "Name")
            }
        )
        Row() {
            Text(text = "Weight: $weight kg")
            Button(
                onClick = {weight--}
            ) {
                Text(text = "-")
            }
            Button(
                onClick = {weight++}
            ) {
                Text(text = "+")
            }
        }
        Text(text = "Height: $height cm")
        Slider(
            value = height.toFloat(),
            onValueChange = {height = it.toInt()},
            valueRange = 100f..250f,
        )
        Button(onClick = {
            val heightMeters = height / 100f
            bmi = (weight / (heightMeters * heightMeters)).toDouble()
        }) {
            Text(text = "Calculate BMI")
        }
        if(bmi != 0.0){
            Text(text = "BMI: $bmi")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImcATheme {
        BMIScreen()
    }
}