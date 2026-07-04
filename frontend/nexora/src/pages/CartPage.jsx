import React, { useEffect, useState } from "react";
import { HiHeart } from "react-icons/hi2";
import QuantitySelector from "../components/main/QuantitySelector";
import CartProduct from "../components/main/CartProduct";
import { shallowEqual, useSelector } from "react-redux";
import EmptyCart from "../components/main/EmptyCart";
import {
  generatePayment,
  getPaymentByOrderUid,
} from "../apis/payments/payment";
import { createOrder } from "../apis/orders/order";
import { fetchUserProfileByEmail } from "../apis/profile/profile";
import { current } from "@reduxjs/toolkit";
import { RAZORPAY_KEY_ID } from "../utils/constants";
function CartPage() {
  const cart = useSelector((store) => store.cart.cartProducts, shallowEqual);
  const auth = useSelector((store) => store.auth);
  const orderRequestList = [];
  const modifiedCart = cart.map((item) => {
    const { name, quantity, uid, brand, description, selectedVariant } = item;
    orderRequestList.push({
      productUid: uid,
      quantity,
      variantUid: selectedVariant.uid,
    });

    const currentVariantImageUrl = selectedVariant.productImages[0].url;

    const { size, color, price, inventory, producteImages } = selectedVariant;

    return {
      uid,
      quantity: item.quantity,
      name,
      brand,
      availableQuantity: inventory.quantity,
      description,
      size,
      url: currentVariantImageUrl,
      color,
      price,
    };
  });

  const total = modifiedCart.reduce((x, y) => x + y.price * y.quantity, 0);

  const discountPercentage = 0.02;
  const gstPercentage = 0.15;
  const discount = total * 0.02;
  const deliveryCharges = total > 2000 ? 500 : 100;
  const amountToPay = Math.round(
    total + total * gstPercentage - discount + deliveryCharges,
  );

  const callRazorPayToMakePayment = async (orderUid) => {
    const response = await getPaymentByOrderUid(orderUid, auth.token);

    const options = {
      key: RAZORPAY_KEY_ID,
      amount: response.amount,
      currency: response.currency,
      order_id: response.gatewayOrderId,

      name: "Nexora",
      description: "Order Payment",

      handler: function (paymentResponse) {
        console.log("SUCCESS");
        console.log(paymentResponse);
      },

      modal: {
        ondismiss: function () {
          console.log("Checkout Closed");
        },
      },
    };

    const razorpay = new window.Razorpay(options);

    razorpay.on("payment.failed", function (response) {
      console.log("PAYMENT FAILED");
      console.log(response.error);
    });

    razorpay.open();
  };

  const handlePayment = async () => {
    const userProfileResponse = await fetchUserProfileByEmail(
      auth.email,
      auth.token,
    );
    console.log(amountToPay);

    const orderRequest = {
      items: orderRequestList,
      userProfileUid: userProfileResponse.uid,
      totalAmount: amountToPay,
    };
    const orderResponse = await createOrder(orderRequest, auth.token);
    const paymentRequest = {
      orderUid: orderResponse.orderUid,
      paymentMethod: "UPI",
      currency: "INR",
    };

    const paymentResponse = await generatePayment(paymentRequest, auth.token);

    setTimeout(() => {
      console.log("let the order be inserted inside the db");
      callRazorPayToMakePayment(orderResponse.orderUid);
    }, 1000);
  };

  return cart.length == 0 ? (
    <EmptyCart />
  ) : (
    <>
      <h3 className="font-bold text-xl text-center md:text-3xl py-4 mb-4">
        Bag
      </h3>
      <div className="my-container flex justify-center p-6 ">
        <div className="flex flex-col md:flex-row md:justify-between md:space-x-10   gap-10">
          <div className="flex flex-col items-center justify-start p-3">
            {modifiedCart.map((item) => (
              <CartProduct key={item.uid} item={item} />
            ))}
          </div>
          <div className="border md:hidden border-gray-400 mb-1"></div>

          <div className="w-full md:w-100    p-6 rounded-lg self-start">
            <h3 className="font-bold text-xl md:text-2xl text-center mb-6">
              Summary
            </h3>

            <div className="flex justify-between mb-3">
              <span>Bag Total</span>
              <span>&#8377;{total}</span>
            </div>

            <div className="flex justify-between mb-3">
              <span>Delivery</span>
              <span>&#8377;{deliveryCharges}</span>
            </div>

            <div className="flex justify-between mb-3">
              <span>Discount</span>
              <span>&#8377;{discount}</span>
            </div>

            <div className="flex justify-between mb-4">
              <span>Taxes</span>
              <span>&#8377;{total * gstPercentage}</span>
            </div>

            <hr className="my-4" />

            <div className="flex justify-between font-bold text-lg mb-6">
              <span>You Pay</span>
              <span>
                &#8377;
                {amountToPay}
              </span>
            </div>

            <button
              onClick={handlePayment}
              className="w-full bg-black text-white py-3 rounded-xl hover:bg-gray-800 transition"
            >
              Pay
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default CartPage;
