package com.shosen.max.presenter.contract;

import com.shosen.max.base.BasePresenter;
import com.shosen.max.base.BaseView;
import com.shosen.max.bean.MallAddressBean;

import java.util.List;

public interface AddressContract {

    interface View extends BaseView {
        void addressBack(List<MallAddressBean> mData);

        void showErrorMessage(Throwable throwable);

        void editAddressBack(int position, boolean isDefault);

    }

    interface Presenter extends BasePresenter<View> {
        void getAddressList();

        void modifyAddress(int position, MallAddressBean bean, boolean isDefault);
    }

    interface EditView extends BaseView {
        void showErrorMessage(Throwable throwable);

        void addOrModifyBack();

        void delAddressBack();
    }

    interface EditPresenter extends BasePresenter<EditView> {

        //        "userId":1,
//                "id": 0,
//                "name": "kathy",
//                "mobile": "17777810050",
//                "provinceId": 1,
//                "cityId": 32,
//                "areaId": 376,
//                "address": "123",
//                "isDefault": true
        void addOrModifyAddress(String id, String name, String mobile,
                                String provinceId, String cityId, String areaId,
                                String address, boolean isDefault);

        void delAddress(String id);

    }
}
