import React from "react";
import SubCard from "./SubCard";
import ps5 from "../../assets/images/ps.jpg";
import headphone from "../../assets/images/headphones.jpg";
import facewash from "../../assets/images/facewash.jpg";
import camera from "../../assets/images/camera.jpg";

function SubGrid() {
  return (
    <div className=" p-5">
      <div>
        <h2 className="text-2xl m-3 font-semibold">Trending</h2>
        <div className="grid gap-5 md:gap-8 grid-rows-auto grid-cols-2 md:grid-cols-4">
          <SubCard
            url={ps5}
            title="Enjoy play station now"
            desc="For  real Players"
            textColor="white"
            small={true}
          />
          <SubCard
            url={headphone}
            title="Enjoy play station now"
            desc="For  real Players"
            textColor="white"
            small={true}
          />
          <SubCard
            url={facewash}
            title="Enjoy play station now"
            desc="For  real Players"
            textColor="white"
            small={true}
          />
          <SubCard
            url={camera}
            title="Enjoy play station now"
            desc="For  real Players"
            textColor="white"
            small={true}
          />
        </div>
      </div>
    </div>
  );
}

export default SubGrid;
