import { cva } from "class-variance-authority";

const ButtonVariants = cva(
  "inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 ring-offset-background",
  {
    variants: {
      variant: {
        default: "bg-[#6366F1] text-white hover:bg-[#6366F1]/90",
        secondary: "bg-[#EC4899] text-white hover:bg-[#EC4899]/90",
        outline: "border border-input hover:bg-accent",
        ghost: "hover:bg-accent",
        link: "underline-offset-4 hover:underline text-[#6366F1]",
        success: "bg-[#FF6B6B] text-white hover:bg-[#FF6B6B]-400",

        //  ACTUAL
        // default: "bg-primary text-white hover:bg-primary/90",
        // secondary: "bg-secondary text-black hover:bg-secondary/90",
        // outline: "border border-input hover:bg-accent",
        // ghost: "hover:bg-accent",
        // link: "underline-offset-4 hover:underline text-primary",
        // success: "bg-[#FF6B6B] text-white hover:bg-[#FF6B6B]-400",
      },
      size: {
        default: "h-10 px-4 py-2",
        sm: "h-9 rounded-md px-3",
        lg: "h-11 rounded-md px-8",
        icon: "h-10 w-10",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
);

export default ButtonVariants;
