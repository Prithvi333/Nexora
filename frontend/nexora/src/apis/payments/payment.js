// currentcy
// paymentMethod
// orderUid

import axios from "axios";

const generatePayment = async (PaymentRequest, token) => {
  const response = await axios({
    url: "http://localhost:8080/api/orders/user/order/payment",
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    data: PaymentRequest,
  });
  return response.data;
};

const getPaymentByOrderUid = async (orderUid, token) => {
  const response = await axios({
    url: "http://localhost:8080/api/payments/user/payments",
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    params: {
      orderUid,
    },
  });
  return response.data;
};

export { generatePayment, getPaymentByOrderUid };
