package com.example.myapplication.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class QRItem(val image: Bitmap?, val filename: String): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(image, flags)
        parcel.writeString(filename)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QRItem> {
        override fun createFromParcel(parcel: Parcel): QRItem {
            return QRItem(parcel)
        }

        override fun newArray(size: Int): Array<QRItem?> {
            return arrayOfNulls(size)
        }
    }
}