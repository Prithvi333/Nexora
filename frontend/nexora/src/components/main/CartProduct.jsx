import React, { useState } from "react";
import camera from "../../assets/images/camera.jpg";
import QuantitySelector from "./QuantitySelector";
import { HiHeart } from "react-icons/hi2";
import { shallowEqual, useDispatch, useSelector } from "react-redux";

function CartProduct({ item }) {
  const [counter, setCounter] = useState(1);
  const dispatcher = useDispatch();
  const { uid, name, brand, description, url, size, color, price } = item;
  const favorite = useSelector(
    (store) => store.favorite.wishListProduct,
    shallowEqual,
  );
  const isFavorite = favorite.some((product) => uid == product.uid);
  return (
    <div className="w-full ">
      <div className="border md:hidden border-gray-400 mb-4"></div>

      <div className="flex p-4 ">
        <div>
          <img src={url} className="w-32 h-32 object-cover rounded-lg" alt="" />
          <QuantitySelector
            quantity={counter}
            onIncrease={() => {
              dispatcher({
                type: "CHANGE_QUANTITY",
                payload: { productUid: uid, isIncrement: true },
              });
              setCounter(counter + 1);
            }}
            onDecrease={() => {
              dispatcher({
                type: "CHANGE_QUANTITY",
                payload: { productUid: uid, isIncrement: false },
              });
              setCounter(counter - 1);
            }}
          />
          {isFavorite && (
            <span>
              {" "}
              <HiHeart className="block ml-2  text-xl" />{" "}
            </span>
          )}
        </div>
        <div className="flex flex-col space-y-1 ml-6 flex-1">
          <h3 className="font-bold">{name}</h3>
          <h4 className=" my-1 font-semibold">&#8377;{price}</h4>

          <p className="text-gray-500">{description.slice(0, 30)}...</p>

          <p className="text-gray-500">14 Day Return</p>

          <p className="underline cursor-pointer text-gray-500">Size {size}</p>
        </div>
      </div>
    </div>
  );
}

export default CartProduct;
