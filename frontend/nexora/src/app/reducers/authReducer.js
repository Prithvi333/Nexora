const initialAuth = {
  email: "",
  token: "",
  refreshToken: "",
  isLoading: false,
  isError: false,
  isLogin: false,
};

const authReducer = (state = initialAuth, action) => {
  switch (action.type) {
    case "LOGIN":
      return {
        ...state,
        isLoading: true,
      };

    case "LOGOUT":
      return {
        ...state,
        isLoading: false,
        email: "",
        isLogin: false,
      };

    case "SIGNUP":
      return {
        ...state,
        isLoading: true,
      };
    case "FAILED":
      return {
        ...state,
        isLoading: false,
        isError: true,
      };
    case "SUCCESS":
      return {
        ...state,
        isLoading: false,
        email: action.payload.email,
        token: action.payload.accessToken,
        isLogin: true,
      };
    case "LOADING":
      return {
        ...state,
        isLoading: true,
      };

    default:
      return state;
  }
};

export default authReducer;
