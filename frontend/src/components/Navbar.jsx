import { NavLink } from "react-router-dom";
import { useEffect } from "react";
import { Menu, User, LogOut, Settings } from "lucide-react";
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
        to="/#about"
        className="hover:text-primary transition-colors"
      >
        About
      </NavLink>
    </li>
  </ul>
);

export default function Navbar() {
  const { user, logout } = useAuth();

  useEffect(() => {
    console.log("user:", user);
  }, [user]);

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
                  <Avatar name={user.name} src={user.imageUrl} />
                  <span className="font-medium">{user.name}</span>
                </button>
              </DropdownMenuTrigger>

              <DropdownMenuContent>
                <DropdownMenuItem asChild>
                  <NavLink to="/profile" className="flex items-center gap-2">
                    <User className="h-4 w-4" />
                    Profile
                  </NavLink>
                </DropdownMenuItem>

                <DropdownMenuItem asChild>
                  <NavLink to="/settings" className="flex items-center gap-2">
                    <Settings className="h-4 w-4" />
                    Settings
                  </NavLink>
                </DropdownMenuItem>

                <DropdownMenuItem
                  // onSelect={logout}
                  onClick={logout}
                  className="flex items-center gap-2"
                >
                  <LogOut className="h-4 w-4" />
                  Logout
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <Button asChild size="sm" variant="secondary">
              <NavLink to="/signin">Sign In</NavLink>
            </Button>
          )}
        </nav>

        {/* Mobile nav hamburger */}
        <Sheet>
          <SheetTrigger asChild className="md:hidden">
            <Button variant="ghost" size="icon">
              <Menu className="h-7 w-7" />
            </Button>
          </SheetTrigger>
          <SheetContent>
            <div className="flex flex-col gap-4 mt-4">
              <NavLinks
                onNavigate={() =>
                  document.querySelector('[role="dialog"] button')?.click()
                }
              />
              {user ? (
                <div className="flex items-center gap-2 p-2">
                  <Avatar name={user.name} src={user.imageUrl} />
                  <span className="font-medium">{user.name}</span>
                </div>
              ) : (
                <NavLink to="/signin" className="w-full">
                  <Button asChild variant="secondary" className="w-full">
                    Sign In
                  </Button>
                </NavLink>
              )}
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </header>
  );
}
