package com.eco.virtuRun.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Ignore
import com.eco.virtuRun.data.model.AppSettings
import com.eco.virtuRun.data.model.initAppSettings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Serializable
abstract class Persistable<T: Any> { //(@Ignore private val initialValues: T, @Ignore val persistableRepository: PersistableRepository<T>) {
    @Ignore private lateinit var persistableRepository: PersistableRepository<T>
    @Ignore private lateinit var settings: T
    operator fun invoke(initialValues: T, persistableRepository: PersistableRepository<T>) : T {
        this.persistableRepository = persistableRepository
        settings = persistableRepository.load() ?: initialValues
        return settings
    }
    fun save(): T = persistableRepository.save(settings)
}
/*
@Serializable
data class TestSettings
constructor(
    // Active Setting
    @EncodeDefault var userName: String = "System",

    ) : Persistable<TestSettings>(TestSettings(), PersistableFileRepository<TestSettings>(context, "testSettings.json", TestSettings.serializer())) {
        override fun save() = persistableRepository.save(this)
    }

    //@Transient override lateinit var repository: PersistableRepository<TestSettings>

    //override fun TestSettings.setRepository (repository: PersistableRepository<TestSettings>): TestSettings {
    //    this.repository = repository; return this }
    //override fun TestSettings.save() = repository.save(this)
/*
    @OptIn(ExperimentalSerializationApi::class)
    override fun TestSettings.decodeFromStream(inputStream: InputStream, json: Json): TestSettings =
        json.decodeFromStream<TestSettings>(inputStream)
    @OptIn(ExperimentalSerializationApi::class)
    override fun TestSettings.encodeToStream(outputStream: OutputStream, json: Json) =
        json.encodeToStream<TestSettings>(this, outputStream)

 */
    //override fun TestSettings.initialize():TestSettings = TestSettings()

    // Calculates the heart rate range for the user given the desired workout intensity
//}

 */

//@SuppressLint("StaticFieldLeak") val repo = PersistableFileRepository<TestSettings>(context, "appSetting.json", TestSettings.serializer())
//val test = Persistable<TestSettings>(repo).load()
//fun tst() { test.save() }



//fun <T> T.setRepository(persistableRepository: PersistableRepository<T>): T { repository = persistableRepository; return this }
//fun <T, U: Persistable<T>> U.setRepository (persistableRepository: PersistableRepository<T>) { this.repository = persistableRepository }


interface PersistableRepository<T> {
    fun save(settings: T): T
    fun load(): T?
}

class PersistableFileRepository<T>(private val context: Context, private val name: String,
                                         private val serializer: KSerializer<T>
    //private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PersistableRepository<T> {
    private val file = File(context.getExternalFilesDir(null).toString() + "/$name")

    init { Log.d("VRUN", "PersistableFileRepository: $name") }
    //private inline fun <reified T : Any> instantiate(): T = T::class.createInstance()

    @OptIn(ExperimentalSerializationApi::class)
    fun loadInitialSettings(): T? {
        //if (!cachedImagesDirectory.mkdirs()) Log.e("VRUN", "unable to create image cache directory")
        return try {
            Json.decodeFromStream(serializer, context.assets.open(name))
        } catch (e: SerializationException) { //JsonDecodingException
            Log.e("VRUN", "initial app settings corrupt")
            return null
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun load(): T? {
        Log.d("VRUN", "loadAppSettings")
        if (file.exists()) {
            try { return Json.decodeFromStream(serializer, FileInputStream(file)) }
            catch (e: SerializationException) { //JsonDecodingException
                Log.w("VRUN", "PersistableFileRepository: saved settings corrupt for $name")
                val json = Json { ignoreUnknownKeys = true }
                try {
                    return json.decodeFromStream(serializer, FileInputStream(file))
                    // try to upgrade
                } catch (e: Exception) { //JsonDecodingException
                    Log.e("VRUN", "saved app settings can not be recovered, re-initializing")
                }
            }
        }
        return loadInitialSettings()
    }
    //override fun load(): AppSettings = runBlocking { return@runBlocking loadSettings() }

    @OptIn(ExperimentalSerializationApi::class)
    override fun save(settings: T): T {
        Json.encodeToStream(serializer, settings, FileOutputStream(file))
        return settings
    }
}

