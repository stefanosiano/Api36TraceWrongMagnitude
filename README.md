# Api 36 Trace Wrong Magnitude

Sample project to demonstrate that the system tracer writes durations and timestamps in the wrong magnitude order on API 36.  

This is an empty app (`new project -> empty Activity` template of Android Studio) that calls `Debug.startMethodTracingSampling`, and after 1 second it calls `Debug.stopMethodTracing`, reads the trace file, parses the duration of the trace and throws if it's smaller than 10 milliseconds (it should be ~1 second).  

Running the app on API 35 (or less) emulator works fine. Running on an emulator with API 36 crashes.
