package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
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
fun ColorsScreen(
  selection: ClientSuitSelection,
  onJacketColorSelected: (ColorOption) -> Unit,
  onMatchJacketToggled: (Boolean) -> Unit,
  onTrouserColorSelected: (ColorOption) -> Unit,
  onShirtColorSelected: (ColorOption) -> Unit,
  onTieColorSelected: (ColorOption) -> Unit,
  onShoeColorSelected: (ColorOption) -> Unit,
  showConsultantSuggestions: Boolean,
  onShowSuggestionsChanged: (Boolean) -> Unit,
  onApplySuggestion: (ConsultantSuggestion) -> Unit,
  onBack: () -> Unit,
  onContinue: () -> Unit,
  modifier: Modifier = Modifier
) {
  val verticalScroll = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(TbsIvory)
  ) {
    TbsHeader(
      showBackArrow = true,
      onBackClick = onBack
    )

    TbsStepBar(currentStep = FlowStep.COLORS)

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .verticalScroll(verticalScroll)
        .padding(horizontal = 20.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Color Palette",
            fontFamily = CormorantGaramond,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = TbsNavy
          )
          Text(
            text = "Choose colors for suit, shirt, tie and shoes.",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.5.sp,
            color = TbsCharcoalMuted
          )
        }

        // Small outlined "Consultant Suggestions" button
        OutlinedButton(
          onClick = { onShowSuggestionsChanged(true) },
          shape = RoundedCornerShape(4.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = TbsBrassGold),
          border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
            brush = androidx.compose.ui.graphics.SolidColor(TbsBrassGold)
          ),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
          modifier = Modifier.testTag("consultant_suggestions_button")
        ) {
          Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = null,
            tint = TbsBrassGold,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "SUGGESTIONS",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            letterSpacing = 0.8.sp,
            color = TbsBrassGold
          )
        }
      }

      // 1. JACKET COLOR
      ColorPaletteRow(
        title = "JACKET COLOR",
        selectedOption = selection.jacketColor,
        options = SuitColorPalettes.jacketColors,
        onSelect = onJacketColorSelected,
        testTagPrefix = "jacket_color"
      )

      // 2. TROUSERS COLOR (Toggle "Match jacket" on its own dedicated separate line)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color.White)
          .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
          .padding(14.dp)
      ) {
        Text(
          text = "TROUSERS COLOR",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          letterSpacing = 1.2.sp,
          color = TbsBrassGold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = if (selection.matchJacketWithTrousers) "${selection.jacketColor.name} (Matched with jacket)" else selection.trouserColor.name,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Medium,
          fontSize = 13.sp,
          color = TbsNavy
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Match jacket toggle on its own dedicated line to prevent any overlap
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(TbsIvory.copy(alpha = 0.7f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Match jacket",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = TbsNavy
          )
          Switch(
            checked = selection.matchJacketWithTrousers,
            onCheckedChange = onMatchJacketToggled,
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = TbsNavy,
              uncheckedThumbColor = TbsCharcoalMuted,
              uncheckedTrackColor = TbsBorder
            ),
            modifier = Modifier.testTag("match_jacket_toggle")
          )
        }

        if (!selection.matchJacketWithTrousers) {
          Spacer(modifier = Modifier.height(12.dp))
          HorizontalSwatchList(
            options = SuitColorPalettes.trouserColors,
            selectedOption = selection.trouserColor,
            onSelect = onTrouserColorSelected,
            testTagPrefix = "trouser_color"
          )
        }
      }

      // 3. SHIRT COLOR
      ColorPaletteRow(
        title = "SHIRT (SPREAD COLLAR)",
        selectedOption = selection.shirtColor,
        options = SuitColorPalettes.shirtColors,
        onSelect = onShirtColorSelected,
        testTagPrefix = "shirt_color"
      )

      // 4. TIE COLOR
      ColorPaletteRow(
        title = "TIE (SILK)",
        selectedOption = selection.tieColor,
        options = SuitColorPalettes.tieColors,
        onSelect = onTieColorSelected,
        testTagPrefix = "tie_color"
      )

      // 5. SHOES COLOR
      ColorPaletteRow(
        title = "FOOTWEAR (OXFORD)",
        selectedOption = selection.shoeColor,
        options = SuitColorPalettes.shoeColors,
        onSelect = onShoeColorSelected,
        testTagPrefix = "shoe_color"
      )

      Spacer(modifier = Modifier.height(12.dp))
    }

    TbsBottomBar(
      onBack = onBack,
      onContinue = onContinue,
      continueText = "TAILOR MY LOOK"
    )

    // Consultant Suggestions Dialog / Sheet
    if (showConsultantSuggestions) {
      ConsultantSuggestionsDialog(
        suggestions = ConsultantPresets.suggestions,
        onApply = onApplySuggestion,
        onDismiss = { onShowSuggestionsChanged(false) }
      )
    }
  }
}

@Composable
private fun ColorPaletteRow(
  title: String,
  selectedOption: ColorOption,
  options: List<ColorOption>,
  onSelect: (ColorOption) -> Unit,
  testTagPrefix: String
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(Color.White)
      .border(1.dp, TbsBorder, RoundedCornerShape(8.dp))
      .padding(14.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = title,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        letterSpacing = 1.2.sp,
        color = TbsBrassGold
      )
      Text(
        text = selectedOption.name,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        color = TbsNavy
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    HorizontalSwatchList(
      options = options,
      selectedOption = selectedOption,
      onSelect = onSelect,
      testTagPrefix = testTagPrefix
    )
  }
}

@Composable
private fun HorizontalSwatchList(
  options: List<ColorOption>,
  selectedOption: ColorOption,
  onSelect: (ColorOption) -> Unit,
  testTagPrefix: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    options.forEach { option ->
      val isSelected = option.id == selectedOption.id
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .clickable { onSelect(option) }
          .padding(vertical = 4.dp)
          .testTag("${testTagPrefix}_${option.id}")
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(if (option.isNoTie) Color(0xFFF0EBE1) else option.color)
            .border(
              width = if (isSelected) 2.5.dp else 1.dp,
              color = if (isSelected) TbsBrassGold else TbsBorder,
              shape = CircleShape
            ),
          contentAlignment = Alignment.Center
        ) {
          if (option.isNoTie) {
            Icon(
              imageVector = Icons.Default.Block,
              contentDescription = "No Tie",
              tint = TbsCharcoalMuted,
              modifier = Modifier.size(20.dp)
            )
          } else if (isSelected) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = if (option.color == SwatchCrispWhite || option.color == SwatchBeige) TbsNavy else Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = option.name,
          fontFamily = InterFontFamily,
          fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
          fontSize = 10.sp,
          color = if (isSelected) TbsNavy else TbsCharcoalMuted,
          textAlign = TextAlign.Center,
          maxLines = 1
        )
      }
    }
  }
}

@Composable
fun ConsultantSuggestionsDialog(
  suggestions: List<ConsultantSuggestion>,
  onApply: (ConsultantSuggestion) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Lightbulb,
          contentDescription = null,
          tint = TbsBrassGold,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Consultant Combinations",
          fontFamily = CormorantGaramond,
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp,
          color = TbsNavy
        )
      }
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "Three classic house pairings crafted by our head bespoke consultants:",
          fontFamily = InterFontFamily,
          fontSize = 12.5.sp,
          color = TbsCharcoalMuted
        )

        suggestions.forEach { suggestion ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(6.dp))
              .background(TbsIvory)
              .border(1.dp, TbsBorder, RoundedCornerShape(6.dp))
              .clickable { onApply(suggestion) }
              .padding(12.dp)
              .testTag("apply_suggestion_${suggestion.title.lowercase().replace(" ", "_")}")
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = suggestion.title,
                  fontFamily = CormorantGaramond,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = TbsNavy
                )
                Text(
                  text = "APPLY",
                  fontFamily = InterFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp,
                  letterSpacing = 1.0.sp,
                  color = TbsBrassGold
                )
              }
              Text(
                text = suggestion.subtitle,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = TbsBrassGold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = suggestion.description,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = TbsCharcoal
              )
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text(
          text = "CLOSE",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          color = TbsNavy
        )
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(8.dp)
  )
}
