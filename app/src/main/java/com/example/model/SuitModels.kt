package com.example.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class SuitStyle(val displayName: String, val subtitle: String, val iconDescription: String) {
  SINGLE_BREASTED("Single-Breasted", "Timeless & versatile, 1–3 buttons", "Classic silhouette for daily business & formal wear"),
  DOUBLE_BREASTED("Double-Breasted", "Broad peak lapel, sartorial power", "Commanding presence with overlapping front buttoning"),
  THREE_PIECE("3-Piece Suit", "Matching tailored waistcoat vest", "Epitome of sartorial elegance for gala & weddings"),
  TUXEDO("Black Tie Tuxedo", "Satin lapels & bow tie evening wear", "Dubai gala, red carpet & grand evening black-tie attire")
}

enum class LapelStyle(val displayName: String) {
  NOTCH("Notch Lapel"),
  PEAK("Peak Lapel"),
  SHAWL("Shawl Collar")
}

enum class ButtonCount(val displayName: String, val promptValue: String) {
  ONE("1 Button", "1"),
  TWO("2 Buttons", "2"),
  THREE("3 Buttons", "3")
}

enum class PocketStyle(val displayName: String) {
  FLAP("Flap Pockets"),
  JETTED("Jetted (Besom)"),
  PATCH("Patch Pockets")
}

enum class VentStyle(val displayName: String) {
  NONE("No Vents (Italian)"),
  SINGLE("Single Center Vent"),
  DOUBLE("Double Side Vents (British)")
}

data class ColorOption(
  val id: String,
  val name: String,
  val color: Color,
  val isNoTie: Boolean = false
)

object SuitColorPalettes {
  val jacketColors = listOf(
    ColorOption("navy", "Midnight Navy", SwatchMidnightNavy),
    ColorOption("charcoal", "Charcoal Grey", SwatchCharcoalGrey),
    ColorOption("mid_grey", "Mid Grey", SwatchMidGrey),
    ColorOption("black", "Black", SwatchBlack),
    ColorOption("forest", "Rich Forest", SwatchRichForest),
    ColorOption("camel", "Camel", SwatchCamel),
    ColorOption("beige", "Beige", SwatchBeige)
  )

  val trouserColors = jacketColors

  val shirtColors = listOf(
    ColorOption("white", "Crisp White", SwatchCrispWhite),
    ColorOption("sky_blue", "Pale Sky Blue", SwatchPaleSkyBlue),
    ColorOption("soft_pink", "Soft Pink", SwatchSoftPink),
    ColorOption("light_grey", "Light Grey", SwatchLightGrey),
    ColorOption("black", "Black", SwatchBlack)
  )

  val tieColors = listOf(
    ColorOption("burgundy", "Burgundy", SwatchBurgundy),
    ColorOption("navy", "Navy", SwatchMidnightNavy),
    ColorOption("silver", "Silver Grey", SwatchSilverGrey),
    ColorOption("gold", "Champagne Gold", SwatchChampagneGold),
    ColorOption("olive", "Olive", SwatchOlive),
    ColorOption("black", "Black", SwatchBlack),
    ColorOption("none", "No Tie", Color.Transparent, isNoTie = true)
  )

  val shoeColors = listOf(
    ColorOption("espresso", "Espresso Brown", SwatchEspressoBrown),
    ColorOption("oxford_black", "Oxford Black", SwatchOxfordBlack),
    ColorOption("chestnut", "Chestnut Tan", SwatchChestnutTan),
    ColorOption("burgundy_shoe", "Burgundy", SwatchBurgundy)
  )
}

data class ClientSuitSelection(
  val style: SuitStyle = SuitStyle.SINGLE_BREASTED,
  val lapel: LapelStyle = LapelStyle.NOTCH,
  val buttons: ButtonCount = ButtonCount.TWO,
  val pockets: PocketStyle = PocketStyle.FLAP,
  val vents: VentStyle = VentStyle.DOUBLE,
  val jacketColor: ColorOption = SuitColorPalettes.jacketColors[0],
  val matchJacketWithTrousers: Boolean = true,
  val trouserColor: ColorOption = SuitColorPalettes.jacketColors[0],
  val shirtColor: ColorOption = SuitColorPalettes.shirtColors[0],
  val tieColor: ColorOption = SuitColorPalettes.tieColors[0],
  val shoeColor: ColorOption = SuitColorPalettes.shoeColors[1]
) {
  fun effectiveTrouserColor(): ColorOption =
    if (matchJacketWithTrousers) jacketColor else trouserColor
}

data class ConsultantSuggestion(
  val title: String,
  val subtitle: String,
  val description: String,
  val selection: ClientSuitSelection
)

object ConsultantPresets {
  val suggestions = listOf(
    ConsultantSuggestion(
      title = "The Dubai Executive",
      subtitle = "Classic boardroom authority",
      description = "Midnight Navy single-breasted two-button with crisp white shirt, burgundy silk tie, and black oxfords.",
      selection = ClientSuitSelection(
        style = SuitStyle.SINGLE_BREASTED,
        lapel = LapelStyle.NOTCH,
        buttons = ButtonCount.TWO,
        pockets = PocketStyle.FLAP,
        vents = VentStyle.DOUBLE,
        jacketColor = SuitColorPalettes.jacketColors.first { it.id == "navy" },
        matchJacketWithTrousers = true,
        trouserColor = SuitColorPalettes.jacketColors.first { it.id == "navy" },
        shirtColor = SuitColorPalettes.shirtColors.first { it.id == "white" },
        tieColor = SuitColorPalettes.tieColors.first { it.id == "burgundy" },
        shoeColor = SuitColorPalettes.shoeColors.first { it.id == "oxford_black" }
      )
    ),
    ConsultantSuggestion(
      title = "Emirates Gala Tuxedo",
      subtitle = "Grand red carpet black-tie",
      description = "Peak or shawl black tuxedo with satin lapels, black bow tie, white evening shirt, and black patent shoes.",
      selection = ClientSuitSelection(
        style = SuitStyle.TUXEDO,
        lapel = LapelStyle.SHAWL,
        buttons = ButtonCount.ONE,
        pockets = PocketStyle.JETTED,
        vents = VentStyle.NONE,
        jacketColor = SuitColorPalettes.jacketColors.first { it.id == "black" },
        matchJacketWithTrousers = true,
        trouserColor = SuitColorPalettes.jacketColors.first { it.id == "black" },
        shirtColor = SuitColorPalettes.shirtColors.first { it.id == "white" },
        tieColor = SuitColorPalettes.tieColors.first { it.id == "black" },
        shoeColor = SuitColorPalettes.shoeColors.first { it.id == "oxford_black" }
      )
    ),
    ConsultantSuggestion(
      title = "Riviera Gentleman",
      subtitle = "Sophisticated Mediterranean chic",
      description = "Rich Forest jacket paired with refined beige trousers, sky blue shirt, champagne gold tie, and chestnut tan oxfords.",
      selection = ClientSuitSelection(
        style = SuitStyle.SINGLE_BREASTED,
        lapel = LapelStyle.PEAK,
        buttons = ButtonCount.TWO,
        pockets = PocketStyle.PATCH,
        vents = VentStyle.DOUBLE,
        jacketColor = SuitColorPalettes.jacketColors.first { it.id == "forest" },
        matchJacketWithTrousers = false,
        trouserColor = SuitColorPalettes.jacketColors.first { it.id == "beige" },
        shirtColor = SuitColorPalettes.shirtColors.first { it.id == "sky_blue" },
        tieColor = SuitColorPalettes.tieColors.first { it.id == "gold" },
        shoeColor = SuitColorPalettes.shoeColors.first { it.id == "chestnut" }
      )
    )
  )
}

data class GeneratedLook(
  val id: String,
  val label: String,
  val bitmap: Bitmap,
  val selection: ClientSuitSelection,
  val timestamp: Long = System.currentTimeMillis()
)
