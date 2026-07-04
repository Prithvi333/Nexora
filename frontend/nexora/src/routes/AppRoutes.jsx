import { Route, Routes } from "react-router-dom";
import ProductDetails from "../components/main/ProductDetails";
import ProductPage from "../pages/ProductPage";
import HomePage from "../pages/HomePage";
import PrivateRoute from "../components/main/PrivateRoute";
import CartPage from "../pages/CartPage";
import NotFound from "../components/main/NotFount";
import Layout from "../layout/Layout";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route
        path="/cart"
        element={
          <Layout>
            <CartPage />
          </Layout>
        }
      />
      {/* <Route path="/wishlist" element={<Wishlist />} /> */}
      <Route
        path="/product/:pUid"
        element={
          <Layout>
            <ProductDetails />
          </Layout>
        }
      />
      <Route
        path="/products"
        element={
          // <PrivateRoute>
          <Layout>
            <ProductPage />
          </Layout>
          // </PrivateRoute>
        }
      />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}
