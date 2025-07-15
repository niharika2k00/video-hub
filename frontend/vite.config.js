import { defineConfig } from "vite";
import tailwindcss from "@tailwindcss/vite";
import react from "@vitejs/plugin-react";
import path from "path";

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
    // {
    //   name: "custom-mime-types",
    //   configureServer(server) {
    //     server.middlewares.use((req, res, next) => {
    //       if (req.url.endsWith(".m3u8")) {
    //         res.setHeader("Content-Type", "application/vnd.apple.mpegurl");
    //       }
    //       if (req.url.endsWith(".ts")) {
    //         res.setHeader("Content-Type", "video/mp2t");
    //       }
    //       next();
    //     });
    //   },
    // },
  ],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"), // configured for importing modules from src(root) directory
    },
  },
  // optimizeDeps: {
  //   include: ["video.js"], // pre-bundle that same copy for dev
  // },
  // assetsInclude: ["**/*.ts"],
  server: {
    port: 5173, // frontend server default port (5173 for vite)
    proxy: {
      "/api": {
        target: "http://localhost:4040", // backend server
        changeOrigin: true,
        secure: false,
      },
      "/s01/video": {
        target: "http://localhost:8080", // nginx server
        changeOrigin: true,
      },
    },
    // middlewareMode: false,
    // fs: {
    //   allow: ["public"],
    // },
  },
});
