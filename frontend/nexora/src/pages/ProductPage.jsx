import React, { use, useEffect, useState } from "react";
import Navbar from "../components/navbar/Navbar";
import Footer from "../components/footer/Footer";
import ProductGrid from "../components/main/ProductGrid";
import { fetchProducts } from "../apis/products/productsApi";
import { useDispatch, useSelector } from "react-redux";

function ProductPage() {
  const dispatcher = useDispatch();
  const [products, setProducts] = useState([]);
  const auth = useSelector((state) => state.auth);
  const fetchProductData = async () => {
    dispatcher({ type: "FETCH_PRODUCTS_REQUEST" });
    try {
      const response = await fetchProducts(auth.token);
      dispatcher({ type: "FETCH_PRODUCTS_SUCCESS", payload: response });
      setProducts(response);
    } catch (error) {
      dispatcher({ type: "FETCH_PRODUCTS_FAILURE", payload: error.message });
    }
  };
  useEffect(() => {
    if (auth.token) {
      fetchProductData();
    }
  }, [auth.token]);

  return (
    <>
      <ProductGrid products={products} />
    </>
  );
}

export default ProductPage;
