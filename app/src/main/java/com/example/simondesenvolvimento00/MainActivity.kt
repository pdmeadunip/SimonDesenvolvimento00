package com.example.simondesenvolvimento00

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simondesenvolvimento00.ui.theme.SimonDesenvolvimento00Theme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimonDesenvolvimento00Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
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
    jogoGenius()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimonDesenvolvimento00Theme {
        Greeting("Android")
    }
}

@Composable
fun jogoGenius(){
    var texto: String
    var rodando by remember { mutableStateOf(true) }
    val game = remember { Game(
                onAndaPara = { if (rodando) rodando = false}
            )
    }
    if (rodando){
        LaunchedEffect(Unit) {
            coroutineScope {
                launch{game.roda()}
            }
        }
    }
    Column(
        modifier = Modifier.background(Color.LightGray),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(50.dp))
        texto = "Tamanho: ${game.contador}"
        Text(
            text = texto, color = Color.Blue, fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
            )
        Button(
            onClick = { rodando = (!rodando)},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Clique")
        }
    }

}

class Game(onAndaPara: () -> Unit){
    var contador by mutableStateOf( 0 )
    var onAndaPara by mutableStateOf(onAndaPara)
    private set
    suspend fun roda() {
        while (true) {
            delay(500)
            contador++
            if (contador == 10) {
                onAndaPara()
                contador = 0
            }
        }
    }
}