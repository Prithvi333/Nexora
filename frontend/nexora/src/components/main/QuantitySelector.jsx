import React from "react";

function QuantitySelector({ quantity, onIncrease, onDecrease }) {
  return (
    <div className="inline-flex my-2 items-center border border-gray-300 rounded-lg overflow-hidden">
      <button
        onClick={onDecrease}
        disabled={quantity <= 1}
        className="w-10 h-10 flex items-center justify-center text-xl font-semibold hover:bg-gray-100 disabled:opacity-40 disabled:cursor-not-allowed transition"
      >
        −
      </button>

      <div className="w-12 h-10 flex items-center justify-center border-x border-gray-300 font-semibold">
        {quantity}
      </div>

      <button
        onClick={onIncrease}
        className="w-10 h-10 flex items-center justify-center text-xl font-semibold hover:bg-gray-100 transition"
      >
        +
      </button>
    </div>
  );
}

export default QuantitySelector;
