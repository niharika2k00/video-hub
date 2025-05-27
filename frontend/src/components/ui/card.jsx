import { cn } from "@/lib/utils";

/**
 * Minimal Card – content wrapper
 */
export const Card = ({ className, children }) => (
  <div
    className={cn(
      "bg-white shadow-xl rounded-2xl p-8 w-full max-w-sm",
      className
    )}
  >
    {children}
  </div>
);
