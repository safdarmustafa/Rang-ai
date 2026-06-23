package com.example.rangai.auth

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/**
 * Listens for OTP SMS via Google SMS Retriever (zero-tap when hash matches)
 * and SMS User Consent (one-tap allow when SMS arrives).
 */
@Composable
fun OtpSmsConsentEffect(
  onOtpDetected: (String) -> Unit
) {
  val context = LocalContext.current
  val onOtpDetectedState = rememberUpdatedState(onOtpDetected)

  val consentLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
      val message = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
      val otp = SmsOtpParser.extractOtp(message)
      Log.d(TAG, "SMS consent granted — otpFound=${otp != null}")
      otp?.let { onOtpDetectedState.value(it) }
    }
    restartSmsListeners(context)
  }

  val receiver = remember {
    object : BroadcastReceiver() {
      override fun onReceive(ctx: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION != intent.action) return

        val extras = intent.extras ?: return
        val status = extras.get(SmsRetriever.EXTRA_STATUS) as? Status ?: return

        when (status.statusCode) {
          CommonStatusCodes.SUCCESS -> {
            val directMessage = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
            if (!directMessage.isNullOrBlank()) {
              val otp = SmsOtpParser.extractOtp(directMessage)
              Log.d(TAG, "SMS retriever — otpFound=${otp != null}")
              if (otp != null) {
                onOtpDetectedState.value(otp)
                restartSmsListeners(ctx)
              }
              return
            }

            val consentIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
            try {
              consentIntent?.let { consentLauncher.launch(it) }
            } catch (e: Exception) {
              Log.e(TAG, "Failed to launch SMS consent", e)
            }
          }
          else -> Log.w(TAG, "SMS retriever status = ${status.statusCode}")
        }
      }
    }
  }

  DisposableEffect(Unit) {
    restartSmsListeners(context)

    val filter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      ContextCompat.registerReceiver(
        context,
        receiver,
        filter,
        ContextCompat.RECEIVER_EXPORTED
      )
    } else {
      @Suppress("UnspecifiedRegisterReceiverFlag")
      context.registerReceiver(receiver, filter)
    }

    onDispose {
      try {
        context.unregisterReceiver(receiver)
      } catch (e: Exception) {
        Log.w(TAG, "Receiver already unregistered", e)
      }
    }
  }
}

private fun restartSmsListeners(context: Context) {
  val client = SmsRetriever.getClient(context)
  client.startSmsRetriever()
    .addOnSuccessListener { Log.d(TAG, "SMS Retriever started") }
    .addOnFailureListener { Log.e(TAG, "SMS Retriever start failed", it) }
  client.startSmsUserConsent(null)
    .addOnSuccessListener { Log.d(TAG, "SMS User Consent started") }
    .addOnFailureListener { Log.e(TAG, "SMS User Consent start failed", it) }
}

private const val TAG = "OTP_SMS"
