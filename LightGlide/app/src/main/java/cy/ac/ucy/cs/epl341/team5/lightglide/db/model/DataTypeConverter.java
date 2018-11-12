package cy.ac.ucy.cs.epl341.team5.lightglide.db.model;

import android.arch.persistence.room.TypeConverter;

import java.sql.Timestamp;

public class DataTypeConverter {

    @TypeConverter
    public static  Long toLong(Timestamp t){
        return java.util.Optional.ofNullable(t).orElseGet(()->new Timestamp(0)).getTime();
    }

    @TypeConverter
    public static Timestamp toTimestamp(Long l){
        return new Timestamp(java.util.Optional.ofNullable(l).orElseGet(()->0L));
    }
}
