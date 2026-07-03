const intialwishList = {
  wishListProduct: [],
};

const wishListReducer = (state = intialwishList, action) => {
  switch (action.type) {
    case "ADD_TO_WISHLIST":
      return {
        ...state,
        wishListProduct: [...state.wishListProduct, action.payload.product],
      };

    case "REMOVE_FROM_WISHLIST":
      return {
        ...state,
        wishListProduct: state.wishListProduct.filter(
          (product) => product.uid != action.payload.productUid,
        ),
      };

    default:
      return state;
  }
};

export default wishListReducer;
