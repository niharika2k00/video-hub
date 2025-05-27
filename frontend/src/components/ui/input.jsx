import * as React from "react";
import { cn } from "@/lib/utils";

/**
 * ShadCN UI → Input
 */
export const Input = React.forwardRef(
  ({ className, type = "text", ...props }, ref) => (
    <input
      ref={ref}
      type={type}
      className={cn(
        "flex h-10 w-full rounded-md border border-input bg-white px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-1 focus:ring-primary disabled:cursor-not-allowed disabled:opacity-50",
        className
      )}
      {...props}
    />
  )
);
Input.displayName = "Input";

/*
focus:outline-none	Removes default outline
focus:outline-pink-500	Sets outline color to pink (if not removed)
focus:ring-2	Sets ring thickness
focus:ring-primary	Applies ring color using theme value
*/
