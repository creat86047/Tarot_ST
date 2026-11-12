# Parcelable.Creator is looked up by Android when unparcelling typed extras.
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# ViewModelProvider discovers these constructors reflectively.
-keepclassmembers class com.example.tarot.viewmodel.** extends androidx.lifecycle.ViewModel {
    public <init>(androidx.lifecycle.SavedStateHandle);
}
