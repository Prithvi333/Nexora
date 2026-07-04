import React, { use, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import ProductSubGrid from "./ProductSubGrid";
import ProductSubGridHeader from "./ProductSubGridHeader";
import camera from "../../assets/images/camera.jpg";
import { HiHeart } from "react-icons/hi";
import { BsHeart, BsHeartFill } from "react-icons/bs";
import { Provider, shallowEqual, useDispatch, useSelector } from "react-redux";
import store from "../../app/store";
import { HiShoppingCart } from "react-icons/hi";

function ProductDetails() {
  const params = useParams();
  const cart = useSelector((store) => store.cart.cartProducts, shallowEqual);
  const favorite = useSelector(
    (store) => store.favorite.wishListProduct,
    shallowEqual,
  );
  const dispatcher = useDispatch();
  const navigator = useNavigate();
  const { pUid } = params;
  const isPresentInCart = cart.some((item) => item.uid == pUid);
  const isFavorite = favorite.some((item) => item.uid == pUid);
  const { products } = useSelector((store) => store.products);
  let myProduct = products.find((prod) => prod.uid === pUid);
  const {
    description,
    name,
    brand,
    category: { name: categaoryName },
    productVariants,
  } = myProduct;

  const [variant1] = productVariants;
  const [selectedVariant, setSelectedVariant] = useState(variant1);
  const { color, price, size, productImages } = selectedVariant;

  const handleAddOrRemoveItem = (isCart = false) => {
    if (isCart) {
      if (isPresentInCart) {
        dispatcher({ type: "REMOVE_FROM_CART", payload: { productUid: pUid } });
      } else {
        dispatcher({
          type: "ADD_TO_CART",
          payload: { product: { ...myProduct, selectedVariant, quantity: 1 } },
        });
      }
    } else {
      if (isFavorite) {
        dispatcher({
          type: "REMOVE_FROM_WISHLIST",
          payload: { productUid: pUid },
        });
      } else {
        dispatcher({
          type: "ADD_TO_WISHLIST",
          payload: { product: myProduct },
        });
      }
    }
  };

  return (
    <div className="my-container p-6">
      <div className="flex items-center mr-2">
        <HiShoppingCart
          onClick={() => navigator("/cart")}
          className="ml-auto text-3xl cursor-pointer"
        />
      </div>
      <div className="md:hidden my-3">
        <ProductSubGridHeader
          name={name}
          category={categaoryName}
          price={price}
        />
      </div>
      <div className="flex flex-col justify-center items-center  md:justify-between  space-y-2 md:flex-row p-3">
        <div className="flex flex-col items-center justify-center w-full ">
          <img
            src={productImages[0].url}
            className=" w-full rounded-2xl md:w-4/5 h-150 object-cover"
            alt="Camera"
          />
          <div className="grid-grid-cols-1 grid-rows-auto py-3">
            {myProduct.productVariants.map((variant) => {
              return (
                <div
                  onClick={() => setSelectedVariant(variant)}
                  key={variant.uid}
                  className="flex items rounded center justify center p3 h-15 cursor-pointer"
                >
                  <img
                    className={
                      variant.uid == selectedVariant.uid &&
                      "border border-black"
                    }
                    src={variant.productImages[0].url}
                    alt=""
                  />
                </div>
              );
            })}
          </div>
        </div>

        <div className="flex flex-col w-full  m-2 text-center  md:w-1/5 items-center justify-center md:text-start">
          <div className="hidden md:block self-start mx-3">
            <ProductSubGridHeader
              name={name}
              category={categaoryName}
              price={price}
            />
          </div>
          <button
            onClick={() => handleAddOrRemoveItem(true)}
            className="w-full px-4 py-3 bg-black text-white md:mx-auto font-bold rounded-2xl hover:bg-gray-800 transition-all duration-300"
          >
            {isPresentInCart ? "Remove" : "Add to Cart"}
          </button>
          <button
            onClick={() => handleAddOrRemoveItem(false)}
            className="w-full h-17 px-4 py-3 bg-white text-black  font-bold rounded-2xl border-2 border-black mx-auto hover:bg-gray-100 transition-all duration-300 mt-2"
          >
            <span>
              {!isFavorite ? "Add to Wishlist" : "Remove from Wishlist"}{" "}
            </span>
            {!isFavorite ? (
              <BsHeart className="inline-block ml-2 text-xl" />
            ) : (
              <BsHeartFill className="inline-block ml-2 text-xl text-red-500" />
            )}
          </button>
          <h4 className="mt-5 mb-2 tracking-wide leading-6 text-justify">
            {description}
          </h4>
          <ul className="my-3 text-start text-gray-800  w-full">
            <li className=" text-sm mb-2">Brand Name: {brand}</li>

            <li className=" text-sm mb-2">Colour Shown: {color}</li>
            <li className="text-gray-500 text-sm ">Size: {size}</li>
          </ul>

          <div className="flex flex-col w-full justify-start text-start   space-y-1">
            <h3 className="text-md  font-semibold">Check delivery date</h3>
            <p className="my-2 text-gray-700 tracking-wide">
              Enter your pincode to check delivery time and pay on delivery
            </p>
            <input
              type="text"
              className="hover:outline-none w-full focus:outline-none border border-gray-500 rounded-2xl px-2  md:w-auto py-2 md:px-4 placeholder-gray-400"
              placeholder="Pincode"
            />
            <div className="flex flex-col space-y-3 mt-3 items-center  justify-center">
              <div className="flex justify-between w-full items-center">
                <h4 className="text-sm font-bold">
                  Estimated Delivery Date: 5-7 Business Days
                </h4>
                <p className="underline text-xs">Learn more</p>
              </div>
              <div className="flex justify-between w-full items-center">
                <h4 className="text-sm font-bold">
                  Estimated Delivery Date: 5-7 Business Days
                </h4>
                <p className="underline text-xs">Learn more</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProductDetails;
