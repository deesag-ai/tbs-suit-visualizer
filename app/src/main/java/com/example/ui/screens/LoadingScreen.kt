package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TbsHeader
import com.example.ui.theme.*

@Composable
fun LoadingScreen(
  modifier: Modifier = Modifier
) {
  // Gentle animated progress
  val infiniteTransition = rememberInfiniteTransition(label = "tailor_progress")
  val progress by infiniteTransition.animateFloat(
    initialValue = 0.1f,
    targetValue = 0.95f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 3500, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "progress_val"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(TbsIvory)
  ) {
    TbsHeader(showBackArrow = false)

    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 32.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        // Monogram Badge
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(TbsNavy),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "TBS",
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = TbsBrassGold
          )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
          text = "Tailoring your look…",
          fontFamily = CormorantGaramond,
          fontWeight = FontWeight.Bold,
          fontSize = 28.sp,
          letterSpacing = 0.5.sp,
          color = TbsNavy,
          textAlign = TextAlign.Center,
          modifier = Modifier.testTag("loading_title")
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Sculpting the silhouette, aligning lapels, and rendering authentic details to your posture.",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 13.5.sp,
          lineHeight = 20.sp,
          color = TbsCharcoalMuted,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Thin brass progress bar
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(TbsBorder)
        ) {
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(3.dp)
              .testTag("brass_progress_bar"),
            color = TbsBrassGold,
            trackColor = TbsIvoryDark
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "THE BESPOKE SUITS · DUBAI ATELIER",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 10.sp,
          letterSpacing = 1.8.sp,
          color = TbsBrassGold
        )
      }
    }
  }
}
