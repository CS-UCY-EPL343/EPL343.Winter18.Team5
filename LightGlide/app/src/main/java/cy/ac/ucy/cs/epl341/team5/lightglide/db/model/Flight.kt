package cy.ac.ucy.cs.epl341.team5.lightglide.db.model

import android.os.Parcel
import android.os.Parcelable
import java.sql.Time
import java.sql.Timestamp
import java.time.Duration

data class Flight constructor(val name:String?, val start:Timestamp, val maxAltitude: Int, val distance: Double, val end: Timestamp, val duration: Int):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            Timestamp(parcel.readLong()),
            parcel.readInt(),
            parcel.readDouble(),
            Timestamp(parcel.readLong()),
                    parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(maxAltitude)
        parcel.writeDouble(distance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Flight> {
        override fun createFromParcel(parcel: Parcel): Flight {
            return Flight(parcel)
        }

        override fun newArray(size: Int): Array<Flight?> {
            return arrayOfNulls(size)
        }
    }
}