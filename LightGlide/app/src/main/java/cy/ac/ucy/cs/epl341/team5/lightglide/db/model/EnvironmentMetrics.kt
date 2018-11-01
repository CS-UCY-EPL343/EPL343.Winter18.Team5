package cy.ac.ucy.cs.epl341.team5.lightglide.db.model

import android.os.Parcel
import android.os.Parcelable
import java.sql.Timestamp

data class EnvironmentMetrics constructor(val uv:Int, val timestamp: Timestamp,val temperature:Int, val humidity:Int, val pressure:Double):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            Timestamp(parcel.readLong()),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uv)
        parcel.writeInt(temperature)
        parcel.writeInt(humidity)
        parcel.writeDouble(pressure)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EnvironmentMetrics> {
        override fun createFromParcel(parcel: Parcel): EnvironmentMetrics {
            return EnvironmentMetrics(parcel)
        }

        override fun newArray(size: Int): Array<EnvironmentMetrics?> {
            return arrayOfNulls(size)
        }
    }
}