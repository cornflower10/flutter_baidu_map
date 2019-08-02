package com.cornflower.flutterbaidumap;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xiejingbao on 2019/7/30.
 */
public class ListLocationAdapter extends BaseQuickAdapter<Location, BaseViewHolder> {
    public ListLocationAdapter( @Nullable List<Location> data) {
        super(R.layout.location_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Location item) {
        helper.setGone(R.id.iv,item.isChoose());
        helper.setText(R.id.tv_title,item.getPoi().name);

        helper.setText(R.id.tv_desc,item.getPoi().address);
    }
}
