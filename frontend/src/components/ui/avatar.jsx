import * as React from "react";
import { cn } from "@/lib/utils";

const AvatarRoot = React.forwardRef(({ className, ...props }, ref) => {
  return (
    <span
      ref={ref}
      className={cn(
        "relative inline-flex h-10 w-10 shrink-0 overflow-hidden rounded-full bg-muted",
        className
      )}
      {...props}
    />
  );
});

const AvatarImage = ({ className, ...props }) => (
  <img className={cn("h-full w-full object-contain", className)} {...props} />
);

const AvatarFallback = ({ className, children }) => (
  <span
    className={cn(
      "flex h-full w-full items-center justify-center rounded-full font-medium",
      className
    )}
  >
    {children}
  </span>
);

/* ─── Smart <Avatar> wrapper ──────────────────────────────────── */
const Avatar = ({
  src,
  name = "User",
  className,
  imgClassName,
  initialsSize,
  ...props
}) => {
  /* produce initials e.g. “Niharika Dutta” -> “ND” */
  const initials = React.useMemo(
    () =>
      name
        .split(" ")
        .filter(Boolean)
        .map((n) => n[0])
        .join("")
        .slice(0, 2)
        .toUpperCase(),
    [name]
  );

  return (
    <AvatarRoot className={className} {...props}>
      {src ? (
        <AvatarImage src={src} alt={name} className={imgClassName} />
      ) : (
        <AvatarFallback
          className={cn(
            "bg-indigo-100 text-indigo-600",
            initialsSize // apply dynamic size
          )}
        >
          {initials}
        </AvatarFallback>
      )}
    </AvatarRoot>
  );
};

export { AvatarRoot, AvatarImage, AvatarFallback };
export default Avatar;
