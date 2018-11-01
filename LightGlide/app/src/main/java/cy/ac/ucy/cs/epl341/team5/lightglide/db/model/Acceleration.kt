package cy.ac.ucy.cs.epl341.team5.lightglide.db.model

import android.os.Parcel
import android.os.Parcelable

data class Acceleration(val x:Double, val y: Double, val z:Double):Parcelable{

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(x)
        parcel.writeDouble(y)
        parcel.writeDouble(z)
    }

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble())

    companion object CREATOR : Parcelable.Creator<Acceleration> {
        override fun createFromParcel(parcel: Parcel): Acceleration {
            return Acceleration(parcel)
        }

        override fun newArray(size: Int): Array<Acceleration?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

}