/** @type {import('tailwindcss').Config} */
// import defaultTheme from "tailwindcss/defaultTheme";

export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"], // Where to scan for Tailwind classes
  plugins: [require("@tailwindcss/typography")], // Tailwind plugins
  // theme: {}, // Custom colors, spacing, etc.
};
