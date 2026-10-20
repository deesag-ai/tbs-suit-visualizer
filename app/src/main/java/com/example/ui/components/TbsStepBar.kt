package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class FlowStep(val stepNumber: Int, val shortNumber: String, val stepName: String) {
  PHOTO(1, "01", "01 PHOTO"),
  STYLE(2, "02", "02 STYLE"),
  DESIGN(3, "03", "03 DESIGN"),
  COLORS(4, "04", "04 COLORS"),
  PREVIEW(5, "05", "05 PREVIEW")
}

@Composable
fun TbsStepBar(
  currentStep: FlowStep,
  modifier: Modifier = Modifier
) {
  val steps = FlowStep.values()

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(TbsIvory)
      .padding(vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp)
        .testTag("flow_step_bar"),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      steps.forEachIndexed { index, step ->
        val isCurrent = step == currentStep
        val isPast = step.stepNumber < currentStep.stepNumber

        // Show "01 · 02 · 03 · 04 · 05" with only current step's name shown, e.g. "01 PHOTO · 02 · 03 · 04 · 05"
        val label = if (isCurrent) step.stepName else step.shortNumber

        Text(
          text = label,
          fontFamily = InterFontFamily,
          fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
          fontSize = if (isCurrent) 10.5.sp else 10.sp,
          letterSpacing = 0.5.sp,
          maxLines = 1,
          softWrap = false,
          color = when {
            isCurrent -> TbsBrassGold
            isPast -> TbsNavy
            else -> TbsCharcoalMuted.copy(alpha = 0.5f)
          },
          modifier = Modifier.testTag("step_${step.name.lowercase()}")
        )

        if (index < steps.size - 1) {
          Text(
            text = " · ",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = TbsBorder,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.padding(horizontal = 4.dp)
          )
        }
      }
    }

    // Slim brass divider underneath
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp)
        .height(1.dp)
        .background(TbsBorder.copy(alpha = 0.5f))
    )
  }
}
