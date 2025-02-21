package com.example.mykeyboard

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream

class ModelHelper(private val context: Context) {
    private var interpreter: Interpreter? = null

    init {
        loadModel()
    }
    
    private fun loadModel() {
        val modelFile = loadModelFile()
        interpreter = Interpreter(modelFile)
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("global_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    fun predict(inputData: FloatArray): Float {
        val output = Array(1) { FloatArray(1) }
        interpreter?.run(inputData, output)
        return output[0][0]
    }
}
