import React from "react";
import shoes from "../../assets/images/shoes.png";
import bottle from "../../assets/images/bottle.png";

import cream from "../../assets/images/cream.png";
import SubCard from "./SubCard";

function FlexCard() {
  return (
    <div className=" p-5">
      <div>
        <h2 className="text-xl md:text-2xl m-3 font-semibold">Feature</h2>
        <div className="flex flex-col space-y-4 md:space-y-0 md:flex-row md:space-x-4 cursor-pointer items-center justify-center">
          <SubCard
            url={shoes}
            title="Premium shoes"
            desc="Limited collection"
            textColor="black"
          />
          <SubCard
            url={cream}
            title="Premium Bottle"
            desc="Limited collection"
          />
        </div>
      </div>
    </div>
  );
}

export default FlexCard;
