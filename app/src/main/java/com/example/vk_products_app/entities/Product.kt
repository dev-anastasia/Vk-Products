package com.example.vk_products_app.entities

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val rating: Double,
    val brand: String,
    val thumbnail: String,
    val images: List<String>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()?: "",
        parcel.readString()?: "",
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString()?: "",
        parcel.readString()?: "",
        parcel.createStringArrayList()?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeInt(price)
        parcel.writeDouble(rating)
        parcel.writeString(brand)
        parcel.writeString(thumbnail)
        parcel.writeStringList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }

}
