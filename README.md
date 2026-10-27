# Biblia Android — 1.1.2

An offline Spanish Bible app using Kotlin, Jetpack Compose and Material 3.

## Improvements

- Warm paper backgrounds, forest green accents, serif reading text, dark mode, correct edge-to-edge insets and large touch controls.
- Home screen with a deterministic daily verse, resume reading and canonical book order.
- Book filtering by name and Old/New Testament.
- Chapter picker, previous/next navigation and automatic scroll to a search result's verse.
- Full offline text search with accent/case-insensitive matching of every entered word; results run off the UI thread with a short debounce.
- Tap a verse to save it, share it with Android's share sheet or attach a reflection.
- Saved verse list and notes with edit, open passage and confirmed deletion.
- Persistent chapter completion, last book/chapter, 16–30 text size and system/light/dark appearance.
- Original Bible text and existing `bible_notes_v1.db` preserved. Internal book keys remain unchanged; displayed names use conventional numbering and accents.

## Build

Use Android Studio with **JDK 17** and **Android SDK 35**. Let Gradle sync download the dependencies.
The package includes the previously missing Gradle wrapper; Gradle 8.9, AGP 8.7.3 and Kotlin/Compose plugin 2.0.21 are pinned.
Configure the SDK through Android Studio, `ANDROID_HOME`, or a local `local.properties` file (`sdk.dir=/your/sdk`).

```bash
chmod +x gradlew
./gradlew testDebugUnitTest lintDebug assembleDebug
# With an Android device/emulator attached:
./gradlew connectedDebugAndroidTest
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.
Minimum Android version is 11, as in the supplied project. The application ID is unchanged.
To retain installed user data, sign updates with the same signing key as the installed app.

## Structure

- `reader/BibleApp.kt`: app shell, home, search, saved items and settings.
- `reader/ReaderScreen.kt`: reader, chapter picker and note editor.
- `reader/ReadingStore.kt`: cached scripture data, search, labels and persistent reading preferences.
- `reader/NoteRepository.kt`: asynchronous CRUD using the existing SQLite notes database and stable note IDs.
- `Data/BibleBooks`: unmodified offline Bible text.
- `Screen`: legacy screens retained for reference; the new main activity uses `reader`.
- `res/values/strings.xml`: new interface text in Spanish.

## Verification

Performed for this delivery:
- Compiled the scripture/search/preferences source against an Android API jar using the available Kotlin compiler.
- Ran data/search smoke checks: 66 books, 1189 chapters, 31103 unique verse keys, canonical name mapping, accent-insensitive multiword search, empty and no-result queries.
- Checked Kotlin syntax, string references, XML and ZIP integrity.

A full APK build and emulator UI review could not be performed in the delivery environment: network access to the Gradle distribution/dependency hosts is unavailable. The included unit tests, SQLite compatibility instrumentation test, and GitHub Actions workflow provide the remaining checks. No prebuilt APK/AAB is included.

## Manual UI checks

1. Rotate while reading; use the chapter picker and navigate the first/last chapters.
2. Search `principio creó`, open Génesis 1:1 and verify the targeted verse is visible.
3. Save a verse, write a note, restart, then reopen both from Guardados.
4. Edit a note and cancel/confirm its deletion. Check an existing note after installing an update with the same signing key.
5. Change theme and text size, restart and verify persistence. Test large system font and landscape.
6. Mark a chapter read and unread; verify home progress and resume reading after restart.
7. Share a verse through the Android share sheet.

The daily verse is a date-based selection from the included text. Progress advances when a chapter is explicitly marked read; it does not infer reading from scrolling.

## Notes and appearance update

The reader and Saved screen have a visible Add Note button. The note editor supports choosing a book, chapter and verse, with available values derived from the offline corpus. Blank notes cannot be saved and controls are disabled during saving. The top toolbar exposes System/Light/Dark appearance selection on every screen; Settings uses the same accessible radio choices. Theme changes apply immediately and persist after restart.

## Búsqueda desde el menú (1.1.2)

Abre el menú ⋮ de la barra superior y selecciona **Buscar en la Biblia**. Escribe al menos dos letras del texto de un versículo. La búsqueda recorre los 66 libros sin conexión, ignora mayúsculas y tildes y permite varias palabras. Los resultados muestran libro, capítulo, versículo y texto. Al tocar uno, se abre el capítulo y se desplaza al versículo. La consulta se conserva al volver de la lectura y al girar la pantalla. La pestaña Buscar sigue disponible.

La prueba de interfaz `SearchMenuTest` cubre la entrada desde el menú, la consulta y la apertura de Génesis 1:1; se incluye para ejecución en dispositivo/emulador.

Verificación de esta actualización: referencias de strings, XML y wrapper correctos. El intento `./gradlew testDebugUnitTest assembleDebug --no-daemon` quedó bloqueado al descargar Gradle (Network is unreachable); la prueba nueva de interfaz no se ejecutó en este entorno.
