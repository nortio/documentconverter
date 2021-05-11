package io.github.nortio.documentconverter


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi


const val htmlHead =
    "<!DOCTYPE html><html lang=\"en\"><head><title>Hello, world!</title><meta charset=\"UTF-8\" /><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\" /><meta name=\"description\" content=\"\" /><style></style></head>"
const val PREVIEWHTML = "io.github.nortio.documentconverter.PREVIEWHTML"
const val DATATOSAVE = "io.github.nortio.documentconverter.DATATOSAVE"


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private val converter = Converter();

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        val opzioneGroup = findViewById<RadioGroup>(R.id.opzioneScelta)
        val opzioneId = opzioneGroup.checkedRadioButtonId
        if (opzioneId!=-1){

            val radio: RadioButton = findViewById(opzioneId)
            Toast.makeText(applicationContext,"Opzione scelta:" +
                    " ${radio.text}",
                Toast.LENGTH_SHORT).show()

            val campoDiTesto = findViewById<TextView>(R.id.testoConvertito)

            if (requestCode == 111 && resultCode == RESULT_OK) {
                val selectedFile = intent?.data //The uri with the location of the file


                val campoDiTestoOriginale = findViewById<TextView>(R.id.testoDelFile)
                val markdownFile = selectedFile?.let { contentResolver.openInputStream(it) }
                if (markdownFile != null) {
                    val testoOriginale = markdownFile.bufferedReader().use { it.readText() }
                    campoDiTesto.text = converter.markdownToHtml( testoOriginale)
                    campoDiTestoOriginale.text = testoOriginale

                } else {
                    Toast.makeText(applicationContext, "Errore nell'apertura del file: il documento potrebbe non esistere", Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == 112 && resultCode == RESULT_OK) {

                if (intent != null) {
                    val dataToBeSaved = campoDiTesto.text.toString()
                    val uri = intent.data
                    println("URI: $uri")
                    println("SALVATAGGIO FILE: $dataToBeSaved")

                    uri?.let { contentResolver.openOutputStream(it) }
                        ?.write(dataToBeSaved.toByteArray())

                } else {
                    println("DATA NULL")
                }
            }
        } else {
            Toast.makeText(applicationContext,"Non è stata scelta nessuna opzione",
                Toast.LENGTH_SHORT).show()
        }

    }


    fun previewHtml(view: View) {
        val campoDiTesto = findViewById<TextView>(R.id.testoConvertito)


        val intent = Intent(this, Preview::class.java).apply {
            putExtra(PREVIEWHTML, campoDiTesto.text.toString())
        }
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun saveFile(view: View) {
        val intent = Intent()
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType("text/html")
            .setAction(Intent.ACTION_CREATE_DOCUMENT)
        startActivityForResult(Intent.createChooser(intent, "Select a place to save the file"), 112)
    }

    fun pickFile(view: View) {
        val intent = Intent()
            .setType("text/markdown")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
    }
}