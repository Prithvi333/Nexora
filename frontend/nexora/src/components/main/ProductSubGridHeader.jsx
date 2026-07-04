import React from "react";

function ProductSubGridHeader({ name, category, price }) {
  return (
    <div className="flex flex-col  justify-start p-2 space-y-1">
      <h3 className="text-md  font-semibold">{name}</h3>
      <p className="text-gray-500 text-sm mb-2">{category}</p>
      <p className="text-gray-900 font-semibold">${price}</p>
    </div>
  );
}

export default ProductSubGridHeader;
