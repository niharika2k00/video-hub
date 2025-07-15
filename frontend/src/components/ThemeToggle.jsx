import { Sun, Moon } from "lucide-react";
import { useTheme } from "@/context/ThemeContext";
import { Button } from "@/components/ui/button";

const ThemeToggle = ({ className = "" }) => {
  const { theme, toggle } = useTheme();
  const Icon = theme === "dark" ? Sun : Moon;

  return (
    <Button
      aria-label="Toggle theme"
      className={className}
      onClick={toggle}
      size="icon"
      variant="ghost"
    >
      <Icon className="h-5 w-5" />
    </Button>
  );
};

export default ThemeToggle;
