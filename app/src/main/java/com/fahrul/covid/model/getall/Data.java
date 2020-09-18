
package com.fahrul.covid.model.getall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("covid")
    @Expose
    private List<Covid> covid = new ArrayList<Covid>();
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
    private final static long serialVersionUID = 5056117249685460592L;

    protected Data(Parcel in) {
        in.readList(this.covid, (com.fahrul.covid.model.getall.Covid.class.getClassLoader()));
    }

    public Data() {
    }

    public List<Covid> getCovid() {
        return covid;
    }

    public void setCovid(List<Covid> covid) {
        this.covid = covid;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(covid);
    }

    public int describeContents() {
        return  0;
    }

}
