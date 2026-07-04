import axios from "axios";

const createOrder = async (orderRequest, token) => {
  const response = await axios({
    url: "http://localhost:8080/api/orders/user/order",
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    data: orderRequest,
  });
  return response.data;
};
export { createOrder };
