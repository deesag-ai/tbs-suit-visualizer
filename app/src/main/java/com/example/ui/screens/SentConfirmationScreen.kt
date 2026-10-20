package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.ui.components.TbsHeader
import com.example.ui.theme.*

@Composable
fun SentConfirmationScreen(
  onNewClient: () -> Unit,
  modifier: Modifier = Modifier
) {
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
        .padding(horizontal = 24.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color.White)
          .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
          .padding(28.dp)
      ) {
        // Success Icon Circle
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(TbsBrassGoldContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = TbsNavy,
            modifier = Modifier.size(32.dp)
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Required text: "✓ Sent. Photo deleted."
        Text(
          text = "✓ Sent. Photo deleted.",
          fontFamily = CormorantGaramond,
          fontWeight = FontWeight.Bold,
          fontSize = 26.sp,
          color = TbsNavy,
          textAlign = TextAlign.Center,
          modifier = Modifier.testTag("sent_confirmation_heading")
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "The tailored consultation look and bespoke specifications have been forwarded to the client's WhatsApp.",
          fontFamily = InterFontFamily,
          fontSize = 13.5.sp,
          lineHeight = 19.sp,
          color = TbsCharcoalMuted,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Privacy Guarantee Pill
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(TbsNavy.copy(alpha = 0.05f))
            .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
          Icon(
            imageVector = Icons.Default.DeleteForever,
            contentDescription = null,
            tint = TbsNavy,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Zero storage: All photos purged from device RAM according to UAE PDPL regulations.",
            fontFamily = InterFontFamily,
            fontSize = 11.5.sp,
            lineHeight = 16.sp,
            color = TbsNavy
          )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // "New Client" button that clears everything
        Button(
          onClick = onNewClient,
          shape = RoundedCornerShape(6.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = TbsNavy,
            contentColor = TbsIvory
          ),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("new_client_button")
        ) {
          Icon(
            imageVector = Icons.Default.PersonAdd,
            contentDescription = null,
            tint = TbsIvory,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "NEW CLIENT",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            letterSpacing = 1.4.sp,
            color = TbsIvory
          )
        }
      }
    }
  }
}
