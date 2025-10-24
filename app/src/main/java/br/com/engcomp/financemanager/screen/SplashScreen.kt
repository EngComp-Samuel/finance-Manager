package br.com.engcomp.financemanager.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.engcomp.financemanager.R
import br.com.engcomp.financemanager.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController, onFinish: () -> Unit,){

    //iniciando animacao
    var iniciarAnimacao by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if(iniciarAnimacao) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1800, //3000
            easing = LinearEasing
        )
    )


    LaunchedEffect(key1 = true){
        iniciarAnimacao = true
        delay(1800) //4000
        navController.popBackStack()
       // navController.navigate(Screen.Home.route)
        onFinish()
    }

    Splash(alpha = alphaAnim.value)
}



@Composable
fun Splash(alpha: Float){

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                painter = painterResource(id = R.drawable.outline_attach_money_24),
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(10.dp)
                    .size(70.dp)
                    .alpha(alpha = alpha)
            )

            Text(
                text = "Finance Manager",
                modifier = Modifier
                    .padding(10.dp)
                    .alpha(alpha = alpha),
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}