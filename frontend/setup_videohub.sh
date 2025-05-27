#!/bin/bash
# ------------------------------------------------------------
# VIDEOHUB – Logout API • Password-eye • Avatar w/ initials
# ------------------------------------------------------------
# WHAT THIS PATCH DOES
# 1)  Adds logout API call  →  src/services/authService.js
# 2)  Updates AuthContext.logout to call that API but keeps
#     the structure EXACTLY like your latest file
# 3)  Introduces <PasswordInput> (eye toggle) and swaps it
#     into SignIn / SignUp pages
# 4)  Adds <Avatar> that shows user.image or initials fallback
# 5)  Re-styles Navbar to show Avatar + username + dropdown
# ------------------------------------------------------------
set -euo pipefail

echo "🚀  Patching auth & UI …"

cd client

# ----------------------------------------------------------------
# 1)  authService – add logoutApi
# ----------------------------------------------------------------
cat > src/services/authService.js <<'EOF'
import api from "@/utils/api";

export const login = async (payload) => {
  const { data } = await api.post("/auth/login", payload);
  return data; // { token }
};

export const register = async (payload) => {
  const { data } = await api.post("/auth/register", payload);
  return data; // { token }
};

/**
 * logoutApi – POST raw Bearer token as text/plain
 */
export const logoutApi = async (token) =>
  api.post("/auth/logout", token, {
    headers: { "Content-Type": "text/plain" },
  });
EOF

# ----------------------------------------------------------------
# 2)  AuthContext – patch only the logout() fn
# ----------------------------------------------------------------
# Create a temporary file with the new logout function
cat > /tmp/new_logout.js <<'EOF'
  const logout = async () => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        // fire-and-forget; ignore any network error so UI always logs out
        await import("@/services/authService").then(m => m.logoutApi(`Bearer ${token}`)).catch(() => {});
      }
    } finally {
      localStorage.removeItem("token");
      delete api.defaults.headers.common.Authorization;
      setUser(null);
      setError(null);
    }
  };
EOF

# Use awk to replace the old logout function with the new one
awk '
  /const logout = () => {/ {
    print "  const logout = async () => {";
    print "    try {";
    print "      const token = localStorage.getItem(\"token\");";
    print "      if (token) {";
    print "        // fire-and-forget; ignore any network error so UI always logs out";
    print "        await import(\"@/services/authService\").then(m => m.logoutApi(`Bearer ${token}`)).catch(() => {});";
    print "      }";
    print "    } finally {";
    print "      localStorage.removeItem(\"token\");";
    print "      delete api.defaults.headers.common.Authorization;";
    print "      setUser(null);";
    print "      setError(null);";
    print "    }";
    print "  };";
    skip = 1;
    next;
  }
  skip && /^  };/ {
    skip = 0;
    next;
  }
  !skip {
    print;
  }
' src/context/AuthContext.jsx > /tmp/AuthContext.jsx.new && mv /tmp/AuthContext.jsx.new src/context/AuthContext.jsx

# Clean up
rm /tmp/new_logout.js

# ----------------------------------------------------------------
# 3)  UI primitives – Avatar & PasswordInput
# ----------------------------------------------------------------
mkdir -p src/components/ui

cat > src/components/ui/avatar.jsx <<'EOF'
import { cn } from "@/lib/utils";

/**
 * Avatar – circular.  If `src` fails or not provided, show initials.
 *
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
      className={cn(
        "rounded-full object-cover",
        className
      )}
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
EOF

cat > src/components/ui/password-input.jsx <<'EOF'
import * as React from "react";
import { Eye, EyeOff } from "lucide-react";
import { Input } from "./input";
import { cn } from "@/lib/utils";

/**
 * PasswordInput – wraps <Input> and adds show/hide eye icon.
 */
export const PasswordInput = React.forwardRef(
  ({ className, ...props }, ref) => {
    const [show, setShow] = React.useState(false);
    return (
      <div className="relative">
        <Input
          ref={ref}
          type={show ? "text" : "password"}
          className={cn("pr-10", className)}
          {...props}
        />
        <button
          type="button"
          tabIndex={-1}
          onClick={() => setShow((v) => !v)}
          className="absolute inset-y-0 right-0 flex items-center px-3 text-gray-500 hover:text-gray-700 focus:outline-none"
        >
          {show ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
        </button>
      </div>
    );
  }
);
PasswordInput.displayName = "PasswordInput";
EOF

# ----------------------------------------------------------------
# 4)  Swap PasswordInput into SignIn & SignUp pages
# ----------------------------------------------------------------
# Update SignIn.jsx
sed -i.bak -e 's/import { Input } from "@\/components\/ui\/input";/import { Input } from "@\/components\/ui\/input";\nimport { PasswordInput } from "@\/components\/ui\/password-input";/' src/pages/SignIn.jsx
sed -i.bak -e 's/<Input\([^>]*name="password"[^>]*\)><\/Input>/<PasswordInput\1 \/>/g' src/pages/SignIn.jsx
sed -i.bak -e 's/<Input\([^>]*name="password"[^>]*\)\/>/<PasswordInput\1 \/>/g' src/pages/SignIn.jsx
rm src/pages/SignIn.jsx.bak

# Update SignUp.jsx
sed -i.bak -e 's/import { Input } from "@\/components\/ui\/input";/import { Input } from "@\/components\/ui\/input";\nimport { PasswordInput } from "@\/components\/ui\/password-input";/' src/pages/SignUp.jsx
sed -i.bak -e 's/<Input\([^>]*name="password"[^>]*\)><\/Input>/<PasswordInput\1 \/>/g' src/pages/SignUp.jsx
sed -i.bak -e 's/<Input\([^>]*name="password"[^>]*\)\/>/<PasswordInput\1 \/>/g' src/pages/SignUp.jsx
rm src/pages/SignUp.jsx.bak

# ----------------------------------------------------------------
# 5)  Update Navbar – Avatar + username + dropdown
# ----------------------------------------------------------------
cat > src/components/Navbar.jsx <<'EOF'
import { NavLink } from "react-router-dom";
import { Menu, LogOut, Settings } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Avatar } from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
} from "@/components/ui/dropdown-menu";
import { Sheet, SheetTrigger, SheetContent } from "@/components/ui/sheet";
import useAuth from "@/context/AuthContext";

const NavLinks = ({ onNavigate }) => (
  <ul className="flex flex-col md:flex-row gap-6 md:gap-4 text-lg md:text-sm">
    <li>
      <NavLink
        onClick={onNavigate}
        to="/"
        className={({ isActive }) =>
          isActive
            ? "text-primary font-medium"
            : "hover:text-primary transition-colors"
        }
      >
        Home
      </NavLink>
    </li>
    <li>
      <NavLink
        onClick={onNavigate}
        to="/#features"
        className="hover:text-primary transition-colors"
      >
        Features
      </NavLink>
    </li>
  </ul>
);

export default function Navbar() {
  const { user, logout } = useAuth();

  return (
    <header className="bg-white/80 backdrop-blur sticky top-0 z-50 shadow-sm">
      <div className="container mx-auto flex items-center justify-between py-4 px-6">
        {/* Brand */}
        <NavLink
          to="/"
          className="text-2xl font-extrabold tracking-wide select-none"
        >
          <span className="text-primary">VIDEO</span>HUB
        </NavLink>

        {/* Desktop nav */}
        <nav className="hidden md:flex items-center gap-6">
          <NavLinks />
          {user ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <button className="flex items-center gap-2 focus:outline-none">
                  <Avatar
                    name={user.name}
                    src={user.imageUrl /* optional backend field */}
                  />
                  <span className="font-medium">{user.name}</span>
                </button>
              </DropdownMenuTrigger>
              <DropdownMenuContent>
                <DropdownMenuItem asChild>
                  <NavLink to="/settings" className="flex items-center gap-2">
                    <Settings className="h-4 w-4" />
                    Settings
                  </NavLink>
                </DropdownMenuItem>
                <DropdownMenuItem onClick={logout} className="flex items-center gap-2">
                  <LogOut className="h-4 w-4" />
                  Logout
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <NavLink to="/signin">
              <Button>Sign In</Button>
            </NavLink>
          )}
        </nav>

        {/* Mobile nav */}
        <Sheet>
          <SheetTrigger asChild className="md:hidden">
            <Button variant="ghost" size="icon">
              <Menu className="h-5 w-5" />
            </Button>
          </SheetTrigger>
          <SheetContent>
            <div className="flex flex-col gap-4 mt-4">
              <NavLinks onNavigate={() => document.querySelector('[role="dialog"] button')?.click()} />
              {user ? (
                <div className="flex items-center gap-2 p-2">
                  <Avatar name={user.name} src={user.imageUrl} />
                  <span className="font-medium">{user.name}</span>
                </div>
              ) : (
                <NavLink to="/signin" className="w-full">
                  <Button className="w-full">Sign In</Button>
                </NavLink>
              )}
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </header>
  );
}
EOF

echo "✅  Patch complete!"
