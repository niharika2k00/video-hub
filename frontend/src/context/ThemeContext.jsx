import { createContext, useContext, useEffect, useState } from "react";

const ThemeCtx = createContext({ theme: "light", toggle: () => {} });

export function ThemeProvider({ children }) {
  const initial = () =>
    localStorage.theme ||
    (window.matchMedia("(prefers-color-scheme: dark)").matches
      ? "dark"
      : "light");

  const [theme, setTheme] = useState(initial);

  useEffect(() => {
    const html = document.documentElement;
    html.classList.remove(theme === "dark" ? "light" : "dark");
    html.classList.add(theme);
    localStorage.theme = theme;
  }, [theme]);

  const toggle = () => setTheme((t) => (t === "dark" ? "light" : "dark"));

  return (
    <ThemeCtx.Provider value={{ theme, toggle }}>{children}</ThemeCtx.Provider>
  );
}

export const useTheme = () => useContext(ThemeCtx);
