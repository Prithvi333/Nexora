import React from "react";
import Navbar from "../components/navbar/Navbar";
import Main from "../components/main/Main";
import Footer from "../components/footer/Footer";
import { useSelector } from "react-redux";

function HomePage() {
  return (
    <>
      <Navbar />
      <Main />
      <Footer />
    </>
  );
}

export default HomePage;
