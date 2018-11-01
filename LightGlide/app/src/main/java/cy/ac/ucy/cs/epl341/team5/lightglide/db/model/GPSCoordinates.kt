package cy.ac.ucy.cs.epl341.team5.lightglide.db.model

import android.os.Parcel
import android.os.Parcelable


data class GPSCoordinates constructor(val longtitude:Double, val latitude:Double, val altitude:Double): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(longtitude)
        parcel.writeDouble(latitude)
        parcel.writeDouble(altitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GPSCoordinates> {
        override fun createFromParcel(parcel: Parcel): GPSCoordinates {
            return GPSCoordinates(parcel)
        }

        override fun newArray(size: Int): Array<GPSCoordinates?> {
            return arrayOfNulls(size)
        }
    }
}