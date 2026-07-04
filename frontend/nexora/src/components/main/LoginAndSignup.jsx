import React, { use, useEffect, useState } from "react";
import { userRegistration, userLogin } from "../../apis/auth/authApi.js";
import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const initialForm = {
  username: "",
  email: "",
  password: "",
};

export default function LoginAdnSignUp({ handleFormEvent, isLogin }) {
  const [form, setForm] = useState(initialForm);
  const dispatcher = useDispatch();
  const navigator = useNavigate();

  const auth = useSelector((state) => state.auth);
  const handleFormSubmit = async () => {
    try {
      dispatcher({ type: "LOADING" });

      const response = isLogin
        ? await userLogin({ email: form.email, password: form.password })
        : await userRegistration(form);
      toast.success(`Successfully ${isLogin ? "logged in" : "signed up"}`);
      if (response.status === 200) {
        dispatcher({
          type: "SUCCESS",
          payload: { ...response.data, email: form.email },
        });
        navigator("/products");
      }
    } catch (error) {
      dispatcher({ type: "FAILED" });
      toast.error(error.response.data.message);
    }
  };

  const handelFormChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };
  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        handleFormSubmit();
      }}
      className="flex  bg-gray-400  rounded-b-xl p-10 flex-col space-y-5 items-center justify-center"
    >
      <button
        onClick={(e) => handleFormEvent(e, false)}
        className="absolute top-3 right-3 flex items-center justify-center w-8 h-8 rounded-full hover:bg-gray-100 hover:cursor-pointer transition duration-200"
      >
        <span className="text-2xl leading-none">&times;</span>
      </button>
      <h3 className="text-xl font-semibold tracking-wide">
        {isLogin ? "Login" : "Signup"} into you account
      </h3>
      <p className="text-md text-center text-white tracking-wide">
        Get personalised pics & faster checkout
      </p>
      {!isLogin && (
        <input
          type="text"
          name="username"
          onChange={(e) => handelFormChange(e)}
          value={form.username}
          className="py-2 w-full rounded-xl bg-white p-4 focus:outline-none placeholder-gray-400"
          placeholder="Enter you username"
        />
      )}
      <input
        type="email"
        name="email"
        value={form.email}
        onChange={(e) => handelFormChange(e)}
        className="py-2 w-full rounded-xl bg-white p-4 focus:outline-none placeholder-gray-400"
        placeholder="Enter you email"
      />
      <input
        type="password"
        name="password"
        value={form.password}
        onChange={(e) => handelFormChange(e)}
        className="py-2 w-full rounded-xl bg-white p-4 focus:outline-none placeholder-gray-400"
        placeholder="Enter you password"
      />
      <button
        type="submit"
        className="rounded-2xl  m-3 w-full h-13 py-4 font-bold bg-gray-200 hover:bg-white transition duration-150 ease-linear   cursor-pointer"
      >
        {auth.isLoading ? (
          <div className="flex items-center justify-center">
            <div className="h-8 w-8 animate-spin rounded-full border-4 border-gray-300 border-t-blue-600"></div>
          </div>
        ) : isLogin ? (
          "Sign In"
        ) : (
          "Sign Up"
        )}
      </button>
      <div className="text-center leading-5">
        <p className="text-sm text-white">
          By entering this site, you agree to the{" "}
          <span className="capitalize text-balance text-black font-semibold">
            Term & conditions
          </span>{" "}
          and{" "}
          <span className="capitalize text-black font-semibold">
            Privacy Policy
          </span>{" "}
        </p>
      </div>
    </form>
  );
}
