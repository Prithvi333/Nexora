import React, { useEffect, useState } from "react";
import { HiHeart } from "react-icons/hi2";
import QuantitySelector from "../components/main/QuantitySelector";
import CartProduct from "../components/main/CartProduct";
import { shallowEqual, useSelector } from "react-redux";
import EmptyCart from "../components/main/EmptyCart";
function CartPage() {
  const cart = useSelector((store) => store.cart.cartProducts, shallowEqual);

  const modifiedCart = cart.map((item) => {
    const { name, uid, brand, description, selectedVariant } = item;
    const currentVariantImageUrl = selectedVariant.productImages[0].url;

    const { size, color, price, producteImages } = selectedVariant;
    return {
      uid,
      quantity: item.quantity,
      name,
      brand,
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
                {Math.round(
                  total + total * gstPercentage - discount + deliveryCharges,
                )}
              </span>
            </div>

            <button className="w-full bg-black text-white py-3 rounded-xl hover:bg-gray-800 transition">
              Pay
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default CartPage;
