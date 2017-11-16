package jp.toastkid.rxpermissionsexample

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import okio.Okio
import java.io.File

/**
 * @author toastkidjp
 */
object FileActions {

    /**
     * Save content to file.
     *
     * @param context
     * @param fileName
     */
    fun save(context: Context, fileName: String) {
        val file = assignFile(context, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        Okio.buffer(Okio.sink(file)).write("Hello".toByteArray()).flush()
        MediaScannerConnection.scanFile(
                context,
                arrayOf(fileName),
                null,
                { _, _ ->  }
        )
    }

    /**
     * Load content from file.
     *
     * @param context
     * @param fileName
     */
    fun load(context: Context, fileName: String): String {
        val file = assignFile(context, fileName)
        if (!file.exists()) {
            return ""
        }
        return Okio.buffer(Okio.source(file)).readUtf8()
    }

    /**
     * Assign file in environment download directory.
     *
     * @param context
     * @param fileName
     * @return [File]
     */
    private fun assignFile(context: Context, fileName: String): File {
        val externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if (!externalFilesDir.exists()) {
            externalFilesDir.mkdirs()
        }
        return File(externalFilesDir, fileName)
    }
}