package com.example.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.example.model.ClientSuitSelection
import com.example.model.SuitStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class GeminiSuitService {

  private val client: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

  companion object {
    private const val TAG = "GeminiSuitService"
    private const val MODEL_NAME = "gemini-2.5-flash-image"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"
  }

  /**
   * Generates tailored look directly from client's actual photo using Gemini image model (image editing).
   * Sends the full original photo without cropping + prompt with selected style, details and colors.
   * If generation fails, returns Result.failure with descriptive error so UI can display "Try Again".
   * Never uses sample, placeholder or stock images.
   */
  suspend fun editSuitPhoto(
    clientBitmap: Bitmap,
    selection: ClientSuitSelection
  ): Result<Bitmap> = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    val prompt = buildTailorPrompt(selection)

    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext Result.failure(
        Exception("Gemini API key is not configured. Please configure your key to tailor looks with Gemini.")
      )
    }

    try {
      val base64Image = bitmapToBase64(clientBitmap)
      val jsonBody = JSONObject().apply {
        val contentsArray = JSONArray().apply {
          val contentObj = JSONObject().apply {
            val partsArray = JSONArray().apply {
              // 1. Precise Tailoring Text Prompt
              put(JSONObject().apply {
                put("text", prompt)
              })
              // 2. Full Client Photo
              put(JSONObject().apply {
                put("inlineData", JSONObject().apply {
                  put("mimeType", "image/jpeg")
                  put("data", base64Image)
                })
              })
            }
            put("parts", partsArray)
          }
          put(contentObj)
        }
        put("contents", contentsArray)

        put("generationConfig", JSONObject().apply {
          put("responseModalities", JSONArray().apply {
            put("TEXT")
            put("IMAGE")
          })
          put("imageConfig", JSONObject().apply {
            put("aspectRatio", "3:4")
          })
        })
      }

      val requestUrl = "$BASE_URL$MODEL_NAME:generateContent?key=$apiKey"
      val request = Request.Builder()
        .url(requestUrl)
        .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
        .build()

      val response = client.newCall(request).execute()
      val responseBodyString = response.body?.string() ?: ""

      if (!response.isSuccessful) {
        val errorMessage = try {
          val json = JSONObject(responseBodyString)
          val errorObj = json.optJSONObject("error")
          val msg = errorObj?.optString("message") ?: ""
          if (response.code == 429) {
            "Gemini image editing quota exceeded. Please check your plan or try again in a moment."
          } else if (msg.isNotEmpty()) {
            "Gemini service: $msg"
          } else {
            "Unable to tailor look with Gemini (code ${response.code})."
          }
        } catch (e: Exception) {
          "Unable to tailor look with Gemini (code ${response.code})."
        }
        Log.w(TAG, "Gemini call status ${response.code}: $errorMessage")
        return@withContext Result.failure(Exception(errorMessage))
      }

      val parsedResponse = JSONObject(responseBodyString)
      val candidates = parsedResponse.optJSONArray("candidates")
      if (candidates == null || candidates.length() == 0) {
        return@withContext Result.failure(Exception("Gemini returned empty response. Please tap Try Again."))
      }

      val firstCandidate = candidates.getJSONObject(0)
      val content = firstCandidate.optJSONObject("content")
      val parts = content?.optJSONArray("parts")

      var imageBitmap: Bitmap? = null
      if (parts != null) {
        for (i in 0 until parts.length()) {
          val part = parts.getJSONObject(i)
          val inlineData = part.optJSONObject("inlineData")
          if (inlineData != null) {
            val base64Data = inlineData.optString("data")
            if (base64Data.isNotEmpty()) {
              val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
              imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
              break
            }
          }
        }
      }

      if (imageBitmap != null) {
        Result.success(imageBitmap)
      } else {
        Result.failure(Exception("Gemini did not return an edited suit image. Please tap Try Again."))
      }
    } catch (e: Exception) {
      Log.w(TAG, "Gemini call exception: ${e.message}")
      Result.failure(Exception(e.message ?: "Failed to connect to Gemini. Please tap Try Again."))
    }
  }

  fun buildTailorPrompt(selection: ClientSuitSelection): String {
    val styleDesc = when (selection.style) {
      SuitStyle.SINGLE_BREASTED -> "Single-Breasted"
      SuitStyle.DOUBLE_BREASTED -> "Double-Breasted"
      SuitStyle.THREE_PIECE -> "3-Piece"
      SuitStyle.TUXEDO -> "Tuxedo"
    }

    val lapelDesc = "${selection.lapel.displayName.lowercase()} lapel"
    val buttonsDesc = "${selection.buttons.promptValue} button(s)"
    val pocketsDesc = "${selection.pockets.displayName.lowercase()} pockets"
    val ventsDesc = "${selection.vents.displayName.lowercase()} vents"
    val jacketColorDesc = selection.jacketColor.name
    val trouserColorDesc = selection.effectiveTrouserColor().name
    val shirtColorDesc = selection.shirtColor.name

    val tieDesc = if (selection.style == SuitStyle.TUXEDO) {
      "black bow tie"
    } else if (selection.tieColor.isNoTie) {
      "open collar, no tie"
    } else {
      "${selection.tieColor.name} silk, classic width"
    }

    val shoeDesc = if (selection.style == SuitStyle.TUXEDO) {
      "black patent leather shoes"
    } else {
      val colorName = selection.shoeColor.name
      if (selection.shoeColor.id == "oxford_black") {
        "$colorName leather oxford shoes"
      } else {
        "$colorName leather oxford shoes — must be exactly this color, not black"
      }
    }

    return """
      Edit this photo. Keep the person's face, skin tone, hair, beard, body shape, height, pose and background EXACTLY the same — do not change his identity in any way.
      Change ONLY his clothing to:
      Suit: $styleDesc, $buttonsDesc, $lapelDesc, $pocketsDesc, $ventsDesc
      Jacket color: $jacketColorDesc; Trousers color: $trouserColorDesc
      Shirt: $shirtColorDesc, spread collar
      Tie: $tieDesc
      Shoes: $shoeDesc
    """.trimIndent()
  }

  private fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    // Retain full original image resolution without downsampling or cropping
    bitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
  }
}
