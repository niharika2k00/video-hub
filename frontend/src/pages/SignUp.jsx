import { useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { PasswordInput } from "@/components/ui/password-input";
import { Card } from "@/components/ui/card";
import useAuth from "@/context/AuthContext";
import { ChevronDown } from "lucide-react";

export default function SignUp() {
  const GENDER_MAPPING = {
    FEMALE: "female",
    MALE: "male",
    PREFER_NOT_TO_SAY: "prefer_not_to_say",
  };

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    age: "",
    gender: "",
    location: "",
    bio: "",
    phoneNumber: "",
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { register, login } = useAuth();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      console.log(form);
      // convert gender to backend format
      const requestBody = {
        ...form,
        gender:
          Object.keys(GENDER_MAPPING).find(
            (key) => GENDER_MAPPING[key] === form.gender
          ) ?? form.gender,
      };

      await register(requestBody);
      await login(form.email, form.password);
      navigate("/");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4 bg-gray-50">
      <Card className="max-w-md">
        <h1 className="text-2xl font-bold mb-6 text-center">Create Account</h1>
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            name="name"
            placeholder="Name"
            value={form.name}
            onChange={handleChange}
            required
          />
          <Input
            name="email"
            type="email"
            placeholder="Email"
            value={form.email}
            onChange={handleChange}
            required
          />
          <PasswordInput
            name="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
            required
          />
          <Input
            name="age"
            placeholder="Age"
            value={form.age}
            onChange={handleChange}
          />
          {/* Gender Dropdown */}
          <div className="relative">
            <select
              name="gender"
              value={form.gender}
              onChange={handleChange}
              className="w-full appearance-none rounded-md border border-gray-300 px-3 py-2 text-sm bg-white focus:outline-none focus:border-primary focus:ring-2 focus:ring-primary/20"
            >
              <option value="">Select Gender</option>
              <option value="male">Male</option>
              <option value="female">Female</option>
              <option value="prefer_not_to_say">Prefer not to say</option>
            </select>
            <ChevronDown className="absolute right-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400 pointer-events-none" />
          </div>
          <Input
            name="location"
            placeholder="Location"
            value={form.location}
            onChange={handleChange}
          />
          <Input
            name="bio"
            placeholder="Bio"
            value={form.bio}
            onChange={handleChange}
          />
          <Input
            name="phoneNumber"
            placeholder="Phone Number"
            value={form.phoneNumber}
            onChange={handleChange}
          />
          <Button type="submit" className="w-full" disabled={loading}>
            {loading ? "Signing Up…" : "Sign Up"}
          </Button>
        </form>

        <p className="text-sm text-center mt-6">
          Already have an account?{" "}
          <NavLink to="/signin" className="text-primary hover:underline">
            Sign In
          </NavLink>
        </p>
      </Card>
    </div>
  );
}
