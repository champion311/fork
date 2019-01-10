package com.shosen.max.bean;

import com.shosen.max.bean.mall.CartList;

import java.util.List;

public class ShoppingCartBean {

    public class CartTotal {
        private int cartTotal;
        private int goodsCount;//2,
        private int checkedGoodsCount;//2,
        private double goodsAmount;//118,
        private double checkedGoodsAmount;//;118

        public int getCartTotal() {
            return cartTotal;
        }

        public void setCartTotal(int cartTotal) {
            this.cartTotal = cartTotal;
        }

        public int getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(int goodsCount) {
            this.goodsCount = goodsCount;
        }

        public int getCheckedGoodsCount() {
            return checkedGoodsCount;
        }

        public void setCheckedGoodsCount(int checkedGoodsCount) {
            this.checkedGoodsCount = checkedGoodsCount;
        }

        public double getGoodsAmount() {
            return goodsAmount;
        }

        public void setGoodsAmount(double goodsAmount) {
            this.goodsAmount = goodsAmount;
        }

        public double getCheckedGoodsAmount() {
            return checkedGoodsAmount;
        }

        public void setCheckedGoodsAmount(double checkedGoodsAmount) {
            this.checkedGoodsAmount = checkedGoodsAmount;
        }
    }


    private CartTotal cartTotal;

    private List<CartList> cartList;

    public void setCartTotal(CartTotal cartTotal) {
        this.cartTotal = cartTotal;
    }

    public void setCartList(List<CartList> cartList) {
        this.cartList = cartList;
    }

    public CartTotal getCartTotal() {
        return cartTotal;
    }

    public List<CartList> getCartList() {
        return cartList;
    }
}
