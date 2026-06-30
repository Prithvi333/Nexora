import React from "react";

function Footer() {
  return (
    <div className="p-6">
      <div className="mx-10 my-5  border border-zinc-300"></div>
      <div className="flex mx-12 flex-col space-y-4 md:flex-row p-4 md:space-y-0 md:space-x-5 justify-start  text-gray-400 items center">
        <div>© 2026 Nike, Inc. All rights reserved</div>
        <a
          href=""
          className="cursor-pointer capitalize  transition text-gray-400 hover:text-black duration-100 ease-in"
        >
          Terms of Use
        </a>
        <a
          href=""
          className="cursor-pointer transition capitalize text-gray-400 hover:text-black duration-100 ease-in"
        >
          Nexoraprivacy policy
        </a>
        <a
          href=""
          className="cursor-pointer transition capitalize text-gray-400 hover:text-black duration-100 ease-in"
        >
          Store claim policy
        </a>
      </div>
    </div>
  );
}

export default Footer;
