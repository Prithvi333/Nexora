import axios from "axios";

const fetchProducts = async (token) => {
  const response = await axios.get(
    "http://localhost:8080/api/products/user/product",
    {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    },
  );
  return response.data;
};

export { fetchProducts };
