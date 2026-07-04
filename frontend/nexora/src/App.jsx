import { useState } from "react";
import Navbar from "./components/navbar/Navbar";
import Main from "./components/main/Main";
import Footer from "./components/footer/Footer";
import { Toaster } from "react-hot-toast";
import ProductGrid from "./components/main/ProductGrid";
import AppRoutes from "./routes/AppRoutes";
import HomePage from "./pages/HomePage";
import ProductPage from "./pages/ProductPage";
import { useSelector } from "react-redux";
function App() {
  const isLogin = useSelector((state) => state.auth.isLogin);
  return (
    <>
      <Toaster position="top" />
      <AppRoutes />
    </>
  );
}

export default App;
