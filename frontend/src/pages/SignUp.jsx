import { useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { PasswordInput } from "@/components/ui/password-input";
import { Card } from "@/components/ui/card";
import useAuth from "@/context/AuthContext";

export default function SignUp() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    age: "",
    location: "",
    bio: "",
    phoneNumber: "",
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { register } = useAuth();

  // (e) => setName(e.target.value)
  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await register(form);
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
