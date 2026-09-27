package com.ramitsuri.mydogs.widget

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream

object WidgetDefinition : GlanceStateDefinition<WidgetState> {
    override suspend fun getDataStore(
        context: Context,
        fileKey: String,
    ): DataStore<WidgetState> {
        return DataStoreFactory.create(
            serializer = WidgetStateSerializer,
            produceFile = { getLocation(context, fileKey) },
        )
    }

    override fun getLocation(
        context: Context,
        fileKey: String,
    ): File {
        return context.dataStoreFile(DATA_STORE_FILENAME + fileKey.lowercase())
    }

    object WidgetStateSerializer : Serializer<WidgetState> {
        override val defaultValue = WidgetState()

        override suspend fun readFrom(input: InputStream): WidgetState =
            try {
                Json.decodeFromString(
                    WidgetState.serializer(),
                    input.readBytes().decodeToString(),
                )
            } catch (exception: SerializationException) {
                Log.v(TAG, "Could not read widget state: ${exception.message}")
                WidgetState()
            }

        override suspend fun writeTo(
            t: WidgetState,
            output: OutputStream,
        ) {
            output.use {
                it.write(
                    Json.encodeToString(WidgetState.serializer(), t).encodeToByteArray(),
                )
            }
        }
    }

    private const val TAG = "WidgetDefinition"
    private const val DATA_STORE_FILENAME = "widget_state-"
}
