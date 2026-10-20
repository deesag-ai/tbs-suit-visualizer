package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun TbsHeader(
  showBackArrow: Boolean = true,
  onBackClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(TbsIvory)
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // Left: Back button or placeholder spacer
    Box(
      modifier = Modifier.size(44.dp),
      contentAlignment = Alignment.CenterStart
    ) {
      if (showBackArrow) {
        IconButton(
          onClick = onBackClick,
          modifier = Modifier
            .size(44.dp)
            .testTag("header_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Navigate back",
            tint = TbsNavy
          )
        }
      }
    }

    // Center: "THE BESPOKE SUITS" in serif
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.weight(1f)
    ) {
      Text(
        text = "THE BESPOKE SUITS",
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 2.0.sp,
        color = TbsNavy,
        textAlign = TextAlign.Center,
        maxLines = 1
      )
      Text(
        text = "DUBAI · ATELIER",
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 8.5.sp,
        letterSpacing = 1.5.sp,
        color = TbsBrassGold,
        textAlign = TextAlign.Center
      )
    }

    // Right: Small navy "TBS" monogram
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(RoundedCornerShape(6.dp))
        .background(TbsNavy)
        .border(1.dp, TbsBrassGold.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
        .testTag("tbs_monogram_badge"),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "TBS",
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        letterSpacing = 0.8.sp,
        color = TbsBrassGold
      )
    }
  }
}
