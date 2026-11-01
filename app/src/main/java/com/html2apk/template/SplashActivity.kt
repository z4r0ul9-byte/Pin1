<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white">

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:gravity="center"
        android:orientation="vertical">

        <androidx.appcompat.widget.AppCompatImageView
            android:layout_width="96dp"
            android:layout_height="96dp"
            android:src="@mipmap/ic_launcher"
            android:contentDescription="@string/app_name" />

        <androidx.appcompat.widget.AppCompatTextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Memuat..."
            android:textSize="16sp" />

        <ProgressBar
            android:layout_width="200dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            style="?android:attr/progressBarStyle" />

    </LinearLayout>

</FrameLayout>