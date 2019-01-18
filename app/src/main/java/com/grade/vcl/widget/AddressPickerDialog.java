package com.grade.vcl.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.grade.unit.adapter.binding.SingleTypeAdapter;
import com.grade.unit.adapter.binding.widget.BaseViewAdapter;
import com.grade.unit.listener.OnConfirmListener;
import com.grade.unit.util.DeviceInfoUtil;
import com.grade.unit.util.IsEmpty;
import com.grade.vcl.R;
import com.grade.vcl.databinding.DialogAddressPickerBinding;
import com.grade.vcl.model.BAddress;
import com.grade.vcl.model.BUcn;

import java.util.List;

/**
 * AddressPickerDialog : 地址选择器
 * <p>
 * </> Created by ylwei on 2018/3/30.
 */
public abstract class AddressPickerDialog extends AppCompatDialog {
  private OnConfirmListener<AddressPickerDialog, BAddress> listener;
  private DialogAddressPickerBinding binding;
  private SingleTypeAdapter<BUcn> adapter;
  private BAddress address;
  private int currentIndex;

  public AddressPickerDialog(@NonNull Context context) {
    super(context, R.style.style_dialog);
    initView();
    startLoading();
  }

  private void initView() {
    binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
        R.layout.dialog_address_picker, null, false);
    binding.setPresenter(new Presenter());
    binding.setIndex(5);// 显示 请选择 字样为红色
    adapter = new SingleTypeAdapter<>(getContext(), R.layout.item_address_list);
    adapter.setPresenter(new Presenter());
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    manager.setOrientation(LinearLayoutManager.VERTICAL);
    binding.rootRcv.setLayoutManager(manager);
    binding.rootRcv.setAdapter(adapter);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(binding.getRoot());
    initComponent();
  }

  private void initComponent() {
    Window window = getWindow();
    if (window == null)
      return;
    window.setGravity(Gravity.BOTTOM);
    window.setWindowAnimations(R.style.animation_bottom_rising);
    window.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.height = DeviceInfoUtil.getScreenHeight() * 3 / 5;
    lp.width = -1;
    window.setAttributes(lp);
  }

  public class Presenter implements BaseViewAdapter.Presenter {

    public void onClose() {
      dismiss();
    }

    public void onTitle(int index) {
      binding.setIndex(index);
      BUcn bUcn;
      if (index == 0) {
        bUcn = null;
      } else if (index == 1) {
        bUcn = address.getProvince();
      } else if (index == 2) {
        bUcn = address.getCity();
      } else if (index == 3) {
        bUcn = address.getDistrict();
      } else {
        return;
      }
      currentIndex = index;
      startLoading();
      loadDate(bUcn);
    }

    public void onItem(BUcn ucn) {
      binding.setIndex(5);
      if (currentIndex == 0) {
        address.setProvince(ucn);
        address.setCity(null);
        address.setDistrict(null);
        address.setStreet(null);
      } else if (currentIndex == 1) {
        address.setCity(ucn);
        address.setDistrict(null);
        address.setStreet(null);
      } else if (currentIndex == 2) {
        address.setDistrict(ucn);
        address.setStreet(null);
      } else if (currentIndex == 3) {
        address.setStreet(ucn);
        listener.onConfirm(AddressPickerDialog.this, address);
        dismiss();
        return;
      }
      currentIndex++;
      binding.setItem(address);
      startLoading();
      loadDate(ucn);
    }
  }

  protected void setAddress(BAddress address) {
    this.address = address;
    binding.setItem(address);
    if (IsEmpty.object(address) || IsEmpty.object(address.getProvince())) {
      loadDate(null);
    } else {
      currentIndex = 3;
      loadDate(address.getDistrict());
    }
  }

  public AddressPickerDialog setListener(
      OnConfirmListener<AddressPickerDialog, BAddress> listener) {
    this.listener = listener;
    return this;
  }

  protected abstract void loadDate(BUcn ucn);

  protected void loadFail(String errorMsg) {
    if (!isShowing())
      return;
    stopLoading();
//    DialogUtil.getErrorDialog(getContext(), errorMsg).show();
  }

  protected void loadSuccess(List<BUcn> addressList) {
    if (!isShowing())
      return;
    stopLoading();
    int index = formatBUcn(addressList);
    adapter.set(addressList);
    adapter.notifyDataSetChanged();
    binding.rootRcv.scrollToPosition(index);
  }

  private int formatBUcn(List<BUcn> addressList) {
    int i = 0;
    BUcn bUcn = new BUcn();
    if (currentIndex == 0) {
      bUcn = address.getProvince();
    } else if (currentIndex == 1) {
      bUcn = address.getCity();
    } else if (currentIndex == 2) {
      bUcn = address.getDistrict();
    } else if (currentIndex == 3) {
      bUcn = address.getStreet();
    }
    if (IsEmpty.object(bUcn))
      return 0;
    for (BUcn temp : addressList) {
      if (temp.getName().equals(bUcn.getName())) {
        temp.setNewUcn(true);
        return i;
      }
      i++;
    }
    return 0;
  }

  private void startLoading() {
    binding.loadingLayout.startLoading();
  }

  private void stopLoading() {
    binding.loadingLayout.stopLoading();
  }
}
