
package com.fahrul.covid.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("covid")
    @Expose
    private Covid covid;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;
    private final static long serialVersionUID = -8294832531033099331L;

    protected Data(Parcel in) {
        this.covid = ((Covid) in.readValue((Covid.class.getClassLoader())));
    }

    public Data() {
    }

    public Covid getCovid() {
        return covid;
    }

    public void setCovid(Covid covid) {
        this.covid = covid;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(covid);
    }

    public int describeContents() {
        return  0;
    }

}
