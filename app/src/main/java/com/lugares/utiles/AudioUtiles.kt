package com.lugares.utiles

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import  android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.isc.lugares.utiles.OtrosUtiles
import com.lugares.R
import java.io.File
import java.io.IOException
import java.lang.IllegalStateException



class AudioUtiles(private  val actividad: Activity,
                  private  val contexto: Context,
                  private  val btAccion: ImageButton,
                  private  val btPlay: ImageButton,
                  private  val btDelete: ImageButton,
                  private  val msgIniciaNotaAudio: String,
                  private  val msgDetenerNotaAudio: String) {


    init {
        btAccion.setOnClickListener { grabarStop() }
        btPlay.setOnClickListener { playNota() }
        btDelete.setOnClickListener { borrarNota() }
        btPlay.isEnabled = false
        btDelete.isEnabled= false
    }

    private  var mediorecorder: MediaRecorder? = null
    private  var grabando: Boolean = false
    var audioFile : File =  File.createTempFile("audio_",".mp3")



    private fun grabarStop(){
        if (ContextCompat.checkSelfPermission(contexto, android.Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED){
            val permission = arrayOf(android.Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(actividad,permission,0)
        }else{
            grabando = if(!grabando){
                mediaRecorderInit()
                iniciaGrabando()
                true

            }else{
                detenerNota()
                false
            }

        }

    }

    private fun detenerNota() {
btPlay.isEnabled=true
        btDelete.isEnabled = true
        mediorecorder?.stop()
        mediorecorder?.release()
        Toast.makeText(contexto,msgDetenerNotaAudio, Toast.LENGTH_SHORT).show()
btAccion.setImageResource(R.drawable.ic_mic)
    }

    private fun iniciaGrabando() {
try{
    mediorecorder?.prepare()
    mediorecorder?.start()
    Toast.makeText(contexto, msgIniciaNotaAudio, Toast.LENGTH_SHORT).show()
    btAccion.setImageResource(R.drawable.ic_stop)
    btPlay.isEnabled = false
    btDelete.isEnabled = false
}catch (e: IllegalStateException){
    e.printStackTrace()
}catch (e: IOException){
    e.printStackTrace()
}

    }



    private fun mediaRecorderInit() {
if(audioFile.exists() && audioFile.isFile){
    audioFile.delete()
}
        var archivo = OtrosUtiles.getTempFile("audio_")
        audioFile = File.createTempFile(archivo,".mp3")
        mediorecorder = MediaRecorder()
        mediorecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediorecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediorecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediorecorder!!.setOutputFile(audioFile)
        }
    }

    private  fun playNota(){
        if(audioFile.exists() && audioFile.canRead()){
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(audioFile.path)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }


    }

    private fun borrarNota(){
        if(audioFile.exists() && audioFile.canRead()){
           audioFile.delete()
            btPlay.isEnabled =false
            btDelete.isEnabled =false
        }



    }
}