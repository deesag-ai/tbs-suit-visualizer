package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun TbsBottomBar(
  onBack: () -> Unit,
  onContinue: () -> Unit,
  continueText: String = "CONTINUE",
  backText: String = "BACK",
  continueEnabled: Boolean = true,
  backEnabled: Boolean = true,
  showBackButton: Boolean = true,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(TbsIvory)
      .border(width = 1.dp, color = TbsBorder.copy(alpha = 0.5f))
      .padding(horizontal = 20.dp, vertical = 14.dp)
      .windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (showBackButton) {
        OutlinedButton(
          onClick = onBack,
          enabled = backEnabled,
          shape = RoundedCornerShape(6.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = TbsNavy,
            disabledContentColor = TbsCharcoalMuted.copy(alpha = 0.4f)
          ),
          border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(TbsNavy)
          ),
          modifier = Modifier
            .weight(1f)
            .heightIn(min = 50.dp)
            .testTag("bottom_back_button")
        ) {
          Text(
            text = backText.uppercase(),
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            letterSpacing = 0.8.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
            maxLines = 2
          )
        }
      }

      Button(
        onClick = onContinue,
        enabled = continueEnabled,
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = TbsNavy,
          contentColor = TbsIvory,
          disabledContainerColor = TbsNavy.copy(alpha = 0.35f),
          disabledContentColor = TbsIvory.copy(alpha = 0.6f)
        ),
        modifier = Modifier
          .weight(if (showBackButton) 2f else 1f)
          .heightIn(min = 50.dp)
          .testTag("bottom_continue_button")
      ) {
        Text(
          text = continueText.uppercase(),
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          letterSpacing = 0.8.sp,
          lineHeight = 16.sp,
          textAlign = TextAlign.Center,
          maxLines = 2,
          color = if (continueEnabled) TbsIvory else TbsIvory.copy(alpha = 0.6f)
        )
      }
    }
  }
}