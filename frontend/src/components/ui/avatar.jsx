import { cn } from "@/lib/utils";

/*
 * Avatar – circular.  If `src` fails or not provided, show initials.
 * Props: { name?:string, src?:string, className?:string, size?:number }
 */
export const Avatar = ({ name = "", src, className = "", size = 32 }) => {
  const initials = name
    .split(" ")
    .filter(Boolean)
    .map((n) => n[0])
    .join("")
    .slice(0, 2)
    .toUpperCase();

  const dimension = `${size}px`;

  return src ? (
    <img
      src={src}
      alt={name}
      className={cn("rounded-full object-cover", className)}
      style={{ width: dimension, height: dimension }}
      onError={(e) => {
        e.currentTarget.onerror = null; // stop loop
        e.currentTarget.src = "";
      }}
    />
  ) : (
    <span
      className={cn(
        "inline-flex items-center justify-center rounded-full bg-primary text-white font-semibold",
        className
      )}
      style={{ width: dimension, height: dimension }}
    >
      {initials || "U"}
    </span>
  );
};
