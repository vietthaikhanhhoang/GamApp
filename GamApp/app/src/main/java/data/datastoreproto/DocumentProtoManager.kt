package data.datastoreproto

import android.content.Context
import android.util.Log
import androidx.datastore.CorruptionException
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import model.PArticle
import model.PListingResponse
import okhttp3.internal.notifyAll
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DocumentProtoManager(context: Context, fileName: String) {
    private val dataStore: DataStore<PListingResponse.Document> =
        context.createDataStore(
        fileName = "$fileName.pb",
        serializer = DocumentProtoSerializer
    )
    
    val documentProto = dataStore.data.catch {
        if (it is IOException) {
            Log.e(TAG, "Error reading sort order preferences.", it)
            emit(PListingResponse.Document.getDefaultInstance())
        } else {
            throw it
        }
    }.map {
        it
    }

    suspend fun updateArt(art: PArticle.ArticleMsg?) {
        dataStore.updateData { it ->
            it.toBuilder()
                .setArt(art)
                .build()
        }
    }

    companion object {
        const val TAG = "DocumentProtoManager"
    }
}

object DocumentProtoSerializer : Serializer<PListingResponse.Document> {
    override fun readFrom(input: InputStream): PListingResponse.Document {
        try {
            return PListingResponse.Document.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: PListingResponse.Document, output: OutputStream) = t.writeTo(output)
}