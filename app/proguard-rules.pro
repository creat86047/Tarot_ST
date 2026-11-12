# Parcelable.Creator is looked up by Android when unparcelling typed extras.
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}
