import { NavLink } from "react-router-dom";
import { useEffect } from "react";
import {
  Menu,
  LogOut,
  Settings,
  LayoutDashboard,
  Info,
  User,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import Avatar from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
} from "@/components/ui/dropdown-menu";
import { Sheet, SheetTrigger, SheetContent } from "@/components/ui/sheet";
import useAuth from "@/context/AuthContext";

function NavLinks({ user, onNavigate }) {
  return (
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
          to="/about"
          className={({ isActive }) =>
            isActive
              ? "text-primary font-medium"
              : "hover:text-primary transition-colors"
          }
        >
          <Info className="h-4 w-4 md:hidden" /> About
        </NavLink>
      </li>

      {user && (
        <li>
          <NavLink
            onClick={onNavigate}
            to="/dashboard"
            className={({ isActive }) =>
              isActive
                ? "text-primary font-medium"
                : "hover:text-primary transition-colors"
            }
          >
            <LayoutDashboard className="h-4 w-4 md:hidden" /> Dashboard
          </NavLink>
        </li>
      )}
    </ul>
  );
}

function Navbar() {
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

        {/* Desktop */}
        <nav className="hidden md:flex items-center gap-6">
          <NavLinks user={user} />
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
                  onSelect={logout}
                  // onClick={logout}
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

        {/* Mobile */}
        <Sheet>
          <SheetTrigger asChild>
            <Button
              variant="ghost"
              className="md:hidden rounded-md p-2 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-primary"
              aria-label="Open navigation menu"
            >
              <Menu className="h-6 w-6" />
            </Button>
          </SheetTrigger>

          <SheetContent>
            <NavLinks user={user} onNavigate={() => {}} />

            <div className="mt-6">
              {user ? (
                <div className="flex flex-col gap-4">
                  <div className="flex items-center gap-3">
                    <Avatar name={user.name} src={user.imageUrl} />
                    <span className="font-medium">{user.name}</span>
                  </div>

                  <Button
                    variant="ghost"
                    onClick={logout}
                    className="justify-start gap-2"
                  >
                    <LogOut className="h-4 w-4" /> Logout
                  </Button>

                  <Button
                    asChild
                    variant="ghost"
                    className="justify-start gap-2"
                  >
                    <NavLink to="/settings">
                      <Settings className="h-4 w-4" /> Settings
                    </NavLink>
                  </Button>
                </div>
              ) : (
                <Button asChild variant="secondary" className="w-full">
                  <NavLink to="/signin">Sign In</NavLink>
                </Button>
              )}
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </header>
  );
}

export default Navbar;
