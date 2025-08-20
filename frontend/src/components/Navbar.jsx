import { NavLink } from "react-router-dom";
import { useEffect } from "react";
import {
  Menu,
  LogOut,
  Settings,
  LayoutDashboard,
  Info,
  User,
  Crown,
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
          // boolean isActive becomes true if the current route is same as the "to" link, this is how react router works
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

      <li>
        <NavLink
          onClick={onNavigate}
          to="/contact"
          className={({ isActive }) =>
            isActive
              ? "text-primary font-medium"
              : "hover:text-primary transition-colors"
          }
        >
          <Info className="h-4 w-4 md:hidden" /> Contact
        </NavLink>
      </li>
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

              <DropdownMenuContent className="w-64 p-2 mt-2 bg-white/95 backdrop-blur-sm border border-gray-200/50 shadow-xl rounded-xl">
                {/* Menu Items */}
                <div className="space-y-1">
                  <DropdownMenuItem asChild>
                    <NavLink
                      to="/profile"
                      className="flex items-center gap-3 px-3 py-2.5 text-sm text-gray-700 hover:text-primary hover:bg-primary/5 rounded-lg transition-all duration-200 cursor-pointer"
                    >
                      <div className="w-8 h-8 bg-blue-50 rounded-lg flex items-center justify-center">
                        <User className="h-4 w-4 text-blue-600" />
                      </div>
                      <div className="flex-1">
                        <p className="font-medium">Profile</p>
                        <p className="text-xs text-gray-500">
                          Manage your account
                        </p>
                      </div>
                    </NavLink>
                  </DropdownMenuItem>

                  <DropdownMenuItem asChild>
                    <NavLink
                      to="/dashboard"
                      className="flex items-center gap-3 px-3 py-2.5 text-sm text-gray-700 hover:text-primary hover:bg-primary/5 rounded-lg transition-all duration-200 cursor-pointer"
                    >
                      <div className="w-8 h-8 bg-purple-50 rounded-lg flex items-center justify-center">
                        <LayoutDashboard className="h-4 w-4 text-purple-600" />
                      </div>
                      <div className="flex-1">
                        <p className="font-medium">Dashboard</p>
                        <p className="text-xs text-gray-500">
                          View your videos
                        </p>
                      </div>
                    </NavLink>
                  </DropdownMenuItem>

                  {/* TODO: add settings page */}
                  {/* <DropdownMenuItem asChild>
                    <NavLink
                      to="/settings"
                      className="flex items-center gap-3 px-3 py-2.5 text-sm text-gray-700 hover:text-primary hover:bg-primary/5 rounded-lg transition-all duration-200 cursor-pointer"
                    >
                      <div className="w-8 h-8 bg-gray-50 rounded-lg flex items-center justify-center">
                        <Settings className="h-4 w-4 text-gray-600" />
                      </div>
                      <div className="flex-1">
                        <p className="font-medium">Settings</p>
                        <p className="text-xs text-gray-500">
                          Customize preferences
                        </p>
                      </div>
                    </NavLink>
                  </DropdownMenuItem> */}

                  <DropdownMenuItem asChild>
                    <NavLink
                      to="/"
                      className="flex items-center gap-3 px-3 py-2.5 text-sm text-amber-700 hover:text-amber-800 hover:bg-amber-50 rounded-lg transition-all duration-200 cursor-pointer"
                    >
                      <div className="w-8 h-8 bg-amber-50 rounded-lg flex items-center justify-center">
                        <Crown className="h-4 w-4 text-amber-600" />
                      </div>
                      <div className="flex-1">
                        <p className="font-medium">Upgrade to Pro</p>
                        <p className="text-xs text-amber-600">
                          Unlock premium features
                        </p>
                      </div>
                    </NavLink>
                  </DropdownMenuItem>
                </div>

                {/* Logout Section */}
                <DropdownMenuItem
                  onSelect={logout}
                  className="flex items-center gap-3 px-3 py-2.5 text-sm text-red-600 hover:text-red-700 hover:bg-red-50 rounded-lg transition-all duration-200 cursor-pointer"
                >
                  <div className="w-8 h-8 bg-red-50 rounded-lg flex items-center justify-center">
                    <LogOut className="h-4 w-4 text-red-600" />
                  </div>
                  <div className="flex-1">
                    <p className="font-medium">Logout</p>
                    <p className="text-xs text-red-500">
                      Sign out of your account
                    </p>
                  </div>
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <Button
              asChild
              size="sm"
              variant="secondary"
              className="px-6 py-2.5 rounded-lg font-medium hover:bg-primary/86 transition-all duration-200"
            >
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

                  <Button
                    asChild
                    variant="ghost"
                    className="justify-start gap-2 text-red-600 font-bold hover:text-amber-600 hover:bg-amber-100"
                  >
                    <NavLink to="/">
                      <Crown className="h-4 w-4" />
                      Upgrade to Pro
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
