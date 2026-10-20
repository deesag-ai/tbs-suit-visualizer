package com.example.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.model.GeneratedLook
import java.io.File
import java.io.FileOutputStream

object WhatsAppHelper {

  /**
   * Normalises a raw phone number string to digits-only with country code.
   * Strips all non-digit characters; if the result starts with "00" it is
   * converted to the international prefix without leading zeros.
   * Examples:
   *   "+971 50 123 4567"  → "971501234567"
   *   "00971501234567"    → "971501234567"
   *   "0501234567"        → "0501234567"   (no country code detected – kept as-is)
   */
  fun normaliseNumber(raw: String): String {
    var digits = raw.filter { it.isDigit() }
    if (digits.startsWith("00")) {
      digits = digits.removePrefix("00")
    }
    return digits
  }

  fun formatConsultantMessage(clientName: String, look: GeneratedLook): String {
    val sel = look.selection
    // Use client name if entered, otherwise fall back to "Dear Sir,"
    val salutation = if (clientName.isNotBlank()) "Dear $clientName," else "Dear Sir,"
    return """
      $salutation
      
      Thank you for visiting *The Bespoke Suits (TBS) Dubai*.
      Here is your customized bespoke suit visualization:
      
      • Style: ${sel.style.displayName}
      • Lapel: ${sel.lapel.displayName}
      • Front: ${sel.buttons.displayName}
      • Pockets: ${sel.pockets.displayName}
      • Vents: ${sel.vents.displayName}
      • Jacket: ${sel.jacketColor.name}
      • Trousers: ${sel.effectiveTrouserColor().name}
      • Shirt: ${sel.shirtColor.name}
      • Tie: ${if (sel.tieColor.isNoTie) "No Tie (Open Collar)" else sel.tieColor.name}
      • Footwear: ${sel.shoeColor.name}
      
      Hand-crafted to your exact measurements at our Dubai atelier.
      
      _The Bespoke Suits · Dubai_
      _Excellence in Bespoke Tailoring_
    """.trimIndent()
  }

  /**
   * Sends the generated look image to a specific WhatsApp contact by JID.
   *
   * Strategy:
   *  1. Build a direct-chat Intent (ACTION_SEND) with jid = "<digits>@s.whatsapp.net"
   *     targeted to com.whatsapp.
   *  2. If com.whatsapp is not found, retry with com.whatsapp.w4b (WhatsApp Business).
   *  3. If neither is installed, show a Toast and fall back to the system share chooser
   *     (no browser URL ever opened).
   *
   * The cached image is NOT deleted here. The caller is responsible for calling
   * [deleteCachedImages] when the user returns to the app (onResume) or taps New Client.
   *
   * @return true if the intent was dispatched, false on unexpected error.
   */
  fun sendLookViaWhatsApp(
    context: Context,
    rawNumber: String,
    clientName: String,
    look: GeneratedLook
  ): Boolean {
    val text = formatConsultantMessage(clientName, look)
    val jid = "${normaliseNumber(rawNumber)}@s.whatsapp.net"

    // Clean up any leftover temp images before writing a new one
    deleteCachedImages(context)

    // Write the generated bitmap to a temporary JPEG in app cache
    val imageFile: File
    try {
      val cacheDir = context.cacheDir
      imageFile = File(cacheDir, "tbs_client_suit_${System.currentTimeMillis()}.jpg")
      FileOutputStream(imageFile).use { out ->
        look.bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
        out.flush()
      }
    } catch (e: Exception) {
      e.printStackTrace()
      return false
    }

    // Obtain a FileProvider content URI
    val contentUri = try {
      FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
      )
    } catch (e: Exception) {
      e.printStackTrace()
      imageFile.delete()
      return false
    }

    /**
     * Builds an ACTION_SEND intent for WhatsApp/WhatsApp Business that opens
     * the chat for a specific contact via the "jid" extra.
     */
    fun buildDirectIntent(packageName: String) = Intent(Intent.ACTION_SEND).apply {
      type = "image/jpeg"
      `package` = packageName
      putExtra(Intent.EXTRA_STREAM, contentUri)
      putExtra(Intent.EXTRA_TEXT, text)
      putExtra("jid", jid)
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    // 1. Try com.whatsapp
    val whatsApp = context.packageManager.getLaunchIntentForPackage("com.whatsapp")
    if (whatsApp != null) {
      context.startActivity(buildDirectIntent("com.whatsapp"))
      return true
    }

    // 2. Try com.whatsapp.w4b (WhatsApp Business)
    val whatsAppBiz = context.packageManager.getLaunchIntentForPackage("com.whatsapp.w4b")
    if (whatsAppBiz != null) {
      context.startActivity(buildDirectIntent("com.whatsapp.w4b"))
      return true
    }

    // 3. Neither installed – inform the consultant and fall back to system chooser
    Toast.makeText(
      context,
      "WhatsApp is not installed on this device",
      Toast.LENGTH_LONG
    ).show()

    val chooserIntent = Intent(Intent.ACTION_SEND).apply {
      type = "image/jpeg"
      putExtra(Intent.EXTRA_STREAM, contentUri)
      putExtra(Intent.EXTRA_TEXT, text)
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val chooser = Intent.createChooser(chooserIntent, "Share Bespoke Look").apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    return try {
      context.startActivity(chooser)
      true
    } catch (e: ActivityNotFoundException) {
      imageFile.delete()
      false
    }
  }

  fun deleteCachedImages(context: Context) {
    try {
      val cacheDir = context.cacheDir
      cacheDir.listFiles()?.forEach { file ->
        if (file.name.startsWith("tbs_client_suit_") && file.name.endsWith(".jpg")) {
          file.delete()
        }
      }
    } catch (_: Exception) {}
  }
}
