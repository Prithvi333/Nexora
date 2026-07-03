const intialCart = {
  cartProducts: [],
};

const cartReducer = (state = intialCart, action) => {
  switch (action.type) {
    case "ADD_TO_CART":
      return {
        ...state,
        cartProducts: [...state.cartProducts, action.payload.product],
      };

    case "REMOVE_FROM_CART":
      return {
        ...state,
        cartProducts: state.cartProducts.filter(
          (product) => product.uid != action.payload.productUid,
        ),
      };
    case "CHANGE_QUANTITY":
      const pUid = action.payload.productUid;
      const isIncrement = action.payload.isIncrement;
      const newStateProduct = state.cartProducts.map((item) => {
        if (pUid === item.uid) {
          return { ...item, quantity: item.quantity + (isIncrement ? 1 : -1) };
        }
        return item;
      });
      return {
        ...state,
        cartProducts: newStateProduct,
      };
    default:
      return state;
  }
};

export default cartReducer;
