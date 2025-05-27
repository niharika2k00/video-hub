import * as React from "react";
import * as DialogPrimitive from "@radix-ui/react-dialog";
import { X } from "lucide-react";
import { cn } from "@/lib/utils";

export const Sheet = DialogPrimitive.Root;
export const SheetTrigger = DialogPrimitive.Trigger;

export const SheetContent = React.forwardRef(
  ({ className, children, ...props }, ref) => (
    <DialogPrimitive.Portal>
      <DialogPrimitive.Overlay className="fixed inset-0 z-40 bg-black/50" />
      <DialogPrimitive.Content
        ref={ref}
        className={cn(
          "fixed inset-y-0 left-0 z-50 w-64 bg-white shadow-xl transition-transform data-[state=open]:translate-x-0 data-[state=closed]:-translate-x-full",
          className
        )}
        {...props}
      >
        <div className="flex items-center justify-between p-4 border-b">
          <span className="font-bold text-lg">Menu</span>
          <DialogPrimitive.Close asChild>
            <button
              aria-label="Close"
              className="rounded-md p-1 hover:bg-gray-100"
            >
              <X className="h-5 w-5" />
            </button>
          </DialogPrimitive.Close>
        </div>
        <div className="px-4 py-2">{children}</div>
      </DialogPrimitive.Content>
    </DialogPrimitive.Portal>
  )
);
SheetContent.displayName = "SheetContent";
