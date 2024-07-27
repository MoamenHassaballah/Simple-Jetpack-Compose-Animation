package com.moaapps.favanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moaapps.favanimation.ui.theme.FavAnimationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FavAnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FavAnimationView(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavAnimationView(name: String, modifier: Modifier = Modifier) {

    var size by remember {
        mutableIntStateOf(0)
    }

    var rotationDegree by remember {
        mutableFloatStateOf(0f)
    }

    var starImage by remember {
        mutableIntStateOf(R.drawable.baseline_star_outline_24)
    }

    var heartImage by remember {
        mutableIntStateOf(R.drawable.baseline_star_outline_24)
    }

    var isStarFav by remember {
        mutableIntStateOf(0)
    }

    var isHeartFav by remember {
        mutableIntStateOf(0)
    }

    var switchLabel by remember {
        mutableStateOf("Off")
    }

    var fanTarget by remember {
        mutableFloatStateOf(0f)
    }

    val fanAnimationInfinite = rememberInfiniteTransition()
    val fanAnimation = fanAnimationInfinite.animateFloat(
        initialValue = 0f,
        targetValue = fanTarget,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 100
            ),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val starSizeAnimator = animateDpAsState(
        targetValue = size.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "",
    )

    val imageAnimator = animateIntAsState(
        targetValue = starImage,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "",
    )

    val heartRotationAnimator = animateFloatAsState(
        targetValue = rotationDegree,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "",
    )



    LaunchedEffect(key1 = isStarFav) {
        size = 100
        delay(100)
        size = 200

        starImage = if (isStarFav == 0) {
            R.drawable.baseline_star_outline_24
        }else{
            R.drawable.ic_star_filled
        }

//        rotationDegree = if (isStarFav == 0) {
//            0f
//        }else{
//            360f
//        }

    }

    LaunchedEffect(key1 = isHeartFav) {

        heartImage = if (isHeartFav == 0) {
            R.drawable.heart_outline
        }else{
            R.drawable.heart_filled
        }

        rotationDegree = if (isHeartFav == 0) {
            0f
        }else{
            360f
        }


    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,

    ){
        Icon(
            painter = painterResource(id = imageAnimator.value),
            contentDescription = "fav",
            modifier = Modifier
                .size(starSizeAnimator.value)
//                .rotate(rotationAnimator.value)
                .clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }
                ) {
                    isStarFav = if (isStarFav == 0) {
                        1
                    } else {
                        0
                    }
                },
            tint = Color.Unspecified
        )


        Icon(
            painter = painterResource(id = heartImage),
            contentDescription = "fav",
            modifier = Modifier
                .size(200.dp)
//                .rotate(rotationAnimator.value)
                .graphicsLayer {
                    rotationY = heartRotationAnimator.value
                }
                .clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }
                ) {
                    isHeartFav = if (isHeartFav == 0) {
                        1
                    } else {
                        0
                    }
                },
            tint = Color.Unspecified
        )

        Icon(
            painter = painterResource(id = R.drawable.fan),
            contentDescription = "fav",
            modifier = Modifier
                .size(200.dp)
                .rotate(fanAnimation.value),
            tint = Color.Unspecified
        )

        AnimatedContent(
            targetState = switchLabel,
            transitionSpec = {
                slideInVertically { height -> -height } togetherWith slideOutVertically { height -> height }
            },
            label = ""
        ) { targetLabel ->
            Text(
                text = targetLabel,
                fontSize = 50.sp,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        switchLabel = if (switchLabel == "Off") {
                            fanTarget = 360f
                            "On"
                        }else{
                            fanTarget = 0f
                            "Off"
                        }
                    }
            )

        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FavAnimationTheme {
        FavAnimationView("Android")
    }
}