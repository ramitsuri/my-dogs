# WorkManager InputMerger and Workers
-keep class androidx.work.InputMerger { *; }
-keep class androidx.work.OverwritingInputMerger { *; }
-keep class androidx.work.ArrayCreatingInputMerger { *; }
-keep class * extends androidx.work.InputMerger {
    <init>();
}
-keep class * extends androidx.work.Worker {
    <init>(...);
}
-keep class * extends androidx.work.ListenableWorker {
    <init>(...);
}
-keep class * extends androidx.work.CoroutineWorker {
    <init>(...);
}

# Room
-keep class * extends androidx.room.RoomDatabase {
    <init>();
}
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>();
}

# Kotlin Serialization
-keepattributes *Annotation*,Signature,InnerClass,EnclosingMethod
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
    @kotlinx.serialization.SerialName <fields>;
    @kotlinx.serialization.Contextual <fields>;
}
-keepclassmembers class ** {
    public static kotlinx.serialization.KSerializer Companion;
    public static kotlinx.serialization.KSerializer serializer(...);
}
-keep class * implements kotlinx.serialization.KSerializer { *; }

# Glance
-keep class * extends androidx.glance.appwidget.GlanceAppWidget { *; }
-keep class * extends androidx.glance.appwidget.GlanceAppWidgetReceiver { *; }
-keep class * extends androidx.glance.state.GlanceStateDefinition { *; }
