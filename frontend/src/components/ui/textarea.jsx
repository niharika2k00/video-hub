import { forwardRef } from "react";
import { cn } from "@/lib/utils";
export const Textarea = forwardRef(function Textarea(
  { className, ...props },
  ref
) {
  return (
    <textarea
      ref={ref}
      className={cn(
        "w-full resize-none rounded-md border border-gray-300 bg-white px-3 py-2",
        "text-sm outline-none focus:border-primary focus:ring-2 focus:ring-primary/20",
        className
      )}
      {...props}
    />
  );
});
