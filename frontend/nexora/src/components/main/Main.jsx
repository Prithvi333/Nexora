import React from "react";
import FlexCard from "./FlexCard";
import SubGrid from "./GridCard";
import MiniGridItem from "./MiniGridItem";
import ps5 from "../../assets/images/ps.jpg";
import headphone from "../../assets/images/headphones.jpg";
import facewash from "../../assets/images/facewash.jpg";
import camera from "../../assets/images/camera.jpg";
import Footer from "../footer/Footer";
function Main() {
  return (
    <main className="my-container">
      <FlexCard />
      <SubGrid />
      <div className="flex flex-col mt-8 space-y-5 p-5 items-center justify-center">
        <div className="space-y-4 text-center mb-20">
          <h2 className="font-bold text-4xl md:text-6xl  uppercase">
            Spotlight
          </h2>
          <h5 className="text-sm tracking-wide">
            Choose you favorite brand and much more!
          </h5>
        </div>
        <div className="max-w-2xl  grid grid-cols-3 items-center md:grid-cols-6 gap-5 md:gap-10 p-1">
          <MiniGridItem url={camera} height="50px" ItemName="camera" />
          <MiniGridItem url={ps5} height="50px" ItemName="play station" />

          <MiniGridItem url={headphone} height="50px" ItemName="headphones" />

          <MiniGridItem url={facewash} height="50px" ItemName="facewash" />
          <MiniGridItem url={camera} height="50px" ItemName="camera" />
          <MiniGridItem url={ps5} height="50px" ItemName="play station" />

          <MiniGridItem url={headphone} height="50px" ItemName="headphones" />

          <MiniGridItem url={facewash} height="50px" ItemName="facewash" />
          <MiniGridItem url={camera} height="50px" ItemName="camera" />
          <MiniGridItem url={ps5} height="50px" ItemName="play station" />

          <MiniGridItem url={headphone} height="50px" ItemName="headphones" />

          <MiniGridItem url={facewash} height="50px" ItemName="facewash" />
        </div>
      </div>
    </main>
  );
}

export default Main;
