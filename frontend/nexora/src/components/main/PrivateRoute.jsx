import React, { use } from "react";
import { useSelector } from "react-redux";

function PrivateRoute({ children }) {
  const auth = useSelector((state) => state.auth);

  return auth.isLogin ? (
    children
  ) : (
    <div>Please log in to access this page.</div>
  );
}

export default PrivateRoute;
