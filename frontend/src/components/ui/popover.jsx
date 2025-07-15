import * as PopoverPrimitive from "@radix-ui/react-popover";
import { cn } from "@/lib/utils";

export const Popover = PopoverPrimitive.Root;
export const PopoverTrigger = PopoverPrimitive.Trigger;

/* Panel */
export function PopoverContent({
  className,
  align = "center",
  sideOffset = 8,
  ...props
}) {
  return (
    <PopoverPrimitive.Portal>
      <PopoverPrimitive.Content
        align={align}
        sideOffset={sideOffset}
        className={cn(
          "z-50 w-auto rounded-md border border-gray-200 bg-white p-4 shadow-md",
          "dark:border-gray-700 dark:bg-gray-800",
          className
        )}
        {...props}
      />
    </PopoverPrimitive.Portal>
  );
}
