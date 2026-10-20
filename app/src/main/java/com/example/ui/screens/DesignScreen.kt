package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.model.*
import com.example.ui.components.FlowStep
import com.example.ui.components.TbsBottomBar
import com.example.ui.components.TbsHeader
import com.example.ui.components.TbsStepBar
import com.example.ui.theme.*

@Composable
fun DesignScreen(
  selection: ClientSuitSelection,
  onLapelSelected: (LapelStyle) -> Unit,
  onButtonsSelected: (ButtonCount) -> Unit,
  onPocketsSelected: (PocketStyle) -> Unit,
  onVentsSelected: (VentStyle) -> Unit,
  onBack: () -> Unit,
  onContinue: () -> Unit,
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

    TbsStepBar(currentStep = FlowStep.DESIGN)

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 16.dp),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      Column {
        Text(
          text = "Tailoring & Design Details",
          fontFamily = CormorantGaramond,
          fontWeight = FontWeight.Bold,
          fontSize = 24.sp,
          color = TbsNavy
        )
        Text(
          text = "Configure lapels, front buttoning, pockets and rear vent craftsmanship.",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 13.sp,
          color = TbsCharcoalMuted,
          modifier = Modifier.padding(top = 4.dp)
        )
      }

      // 1. LAPEL: Notch, Peak, Shawl
      DesignSection(title = "LAPEL TYPE") {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          LapelStyle.values().forEach { lapel ->
            OptionPill(
              text = lapel.displayName,
              isSelected = lapel == selection.lapel,
              onClick = { onLapelSelected(lapel) },
              modifier = Modifier.weight(1f),
              testTag = "lapel_${lapel.name.lowercase()}"
            )
          }
        }
      }

      // 2. BUTTONS: 1, 2, 3
      DesignSection(title = "FRONT BUTTONS") {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ButtonCount.values().forEach { button ->
            OptionPill(
              text = button.displayName,
              isSelected = button == selection.buttons,
              onClick = { onButtonsSelected(button) },
              modifier = Modifier.weight(1f),
              testTag = "buttons_${button.promptValue}"
            )
          }
        }
      }

      // 3. POCKETS: Flap, Jetted, Patch
      DesignSection(title = "WAIST POCKETS") {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          PocketStyle.values().forEach { pocket ->
            OptionPill(
              text = pocket.displayName,
              isSelected = pocket == selection.pockets,
              onClick = { onPocketsSelected(pocket) },
              modifier = Modifier.weight(1f),
              testTag = "pocket_${pocket.name.lowercase()}"
            )
          }
        }
      }

      // 4. VENTS: None, Single, Double
      DesignSection(title = "REAR VENTS") {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          VentStyle.values().forEach { vent ->
            OptionPill(
              text = when (vent) {
                VentStyle.NONE -> "No Vents"
                VentStyle.SINGLE -> "Single Vent"
                VentStyle.DOUBLE -> "Double Vents"
              },
              isSelected = vent == selection.vents,
              onClick = { onVentsSelected(vent) },
              modifier = Modifier.weight(1f),
              testTag = "vent_${vent.name.lowercase()}"
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
    }

    TbsBottomBar(
      onBack = onBack,
      onContinue = onContinue,
      continueText = "CONTINUE TO COLORS"
    )
  }
}

@Composable
private fun DesignSection(
  title: String,
  content: @Composable () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(Color.White)
      .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
      .padding(16.dp)
  ) {
    Text(
      text = title,
      fontFamily = InterFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 11.sp,
      letterSpacing = 1.2.sp,
      color = TbsBrassGold,
      modifier = Modifier.padding(bottom = 12.dp)
    )
    content()
  }
}

@Composable
private fun OptionPill(
  text: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = ""
) {
  Box(
    modifier = modifier
      .height(48.dp)
      .clip(RoundedCornerShape(6.dp))
      .background(if (isSelected) TbsNavy else TbsIvory)
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) TbsNavy else TbsBorder,
        shape = RoundedCornerShape(6.dp)
      )
      .clickable { onClick() }
      .padding(horizontal = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      fontFamily = InterFontFamily,
      fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
      fontSize = 12.sp,
      lineHeight = 15.sp,
      color = if (isSelected) Color.White else TbsCharcoal,
      textAlign = TextAlign.Center,
      maxLines = 2
    )
  }
}
