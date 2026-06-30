import React from "react";

function NavSubMenu() {
  return (
    <div className="flex flex-col space-y-5 p-2">
      <a className=" capitalize font-bold hover:text-gray-200 transition duration-200 ease-in">
        Feature
      </a>
      <a
        href=""
        className=" text-sm text-gray-200 capitalize hover:text-black transition duration-200"
      >
        Rip the script
      </a>
      <a className="text-sm   text-gray-200 capitalize hover:text-black transition duration-200 ease-in">
        New Arrival
      </a>
      <a
        href=""
        className="text-sm capitalize text-gray-200 hover:text-black transition duration-200"
      >
        Best Seller
      </a>
      <a
        href=""
        className="text-sm text-gray-200 capitalize hover:text-black transition duration-200"
      >
        Top pick up under $5000
      </a>
    </div>
  );
}

export default NavSubMenu;
