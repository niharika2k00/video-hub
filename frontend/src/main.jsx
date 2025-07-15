import React from "react";
import { ThemeProvider } from "@/context/ThemeContext";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import App from "./App";
import "./index.css";
import "react-toastify/dist/ReactToastify.css";

import { VideoProvider } from "./context/VideoContext";
import { AuthProvider } from "./context/AuthContext";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <ThemeProvider>
      {" "}
      <AuthProvider>
        <VideoProvider>
          <BrowserRouter>
            <App />
            <ToastContainer position="top-right" autoClose={3000} />
          </BrowserRouter>
        </VideoProvider>
      </AuthProvider>
    </ThemeProvider>{" "}
  </React.StrictMode>
);
