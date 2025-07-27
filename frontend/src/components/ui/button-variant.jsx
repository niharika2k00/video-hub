import { cva } from "class-variance-authority";

const ButtonVariants = cva(
  "inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 ring-offset-background",
  {
    variants: {
      variant: {
        default: "bg-primary/90 text-white hover:bg-primary",
        secondary: "bg-secondary text-white hover:bg-secondary/90",
        outline: "border border-input hover:bg-accent",
        ghost: "hover:bg-accent",
        link: "underline-offset-4 hover:underline text-primary",
        success: "bg-green-500 text-white hover:bg-green-600",
        destructive: "bg-red-500 text-white hover:bg-red-600",
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
