package cy.ac.ucy.cs.epl341.team5.lightglide.db.model

import android.os.Parcel
import android.os.Parcelable

data class Velocity constructor(val x:Double, val y: Double, val z:Double):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(x)
        parcel.writeDouble(y)
        parcel.writeDouble(z)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Velocity> {
        override fun createFromParcel(parcel: Parcel): Velocity {
            return Velocity(parcel)
        }

        override fun newArray(size: Int): Array<Velocity?> {
            return arrayOfNulls(size)
        }
    }
}

