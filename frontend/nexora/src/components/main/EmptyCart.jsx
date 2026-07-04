import React from "react";
import { HiShoppingCart } from "react-icons/hi2";
import { useNavigate } from "react-router-dom";

function EmptyCart() {
  const navigate = useNavigate();

  return (
    <div className="flex min-h-[70vh] flex-col items-center justify-center px-6 text-center">
      <div className="flex h-28 w-28 items-center justify-center rounded-full bg-gray-100">
        <HiShoppingCart className="text-6xl text-gray-400" />
      </div>

      <h2 className="mt-8 text-3xl font-bold text-gray-900">
        Your cart is empty
      </h2>

      <p className="mt-3 max-w-md text-gray-500">
        Looks like you haven't added anything to your cart yet. Explore our
        products and find something you'll love.
      </p>

      <button
        onClick={() => navigate("/products")}
        className="mt-8 rounded-full bg-black px-8 py-3 font-medium text-white transition hover:bg-gray-800"
      >
        Continue Shopping
      </button>
    </div>
  );
}

export default EmptyCart;
