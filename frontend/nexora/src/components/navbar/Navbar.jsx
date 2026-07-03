import React, { useState } from "react";
import logo from "../../assets/images/logo.png";
import NavSubMenu from "./NavSubMenu";
import LoginAdnSignUp from "../main/LoginAndSignup";
import { Toaster } from "react-hot-toast";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

export default function Navbar() {
  const [loginLogoutForm, setLoginLogoutForm] = useState({
    isLogin: false,
    formPopUp: false,
  });
  const isLogin = useSelector((state) => state.auth.isLogin);
  const dispatch = useDispatch();
  const navigator = useNavigate();
  const handleFormEvent = (e, isLogin) => {
    e.preventDefault();
    setLoginLogoutForm({
      isLogin: isLogin,
      formPopUp: !loginLogoutForm.formPopUp,
    });
  };

  return (
    <header className="my-container">
      {/* col-1  */}
      <div className="flex flex-col   w-full">
        <div className="hidden md:flex  p-3 justify-end bg-gray-300">
          <div className="flex items-center justify-center space-x-6 text-sm  font-bold">
            <a className="transition duration-200 hover:text-gray-500" href="#">
              Help
            </a>
            <span>|</span>
            {isLogin ? (
              <a
                onClick={() => {
                  dispatch({ type: "LOGOUT" });
                  navigator("/");
                }}
                className="transition duration-200 hover:text-gray-500"
                href="#"
              >
                Log Out
              </a>
            ) : (
              <>
                <a
                  onClick={(e) => handleFormEvent(e, false)}
                  className="transition duration-200 hover:text-gray-500"
                  href="#"
                >
                  Sign Up
                </a>
                <span>|</span>
                <a
                  onClick={(e) => handleFormEvent(e, true)}
                  className="transition duration-200 hover:text-gray-500"
                  href="#"
                >
                  Log In
                </a>
              </>
            )}
          </div>
        </div>
        {/* col-2 */}
        <div className="flex items-center  w-full p-1">
          <div className="flex-1 flex justify-start">
            <img src={logo} className="h-20" alt="Logo" />
          </div>

          <div className="hidden md:flex flex-1 justify-center items-center space-x-5 sm:mr-5">
            <div className="group">
              <a href="#">New & Featured</a>
              <div className="mt-1 mx-1 border border-black opacity-0 group-hover:opacity-65 transition duration-200 ease-in"></div>
            </div>

            <div className="group">
              <a href="#">Men</a>
              <div className="mt-1 mx-1 border border-black opacity-0 group-hover:opacity-65 transition duration-200 ease-in"></div>
            </div>

            <div className="group">
              <a href="#">Women</a>
              <div className="mt-1 mx-1 border border-black opacity-0 group-hover:opacity-65 transition duration-200 ease-in"></div>
            </div>

            <div className="group">
              <a href="#">Children</a>
              <div className="mt-1 mx-1 border border-black opacity-0 group-hover:opacity-65 transition duration-200 ease-in"></div>
            </div>

            <div className="group">
              <a href="#">Sale</a>
              <div className="mt-1 mx-1 border border-black opacity-0 group-hover:opacity-65 transition duration-200 ease-in"></div>
            </div>
          </div>

          <div className="flex-1 flex justify-end items-center">
            <input
              type="text"
              className="bg-gray-300 px-4 py-2 rounded-full focus:outline-none"
              placeholder="Search"
            />
          </div>
        </div>
        <div className="bg-300 text-center bg-gray-300 p-2 space-y-2 py-6">
          <h4 className="tracking-wide font-semibold">
            Enjoy 10% Off On The Nike App. Use: APP10
          </h4>
          <h5 className="text-sm hover:cursor-pointer underline">
            Download Node T&Cs
          </h5>
        </div>
      </div>
      <div className={isLogin ? "hidden" : "relative"}>
        <div className="nav-bg-image h-200  w-full">
          <div
            className={`
    transition-all duration-300 ease-in-out
    overflow-hidden md:max-w-md m-auto
    ${
      loginLogoutForm.formPopUp
        ? "opacity-100 scale-100 translate-y-0"
        : "opacity-0 scale-95 -translate-y-3 pointer-events-none"
    }
  `}
          >
            <LoginAdnSignUp
              handleFormEvent={handleFormEvent}
              isLogin={loginLogoutForm.isLogin}
            />
          </div>
        </div>
      </div>
    </header>
  );
}
