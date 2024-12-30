package com.example.simondesenvolvimento00

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simondesenvolvimento00.ui.theme.SimonDesenvolvimento00Theme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.Int
import kotlin.collections.MutableList
import kotlin.random.Random

val ESTADOINICIAL = EstadoJogo(
    sequencia= mutableListOf(4,3,2,1),
    ultimo=3
)
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

data class EstadoJogo(
    var sequencia:MutableList<Int>,
    var ultimo:Int
)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun jogoGenius(){
    val BOARDSIZE = 2
    var cor1 = Color.Green
    var cor2 = Color.Blue
    var cor3 = Color.Red
    var cor4 = Color.Yellow
    var texto: String
    var rodando by remember { mutableStateOf(true) }
    val game = remember { Game(
//                onAndaPara = { if (rodando) rodando = !rodando },
                onAndaPara = { rodando = it },
                rodando = rodando
            )
    }
    if (rodando){
        LaunchedEffect( game) {
            coroutineScope {
                launch { game.roda() }
            }
            rodando = false
        }
    }
    BoxWithConstraints(Modifier.padding(16.dp))  {
        val tileSize = maxWidth / BOARDSIZE

        Column(
            modifier = Modifier.background(Color.LightGray),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            texto = "Tocando: ${game.tocando}"
            Text(
                text = texto, color = Color.Blue, fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            texto = "Contateste: ${game.contateste}"
            Text(
                text = texto, color = Color.Blue, fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            texto = "Sequencia: ${game.estadoAtual.sequencia}"
            Text(
                text = texto, color = Color.Blue, fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            when (game.tocando) {
                1 -> cor1 = colorResource(id = R.color.light_green)
                2-> cor2 = colorResource(id = R.color.light_blue)
                3-> cor3 = colorResource(id = R.color.light_red)
                4-> cor4 = colorResource(id = R.color.light_yellow)
                else -> {
                    cor1 = Color.Green
                    cor2 = Color.Blue
                    cor3 = Color.Red
                    cor4 = Color.Yellow
                }
            }
            Row() {
                Button(onClick = { game.onTeclado(1) },
                        modifier = Modifier
                        .size(tileSize),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cor1))
                {
                    Text(text = "1")
                }
                Button(onClick = { game.onTeclado(2) },
                    modifier = Modifier.size(tileSize),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cor2)) {
                    Text(text = "2")
                }
            }
            Row() {
                Button(onClick = { game.onTeclado(3) },
                    modifier = Modifier.size(tileSize),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cor3)) {
                    Text(text = "3")
                }
                Button(onClick = { game.onTeclado(4) },
                    modifier = Modifier.size(tileSize),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cor4)) {
                    Text(text = "4")
                }
            }




            Button(
                onClick = { rodando = (!rodando) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Clique")
            }
        }
    }
}

class Game(estadoJogo: EstadoJogo = ESTADOINICIAL,
           onAndaPara: (Boolean) -> Unit,rodando: Boolean = true){

    var contador by mutableStateOf( 0 )
        private set
    var estadoAtual by mutableStateOf(estadoJogo)
    var tocando by mutableStateOf(0)
    var contateste by mutableStateOf( 0 )
    var onAndaPara by mutableStateOf(onAndaPara)
        private set
    var minhaVez by mutableStateOf(true)
    val toneGenerator =  ToneGenerator(AudioManager.STREAM_MUSIC, 50)

    suspend fun roda(){
        while (true) {
            delay(700)
            if (minhaVez) {
                estadoAtual.sequencia.forEach { numero ->
                    coroutineScope {
                        toneGenerator.startTone(numero,1000)
                        tocando = numero
                        delay(700)
                        tocando = 0
                        delay(50)
                    }
                }
                minhaVez = false
            }
            delay(700)
            tocando = 0
            onAndaPara(false)
            contateste=0
        }
    }

    fun onTeclado(digito: Int) {
        toneGenerator.startTone(digito,300)
        if (digito == estadoAtual.sequencia[contateste]) {
            contateste++
            if (contateste >= estadoAtual.sequencia.size) {
                estadoAtual.sequencia.add(Random.nextInt(1, 5))
                contateste = 0
                onAndaPara(true)
                minhaVez = true
            }
        }else{
            contateste = 0
            tocando =99999
            estadoAtual.sequencia.clear()
            estadoAtual.sequencia.add(Random.nextInt(1, 5))
            minhaVez = true
            onAndaPara(false)
   //
        }

    }
    fun reset() {
         estadoAtual= ESTADOINICIAL
        contador = 0
        tocando = 0
        contateste=0
        onAndaPara(false)
    }
    companion object{
        const val BOARD_SIZE = 16
        val ESTADOINICIAL = EstadoJogo(
             sequencia= mutableListOf(4,3,2,1),
             ultimo=3
        )
    }
}