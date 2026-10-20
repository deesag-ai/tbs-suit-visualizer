package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SuitStyle
import com.example.ui.components.FlowStep
import com.example.ui.components.TbsBottomBar
import com.example.ui.components.TbsHeader
import com.example.ui.components.TbsStepBar
import com.example.ui.theme.*

@Composable
fun StyleScreen(
  selectedStyle: SuitStyle,
  onStyleSelected: (SuitStyle) -> Unit,
  onBack: () -> Unit,
  onContinue: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()
  val styles = SuitStyle.values()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(TbsIvory)
  ) {
    TbsHeader(
      showBackArrow = true,
      onBackClick = onBack
    )

    TbsStepBar(currentStep = FlowStep.STYLE)

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      Text(
        text = "Select Suit Silhouette",
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = TbsNavy
      )

      Text(
        text = "Choose the foundational cut for the client's bespoke commission.",
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        color = TbsCharcoalMuted,
        modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
      )

      // 2-column grid of 4 cards
      Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        for (i in 0 until styles.size step 2) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            val styleA = styles[i]
            StyleCard(
              style = styleA,
              isSelected = styleA == selectedStyle,
              onSelect = { onStyleSelected(styleA) },
              modifier = Modifier.weight(1f)
            )

            if (i + 1 < styles.size) {
              val styleB = styles[i + 1]
              StyleCard(
                style = styleB,
                isSelected = styleB == selectedStyle,
                onSelect = { onStyleSelected(styleB) },
                modifier = Modifier.weight(1f)
              )
            } else {
              Spacer(modifier = Modifier.weight(1f))
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Selected Style Details Info Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color.White)
          .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
          .padding(16.dp)
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(TbsBrassGold)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "HOUSE CUT SPECIFICATION",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              letterSpacing = 1.0.sp,
              color = TbsBrassGold
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = selectedStyle.iconDescription,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = TbsCharcoal
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    TbsBottomBar(
      onBack = onBack,
      onContinue = onContinue,
      continueText = "CONTINUE TO DESIGN"
    )
  }
}

@Composable
private fun StyleCard(
  style: SuitStyle,
  isSelected: Boolean,
  onSelect: () -> Unit,
  modifier: Modifier = Modifier
) {
  val borderColor = if (isSelected) TbsBrassGold else TbsBorder
  val bgColor = if (isSelected) TbsBrassGoldContainer.copy(alpha = 0.45f) else Color.White

  Box(
    modifier = modifier
      .height(160.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(bgColor)
      .border(if (isSelected) 1.8.dp else 1.dp, borderColor, RoundedCornerShape(8.dp))
      .clickable { onSelect() }
      .padding(14.dp)
      .testTag("style_card_${style.name.lowercase()}"),
    contentAlignment = Alignment.TopStart
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        // Tag / Icon
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) TbsNavy else TbsNavy.copy(alpha = 0.08f)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = when (style) {
              SuitStyle.SINGLE_BREASTED -> "SB"
              SuitStyle.DOUBLE_BREASTED -> "DB"
              SuitStyle.THREE_PIECE -> "3P"
              SuitStyle.TUXEDO -> "TX"
            },
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (isSelected) TbsBrassGold else TbsNavy
          )
        }

        // Selection radio/check indicator
        Box(
          modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(if (isSelected) TbsBrassGold else Color.Transparent)
            .border(
              1.2.dp,
              if (isSelected) TbsBrassGold else TbsBorder,
              CircleShape
            ),
          contentAlignment = Alignment.Center
        ) {
          if (isSelected) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }

      Column {
        Text(
          text = style.displayName,
          fontFamily = CormorantGaramond,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          lineHeight = 20.sp,
          color = TbsNavy
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = style.subtitle,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 11.5.sp,
          lineHeight = 15.sp,
          color = TbsCharcoalMuted
        )
      }
    }
  }
}
