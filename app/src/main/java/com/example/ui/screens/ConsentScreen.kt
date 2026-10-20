package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TbsHeader
import com.example.ui.theme.*

@Composable
fun ConsentScreen(
  verbalConsentChecked: Boolean,
  onVerbalConsentChanged: (Boolean) -> Unit,
  onClientAgrees: () -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(TbsIvory)
  ) {
    TbsHeader(
      showBackArrow = true,
      onBackClick = onCancel
    )

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Security,
          contentDescription = null,
          tint = TbsBrassGold,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "PRIVACY & CLIENT CONSENT",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 11.sp,
          letterSpacing = 1.4.sp,
          color = TbsBrassGold
        )
      }

      Text(
        text = "We'll take a photo to show you in your new suit. Your photo is never stored.",
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        color = TbsNavy
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Four Points Card
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color.White)
          .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
          .padding(20.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
          ConsentPoint(
            number = "01",
            text = "Used exclusively to generate your personalized suit preview in this consultation."
          )
          ConsentPoint(
            number = "02",
            text = "Processed securely in-memory by AI image editing; never archived or logged."
          )
          ConsentPoint(
            number = "03",
            text = "Deleted automatically as soon as this consultation session concludes."
          )
          ConsentPoint(
            number = "04",
            text = "Strictly complies with the UAE Federal Decree-Law on Personal Data Protection (PDPL)."
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Required Checkbox
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(6.dp))
          .background(if (verbalConsentChecked) TbsBrassGoldContainer.copy(alpha = 0.5f) else Color.White)
          .border(
            width = 1.dp,
            color = if (verbalConsentChecked) TbsBrassGold else TbsBorder,
            shape = RoundedCornerShape(6.dp)
          )
          .clickable { onVerbalConsentChanged(!verbalConsentChecked) }
          .padding(horizontal = 14.dp, vertical = 14.dp)
          .testTag("verbal_consent_checkbox_row")
      ) {
        Checkbox(
          checked = verbalConsentChecked,
          onCheckedChange = { onVerbalConsentChanged(it) },
          colors = CheckboxDefaults.colors(
            checkedColor = TbsNavy,
            uncheckedColor = TbsBorder,
            checkmarkColor = Color.White
          ),
          modifier = Modifier.testTag("verbal_consent_checkbox")
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
          text = "Client gave verbal consent",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 14.sp,
          color = TbsNavy
        )
      }

      Spacer(modifier = Modifier.height(30.dp))

      // Buttons: Client Agrees / Cancel
      Button(
        onClick = onClientAgrees,
        enabled = verbalConsentChecked,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = TbsNavy,
          contentColor = Color.White,
          disabledContainerColor = TbsNavy.copy(alpha = 0.35f),
          disabledContentColor = Color.White.copy(alpha = 0.6f)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("client_agrees_button")
      ) {
        Text(
          text = "CLIENT AGREES",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.5.sp,
          letterSpacing = 1.6.sp,
          color = if (verbalConsentChecked) TbsIvory else TbsIvory.copy(alpha = 0.6f)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedButton(
        onClick = onCancel,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = TbsCharcoalMuted
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
          brush = androidx.compose.ui.graphics.SolidColor(TbsBorder)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("consent_cancel_button")
      ) {
        Text(
          text = "CANCEL",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          letterSpacing = 1.2.sp
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun ConsentPoint(
  number: String,
  text: String
) {
  Row(
    verticalAlignment = Alignment.Top,
    modifier = Modifier.fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .size(24.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(TbsNavy.copy(alpha = 0.08f)),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = number,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.5.sp,
        color = TbsNavy
      )
    }

    Spacer(modifier = Modifier.width(12.dp))

    Text(
      text = text,
      fontFamily = InterFontFamily,
      fontWeight = FontWeight.Normal,
      fontSize = 13.sp,
      lineHeight = 18.5.sp,
      color = TbsCharcoal,
      modifier = Modifier.weight(1f)
    )
  }
}
