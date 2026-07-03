import React from "react";
import ProductSubGrid from "./ProductSubGrid";

function ProductGrid({ products }) {
  return (
    <div className="p-6 my-container">
      <div className="grid gap-3 grid-cols-2 md:gap-3 md:grid-cols-4 place-items-center p-4 m-2">
        {products.map((product, index) => (
          <ProductSubGrid
            key={product.uid}
            pUid={product.uid}
            name={product.name}
            category={product.category.name}
            price={product.productVariants[0].price}
          />
        ))}
      </div>
    </div>
  );
}

export default ProductGrid;
