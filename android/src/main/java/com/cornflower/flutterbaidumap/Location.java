package com.cornflower.flutterbaidumap;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.search.core.PoiInfo;

/**
 * Created by xiejingbao on 2019/7/30.
 */
public class Location implements Parcelable {
    private PoiInfo poi;
    private boolean isChoose;

    public PoiInfo getPoi() {
        return poi;
    }

    public void setPoi(PoiInfo poi) {
        this.poi = poi;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.poi, flags);
        dest.writeByte(this.isChoose ? (byte) 1 : (byte) 0);
    }

    public Location() {
    }

    protected Location(Parcel in) {
        this.poi = in.readParcelable(PoiInfo.class.getClassLoader());
        this.isChoose = in.readByte() != 0;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
