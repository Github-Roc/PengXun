package cn.peng.pxun.ui.view.picker;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.peng.pxun.modle.picker.Province;
import cn.peng.pxun.utils.ConvertUtil;

/**
 * 获取地址数据并显示地址选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/15
 */
public class AddressPickTask extends AsyncTask<String, Void, ArrayList<Province>> {
    private Activity activity;
    private Callback callback;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideProvince = false;
    private boolean hideCounty = false;

    private AddressPicker picker;

    public AddressPickTask(Activity activity) {
        this.activity = activity;
    }

    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        //初始化数据
    }

    @Override
    protected ArrayList<Province> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }
        ArrayList<Province> data = new ArrayList<>();
        try {
            String json = ConvertUtil.toString(activity.getAssets().open("city.json"));
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            Type classType = new TypeToken<List<Province>>() {}.getType();
            ArrayList<Province> dataLsit = gson.fromJson(json,classType);
            data.addAll(dataLsit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<Province> result) {
        if (result.size() > 0) {
            picker = new AddressPicker(activity, result);
            picker.setHideProvince(hideProvince);
            picker.setHideCounty(hideCounty);
            if (hideCounty) {
                picker.setColumnWeight(0.8f, 1.0f);
            } else if (hideProvince) {
                picker.setColumnWeight(1.0f, 0.8f);
            } else {
                picker.setColumnWeight(0.8f, 1.0f, 1.0f);
            }
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnAddressPickListener(callback);
        } else {
            callback.onAddressInitFailed();
        }
    }

    public void show(){
        picker.show();
    }

    public interface Callback extends AddressPicker.OnAddressPickListener {
        void onAddressInitFailed();
    }
}
