package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.TbsHeader
import com.example.ui.theme.*

@Composable
fun WelcomeScreen(
  onStartNewClient: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(TbsIvory)
  ) {
    // Header with "THE BESPOKE SUITS" in serif and monogram badge
    TbsHeader(
      showBackArrow = false
    )

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp, vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(8.dp))

      // Clean Atelier Hero Image (No text, signage, names or brands anywhere)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(230.dp)
          .clip(RoundedCornerShape(8.dp))
          .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_atelier_clean),
          contentDescription = "Bespoke tailoring atelier with suits on mannequins",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Bespoke Intro Card
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color.White)
          .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
          .padding(20.dp)
      ) {
        Column {
          Text(
            text = "Virtual Bespoke Consultation",
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            color = TbsNavy
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "See your client in his new suit before it is made. Take a photo, choose the style, design and colors, and preview the complete look in seconds.",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = TbsCharcoal
          )

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .background(TbsBrassGoldContainer.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
              .padding(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(TbsBrassGold)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "In-store consultant tool · Photos are never stored",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp,
              color = TbsNavy
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      // Start New Client Button with visible ivory (#F7F4EE) uppercase text with letter spacing
      Button(
        onClick = onStartNewClient,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = TbsNavy,
          contentColor = TbsIvory
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("start_new_client_button")
      ) {
        Text(
          text = "START NEW CLIENT",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.5.sp,
          letterSpacing = 1.6.sp,
          color = TbsIvory
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
