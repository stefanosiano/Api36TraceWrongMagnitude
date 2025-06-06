package com.example.api36tracewrongmagnitude

import android.os.Bundle
import android.os.Debug
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.api36tracewrongmagnitude.ui.theme.Api36TraceWrongMagnitudeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    lateinit var traceFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cacheDir.mkdirs()
        traceFile = File(cacheDir, "trace.trace")
        Debug.startMethodTracingSampling(traceFile.path, 8000000, 10000)
        enableEdgeToEdge()
        setContent {
            Api36TraceWrongMagnitudeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> }
            }
        }
        lifecycleScope.launch {
            delay(1000)
            Debug.stopMethodTracing()
            val reader = traceFile.reader().buffered()
            var line = reader.readLine()
            while (!line.contains("elapsed-time-usec")) {
                line = reader.readLine()
            }
            val durationUs = line.substringAfter("=").toLong()
            // If the trace duration is less than 10 milliseconds, throw an exception.
            if (durationUs < 10_000) {
                throw IllegalStateException("Trace duration is too short: $durationUs microseconds, but expected ~1 second (or 1_000_000_000 microseconds).")
            }
            reader.close()
        }
    }
}