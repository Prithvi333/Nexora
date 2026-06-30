import React from "react";

function SubCard({ url, title, desc, textColor, small }) {
  return (
    <div className={`relative group text-${textColor}  drop-shadow-2xl`}>
      <img className="rounded-2xl" src={url} alt="" />
      <div className="absolute bottom-0 flex flex-col px-4 space-y-2">
        <h4
          className={`${!small ? "text-md md:text-xl tracking-wide" : "text-xs md:text-md"}   font-bold capitalize `}
        >
          {title}
        </h4>
        <h3
          className={`capitalize ${!small ? "text-md md:text-xl tracking-wide " : "text-xs md:text-md"}  font-bold `}
        >
          {desc}
        </h3>
        <button className=" bg-white opacity-0 transition-all duration-150 ease-in-out group-hover:opacity-70  text-black rounded-2xl px-4 py-2 font-semibold">
          Shop now
        </button>
      </div>
    </div>
  );
}

export default SubCard;
