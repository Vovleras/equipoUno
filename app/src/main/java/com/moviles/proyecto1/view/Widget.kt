package com.moviles.proyecto1.view

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.moviles.proyecto1.R
import android.content.Intent
import android.app.PendingIntent
import com.moviles.proyecto1.repository.InventoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class Widget : AppWidgetProvider() {
    companion object{
        private const val BUTTON_EYE = "button_eye"
        private const val BUTTON_SETTINGS = "button_settings"
        private var isVisible: Boolean = false
        private const val KEY_WIDGET_ID = "appWidgetId"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)

        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            BUTTON_EYE -> {
                showButtonEye(context)
            }
        }
    }

    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ){
        val views = RemoteViews(context.packageName, R.layout.widget)

        // Alternar el ícono y la visibilidad del precio
        if (isVisible) {
            CoroutineScope(Dispatchers.IO).launch {
                val repository = InventoryRepository(context)
                val total = repository.calculateTotalInventory()
                val localeES = Locale.Builder().setLanguageTag("es-CO").build()

                withContext(Dispatchers.Main) {
                    views.setTextViewText(R.id.tvprice, String.format(localeES,"\$%,.2f", total))
                    views.setImageViewResource(R.id.iseye, R.drawable.closedeye)

                    views.setOnClickPendingIntent(
                        R.id.iseye,
                        actionButtonPendingIntent(context, BUTTON_EYE, appWidgetId)
                    )

                    views.setOnClickPendingIntent(
                        R.id.ivsets,
                        openMainActivityPendingIntent(context)
                    )

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        } else {
            views.setTextViewText(R.id.tvprice, context.getString(R.string.default_price_w))
            views.setImageViewResource(R.id.iseye, R.drawable.eye)

            views.setOnClickPendingIntent(
                R.id.iseye,
                actionButtonPendingIntent(context, BUTTON_EYE, appWidgetId)
            )

            views.setOnClickPendingIntent(
                R.id.ivsets,
                openMainActivityPendingIntent(context)
            )

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun actionButtonPendingIntent(
        context: Context,
        action: String,
        appWidgetId: Int
    ): PendingIntent {
        val intent = Intent(context, Widget::class.java)
        intent.action = action
        intent.putExtra(KEY_WIDGET_ID, appWidgetId)
        return PendingIntent.getBroadcast(
            context,
            appWidgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun openMainActivityPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showButtonEye(context: Context) {
        isVisible = !isVisible

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            android.content.ComponentName(context, Widget::class.java)
        )

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }



}