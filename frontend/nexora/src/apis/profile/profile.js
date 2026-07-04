import axios from "axios";

const fetchUserProfileByEmail = async (email, token) => {
  const response = await axios({
    url: "http://localhost:8080/api/users/user/profile",
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    params: {
      email: email,
    },
  });
  return response.data;
};

export { fetchUserProfileByEmail };
