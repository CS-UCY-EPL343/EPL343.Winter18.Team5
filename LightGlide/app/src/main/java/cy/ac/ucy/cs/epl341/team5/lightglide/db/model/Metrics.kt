package cy.ac.ucy.cs.epl341.team5.lightglide.db.model

import android.os.Parcel
import android.os.Parcelable
import java.sql.Timestamp

data class Metrics constructor(
        val altitude: Double, val descendRate: Double,
        val timestamp: Timestamp, val gpsCoordinates: GPSCoordinates
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            Timestamp(parcel.readLong()),
            GPSCoordinates.createFromParcel(parcel)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(altitude)
        parcel.writeDouble(descendRate)
        parcel.writeLong(timestamp.time)
        parcel.writeParcelable(gpsCoordinates, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Metrics> {
        override fun createFromParcel(parcel: Parcel): Metrics {
            return Metrics(parcel)
        }

        override fun newArray(size: Int): Array<Metrics?> {
            return arrayOfNulls(size)
        }
    }
}