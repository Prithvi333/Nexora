import { useState } from "react";
import Navbar from "./components/navbar/Navbar";
import Main from "./components/main/Main";
import Footer from "./components/footer/Footer";
function App() {
  const [count, setCount] = useState(0);
  return (
    <>
      <Navbar />
      <Main />
      <Footer />
    </>
  );
}

export default App;
