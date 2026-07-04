import React from "react";

function MiniGridItem({ url, height, ItemName }) {
  return (
    <div className="flex p-5 hover:cursor-pointer hover:scale-110 transition duration-100 px-3 flex-col  items-center justify-center space-y-3">
      <img src={url} className={`h-[${height}  rounded-md w-full  `} alt="" />
      <h5 className="text-md md:text-xs capitalize">{ItemName}</h5>
    </div>
  );
}

export default MiniGridItem;
