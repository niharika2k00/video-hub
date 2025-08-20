import { useLocation, useNavigate } from "react-router-dom";
import useAuth from "@/context/AuthContext";

const ProtectedRoute = ({ children }) => {
  const { user } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  // Redirect to signin page, but save the intended destination
  if (!user) {
    // <Navigate to="/signin" state={{ from: location }} replace />;
    navigate("/signin", { replace: true });
    return null;
  }

  return children;
};

export default ProtectedRoute;
