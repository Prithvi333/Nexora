import { legacy_createStore as createStore, combineReducers } from "redux";

import authReducer from "./reducers/authReducer";
import productReducer from "./reducers/productReducer";
import cartReducer from "./reducers/cartReducer";
import wishListReducer from "./reducers/wishlist";

const rootReducer = combineReducers({
  auth: authReducer,
  products: productReducer,
  cart: cartReducer,
  favorite: wishListReducer,
});

const store = createStore(rootReducer);

export default store;
