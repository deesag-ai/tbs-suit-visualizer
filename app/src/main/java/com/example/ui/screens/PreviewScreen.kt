package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClientSuitSelection
import com.example.model.GeneratedLook
import com.example.ui.components.FlowStep
import com.example.ui.components.TbsHeader
import com.example.ui.components.TbsStepBar
import com.example.ui.theme.*

@Composable
fun PreviewScreen(
  look: GeneratedLook?,
  selection: ClientSuitSelection,
  generationError: String?,
  lookCount: Int,
  onTryAnotherLook: () -> Unit,
  onCompareLooks: () -> Unit,
  onRetryGeneration: () -> Unit,
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

    TbsStepBar(currentStep = FlowStep.PREVIEW)

    if (generationError != null) {
      // Clear error state with Try Again button
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
            .padding(24.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = TbsBrassGold,
            modifier = Modifier.size(40.dp)
          )
          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "Tailoring Service Notice",
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = TbsNavy
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = generationError,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            color = TbsCharcoalMuted,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = onRetryGeneration,
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = TbsNavy,
              contentColor = TbsIvory
            ),
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(min = 48.dp)
              .testTag("retry_generation_button")
          ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = TbsIvory)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "TRY AGAIN",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.5.sp,
              letterSpacing = 0.8.sp,
              lineHeight = 16.sp,
              textAlign = TextAlign.Center,
              maxLines = 2,
              color = TbsIvory
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedButton(
            onClick = onTryAnotherLook,
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TbsNavy),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
              brush = androidx.compose.ui.graphics.SolidColor(TbsNavy)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(min = 46.dp)
          ) {
            Text(
              text = "MODIFY SELECTIONS",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.5.sp,
              letterSpacing = 0.8.sp,
              lineHeight = 16.sp,
              textAlign = TextAlign.Center,
              maxLines = 2,
              color = TbsNavy
            )
          }
        }
      }
    } else {
      Column(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .verticalScroll(scrollState)
          .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Large Full-Length Generated Image - complete image shown with ContentScale.Fit
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(520.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF14171E))
            .border(1.5.dp, TbsBrassGold, RoundedCornerShape(8.dp)),
          contentAlignment = Alignment.Center
        ) {
          if (look != null) {
            Image(
              bitmap = look.bitmap.asImageBitmap(),
              contentDescription = "Full-length bespoke tailored look preview",
              contentScale = ContentScale.Fit,
              modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .testTag("generated_suit_image")
            )

            // Luxury Tag
            Box(
              modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(TbsNavy.copy(alpha = 0.88f))
                .border(0.8.dp, TbsBrassGold.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Text(
                text = "BESPOKE VISUALIZATION",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.5.sp,
                letterSpacing = 1.2.sp,
                maxLines = 1,
                color = TbsBrassGold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Selections summary card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
            .padding(16.dp)
        ) {
          Column {
            Text(
              text = "BESPOKE SPECIFICATIONS",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              letterSpacing = 1.2.sp,
              color = TbsBrassGold
            )
            Spacer(modifier = Modifier.height(10.dp))

            SelectionDetailRow("Suit Silhouette", selection.style.displayName)
            SelectionDetailRow("Lapel & Front", "${selection.lapel.displayName} · ${selection.buttons.displayName}")
            SelectionDetailRow("Pockets & Vents", "${selection.pockets.displayName} · ${selection.vents.displayName}")
            SelectionDetailRow("Jacket Color", selection.jacketColor.name)
            SelectionDetailRow("Trousers Color", selection.effectiveTrouserColor().name)
            SelectionDetailRow("Shirting & Tie", "${selection.shirtColor.name} · ${if (selection.tieColor.isNoTie) "No Tie" else selection.tieColor.name}")
            SelectionDetailRow("Footwear", selection.shoeColor.name)
          }
        }

        Spacer(modifier = Modifier.height(18.dp))
      }

      // Sticky Bottom Buttons: "Try Another Look" and "Send / Share"
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(TbsIvory)
          .border(1.dp, TbsBorder.copy(alpha = 0.5f))
          .padding(horizontal = 20.dp, vertical = 14.dp)
          .windowInsetsPadding(WindowInsets.navigationBars)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedButton(
            onClick = onTryAnotherLook,
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TbsNavy),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
              brush = androidx.compose.ui.graphics.SolidColor(TbsNavy)
            ),
            modifier = Modifier
              .weight(1f)
              .heightIn(min = 50.dp)
              .testTag("try_another_look_button")
          ) {
            Text(
              text = "TRY ANOTHER LOOK",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp,
              letterSpacing = 0.6.sp,
              lineHeight = 15.sp,
              textAlign = TextAlign.Center,
              maxLines = 2,
              color = TbsNavy
            )
          }

          Button(
            onClick = onCompareLooks,
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = TbsNavy,
              contentColor = Color(0xFFF7F4EE)
            ),
            modifier = Modifier
              .weight(1f)
              .heightIn(min = 50.dp)
              .testTag("send_share_button")
          ) {
            Text(
              text = if (lookCount > 1) "SEND / SHARE ($lookCount)" else "SEND / SHARE",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp,
              letterSpacing = 0.6.sp,
              lineHeight = 15.sp,
              textAlign = TextAlign.Center,
              maxLines = 2,
              color = Color(0xFFF7F4EE)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun SelectionDetailRow(
  label: String,
  value: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.5.dp),
    verticalAlignment = Alignment.Top
  ) {
    Text(
      text = label,
      fontFamily = InterFontFamily,
      fontSize = 12.5.sp,
      color = TbsCharcoalMuted
    )
    Spacer(modifier = Modifier.width(12.dp))
    Text(
      text = value,
      fontFamily = InterFontFamily,
      fontWeight = FontWeight.Medium,
      fontSize = 12.5.sp,
      textAlign = TextAlign.End,
      color = TbsNavy,
      modifier = Modifier.weight(1f)
    )
  }
}