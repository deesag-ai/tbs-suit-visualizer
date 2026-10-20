package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GeneratedLook
import com.example.ui.components.TbsHeader
import com.example.ui.theme.*

@Composable
fun CompareAndSendScreen(
  looks: List<GeneratedLook>,
  selectedLookIndex: Int,
  onSelectLook: (Int) -> Unit,
  whatsAppNumber: String,
  onWhatsAppNumberChange: (String) -> Unit,
  onSendToWhatsApp: () -> Unit,
  onBack: () -> Unit,
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
      onBackClick = onBack
    )

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
      Text(
        text = "Compare & Finalize Look",
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = TbsNavy
      )

      Text(
        text = "Swipe through tailored looks, select client preference, and deliver via WhatsApp.",
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        color = TbsCharcoalMuted,
        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
      )

      // Horizontal swipeable cards for up to 3 looks
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        looks.forEachIndexed { index, look ->
          val isSelected = index == selectedLookIndex
          LookCard(
            look = look,
            lookIndex = index + 1,
            isSelected = isSelected,
            onChoose = { onSelectLook(index) }
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // WhatsApp Delivery Card
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color.White)
          .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
          .padding(18.dp)
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF25D366)) // WhatsApp green accent
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "WHATSAPP CLIENT DELIVERY",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              letterSpacing = 1.0.sp,
              color = TbsNavy
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Enter client's WhatsApp number to dispatch the selected bespoke visualization and atelier specifications.",
            fontFamily = InterFontFamily,
            fontSize = 12.5.sp,
            color = TbsCharcoalMuted
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = whatsAppNumber,
            onValueChange = onWhatsAppNumberChange,
            label = {
              Text(
                text = "Client WhatsApp Number",
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = TbsCharcoalMuted
              )
            },
            placeholder = {
              Text(
                text = "+971 50 123 4567",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                color = TbsCharcoalMuted.copy(alpha = 0.5f)
              )
            },
            singleLine = true,
            shape = RoundedCornerShape(6.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = TbsNavy,
              unfocusedBorderColor = TbsBorder,
              focusedContainerColor = TbsIvory,
              unfocusedContainerColor = TbsIvory
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("whatsapp_number_input")
          )

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = onSendToWhatsApp,
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = TbsNavy,
              contentColor = Color(0xFFF7F4EE)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("send_to_whatsapp_button")
          ) {
            Icon(
              imageVector = Icons.Default.Send,
              contentDescription = null,
              tint = Color(0xFFF7F4EE),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "SEND TO WHATSAPP",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp,
              letterSpacing = 1.4.sp,
              color = Color(0xFFF7F4EE)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun LookCard(
  look: GeneratedLook,
  lookIndex: Int,
  isSelected: Boolean,
  onChoose: () -> Unit
) {
  val borderColor = if (isSelected) TbsBrassGold else TbsBorder
  val cardWidth = 260.dp

  Column(
    modifier = Modifier
      .width(cardWidth)
      .clip(RoundedCornerShape(8.dp))
      .background(Color.White)
      .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(8.dp))
      .clickable { onChoose() }
      .padding(12.dp)
      .testTag("look_card_$lookIndex")
  ) {
    // Image
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(340.dp)
        .clip(RoundedCornerShape(6.dp))
        .background(Color(0xFF16181D))
    ) {
      Image(
        bitmap = look.bitmap.asImageBitmap(),
        contentDescription = "Look $lookIndex preview",
        contentScale = ContentScale.Fit,
        modifier = Modifier
          .fillMaxSize()
          .padding(4.dp)
      )

      Box(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(8.dp)
          .clip(RoundedCornerShape(4.dp))
          .background(TbsNavy.copy(alpha = 0.85f))
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Text(
          text = "LOOK 0$lookIndex",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 9.sp,
          letterSpacing = 1.0.sp,
          color = TbsBrassGold
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
      text = look.selection.style.displayName,
      fontFamily = CormorantGaramond,
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp,
      color = TbsNavy
    )

    Text(
      text = "${look.selection.jacketColor.name} · ${look.selection.lapel.displayName}",
      fontFamily = InterFontFamily,
      fontSize = 11.5.sp,
      color = TbsCharcoalMuted
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Choose This Look button
    Button(
      onClick = onChoose,
      shape = RoundedCornerShape(4.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = if (isSelected) TbsBrassGold else TbsNavy,
        contentColor = if (isSelected) TbsNavy else TbsIvory
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .testTag("choose_look_button_$lookIndex")
    ) {
      if (isSelected) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = null,
          tint = TbsNavy,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
      }
      Text(
        text = if (isSelected) "CHOSEN LOOK" else "CHOOSE THIS LOOK",
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 0.8.sp,
        color = if (isSelected) TbsNavy else TbsIvory
      )
    }
  }
}
