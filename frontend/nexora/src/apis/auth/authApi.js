import axios from "axios";

const userLogin = async (formData) => {
  return await axios.post(
    "http://localhost:8080/api/auth/user/login",
    formData,
    {
      headers: {
        "Content-Type": "application/json",
      },
    },
  );
};

const userRegistration = async (formData) => {
  return await axios.post(
    "http://localhost:8080/api/auth/user/signup",
    formData,
    {
      headers: {
        "Content-Type": "application/json",
      },
    },
  );
};

export { userLogin, userRegistration };
