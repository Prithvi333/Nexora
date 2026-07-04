import React, { useState } from "react";
import camera from "../../assets/images/camera.jpg";
import { FaHeart } from "react-icons/fa";
import { FiHeart } from "react-icons/fi";
import ProductSubGridHeader from "./ProductSubGridHeader";
import { useNavigate } from "react-router-dom";

function ProductSubGrid({ url, pUid, name, category, price }) {
  const [liked, setLiked] = useState(false);
  const navigator = useNavigate();
  const handleOnClick = () => {
    navigator(`/product/${pUid}`);
  };
  return (
    <div onClick={handleOnClick}>
      <div className="relative flex-items-center justify-center mb-2">
        <img src={camera} className="" alt="Camera" />
        <button
          onClick={() => setLiked(!liked)}
          className="absolute top-3 right-3 flex h-10 w-10 items-center justify-center rounded-full bg-white shadow-md transition-all duration-300 hover:scale-110"
        >
          {liked ? (
            <FaHeart className="text-xl text-red-500" />
          ) : (
            <FiHeart className="text-xl text-gray-600" />
          )}
        </button>
      </div>
      <ProductSubGridHeader
        key={pUid}
        name={name}
        category={category}
        price={price}
      />
    </div>
  );
}

export default ProductSubGrid;
