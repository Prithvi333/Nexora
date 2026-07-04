import React from "react";
import { useNavigate } from "react-router-dom";

function NotFound() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex items-center justify-center bg-white px-6">
      <div className="text-center max-w-xl">
        <h1 className="text-8xl md:text-9xl font-black text-black tracking-tight">
          404
        </h1>

        <h2 className="mt-6 text-3xl md:text-4xl font-bold text-gray-900">
          Page Not Found
        </h2>

        <p className="mt-4 text-gray-500 leading-relaxed">
          Sorry, the page you're looking for doesn't exist or has been moved.
          Let's get you back to exploring Nexora.
        </p>

        <div className="mt-10 flex flex-col sm:flex-row justify-center gap-4">
          <button
            onClick={() => navigate("/")}
            className="rounded-full bg-black px-8 py-3 text-white font-semibold transition duration-300 hover:bg-gray-800"
          >
            Go Home
          </button>

          <button
            onClick={() => navigate(-1)}
            className="rounded-full border border-gray-300 px-8 py-3 font-semibold transition duration-300 hover:bg-gray-100"
          >
            Go Back
          </button>
        </div>
      </div>
    </div>
  );
}

export default NotFound;
