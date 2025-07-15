// src/pages/Profile.jsx
import { useState, useCallback, useEffect, useRef } from "react";
import { Edit3, Camera } from "lucide-react";
import useAuth from "@/context/AuthContext";
import api from "@/utils/api";
import { toast } from "react-toastify";

import Avatar from "@/components/ui/avatar";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";

export default function Profile() {
  const { user, setUser } = useAuth();

  /* ------------------------------------------------------------------------ */
  /* Local state                                                              */
  /* ------------------------------------------------------------------------ */
  const [form, setForm] = useState({});
  const [editingField, setEditing] = useState(null); // only ONE active
  const [submitting, setSubmitting] = useState(false);

  /* Reset form when auth-user changes */
  useEffect(() => {
    if (user) {
      setForm({
        name: user.name ?? "",
        email: user.email ?? "",
        password: "********" || "",
        age: user.age ?? "",
        location: user.location ?? "",
        phoneNumber: user.phoneNumber ?? "",
        bio: user.bio ?? "",
      });
    }
  }, [user]);

  const handleChange = (k, v) => setForm((p) => ({ ...p, [k]: v }));

  /* ------------------------------------------------------------------------ */
  /* Submit plain profile data                                                */
  /* ------------------------------------------------------------------------ */
  const handleSubmit = useCallback(async () => {
    setSubmitting(true);
    try {
      const { data: updated } = await api.put("/users", {
        id: user.id,
        ...user,
        ...form,
      });
      setUser(updated);
      toast.success("Profile updated ✅");
      setEditing(null);
    } catch (err) {
      toast.error(err?.response?.data?.message || "Update failed");
    } finally {
      setSubmitting(false);
    }
  }, [form, user, setUser]);

  /* ------------------------------------------------------------------------ */
  /* Stub picture upload handler                                              */
  /* ------------------------------------------------------------------------ */
  const uploadPicture = async (file) => {
    if (!file) return;
    // replace endpoint later:
    // const fd = new FormData(); fd.append("file", file); await api.put("/users/picture", fd);
    toast.success("Picture updated (stub) 📸");
  };

  /* ------------------------------------------------------------------------ */
  /* Re-usable Field component                                                */
  /* ------------------------------------------------------------------------ */
  const Field = ({ id, label, type = "text", textarea }) => {
    const ref = useRef(null);

    /* focus when this field becomes active */
    useEffect(() => {
      if (editingField === id && ref.current) ref.current.focus();
    }, [editingField, id]);

    const sharedProps = {
      ref,
      id,
      disabled: editingField !== id,
      value: form[id] ?? "",
      onChange: (e) => handleChange(id, e.target.value),
      className: `${
        editingField === id
          ? "ring-2 ring-indigo-300 border-indigo-400"
          : "border-gray-300"
      } w-full rounded-md px-3 py-2 text-sm bg-white focus:outline-none`,
    };

    return (
      <div className="flex flex-col gap-1">
        <Label htmlFor={id} className="text-sm font-medium">
          {label}
        </Label>

        <div className="relative">
          {textarea ? (
            <Textarea rows={4} {...sharedProps} />
          ) : (
            <Input type={type} {...sharedProps} />
          )}

          {/* edit button */}
          <button
            type="button"
            onClick={() => setEditing(id)}
            className={`absolute right-2 top-2 text-gray-400 hover:text-indigo-500 ${
              editingField === id ? "text-indigo-600" : ""
            }`}
          >
            <Edit3 size={16} />
          </button>
        </div>
      </div>
    );
  };

  /* ------------------------------------------------------------------------ */
  /* UI                                                                       */
  /* ------------------------------------------------------------------------ */
  return (
    <section className="min-h-screen container mx-auto px-6 py-10 flex flex-col gap-10">
      <div>
        <h1 className="text-3xl font-bold">Profile</h1>
        <p className="text-gray-500">View and edit your profile details</p>
      </div>

      <div className="flex flex-col lg:flex-row gap-8">
        {/* Profile card */}
        <div className="relative w-full lg:max-w-xs rounded-3xl p-10 bg-gradient-to-br from-indigo-50 via-purple-50 to-pink-50 shadow-sm flex flex-col items-center">
          {/* Avatar + camera */}
          <div className="relative">
            <Avatar
              className="h-48 w-48 ring-4 ring-white"
              initialsSize="text-5xl"
              src={user?.profilePicture}
              name={user?.name}
            />

            <label
              htmlFor="pic-input"
              className="absolute bottom-3 right-3 h-10 w-10 rounded-full bg-pink-500 shadow-md grid place-content-center cursor-pointer hover:bg-pink-600 text-white"
            >
              <Camera size={18} />
            </label>
            <input
              id="pic-input"
              type="file"
              accept="image/*"
              className="sr-only"
              onChange={(e) => uploadPicture(e.target.files?.[0])}
            />
          </div>

          <p className="mt-6 font-semibold text-lg">{user?.name}</p>
        </div>

        {/* Form */}
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSubmit();
          }}
          className="flex-1 bg-white/60 backdrop-blur rounded-3xl p-6 lg:p-10 shadow-sm border border-gray-200"
        >
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Field id="name" label="Name" />
            <Field id="email" label="Email" type="email" />
            <Field id="password" label="Password" type="password" />
            <Field id="age" label="Age" type="number" />
            <Field id="location" label="Location" />
            <Field id="phoneNumber" label="Phone number" />
            <Field id="bio" label="Bio" textarea />
          </div>

          <div className="flex justify-center mt-8">
            <Button type="submit" disabled={submitting}>
              {submitting ? "Saving…" : "Save changes"}
            </Button>
          </div>
        </form>
      </div>
    </section>
  );
}
