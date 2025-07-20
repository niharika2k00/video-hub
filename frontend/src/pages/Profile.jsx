// src/pages/Profile.jsx
import { useState, useCallback, useEffect, useRef } from "react";
import { Edit3, Camera, ChevronDown } from "lucide-react";
import useAuth from "@/context/AuthContext";
import api from "@/utils/api";
import { toast } from "react-toastify";

import Avatar from "@/components/ui/avatar";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";

// Single gender mapping object
const GENDER_MAPPING = {
  FEMALE: "female",
  MALE: "male",
  PREFER_NOT_TO_SAY: "prefer_not_to_say",
};

const Profile = () => {
  const { user, setUser } = useAuth();

  const [form, setForm] = useState({});
  const [editingField, setEditing] = useState(null); // only ONE active
  const [submitting, setSubmitting] = useState(false);
  const [profileImageFile, setProfileImageFile] = useState(null); // Store the file object
  const [profileImagePreview, setProfileImagePreview] = useState(null); // Store the preview URL

  /* Reset form when auth-user changes */
  useEffect(() => {
    if (user) {
      setForm({
        name: user.name ?? "",
        email: user.email ?? "",
        password: "**********" || "",
        age: user.age ?? "",
        gender: GENDER_MAPPING[user.gender] ?? user.gender ?? "",
        location: user.location ?? "",
        phoneNumber: user.phoneNumber ?? "",
        bio: user.bio ?? "",
      });
      // Reset image states when user changes
      setProfileImageFile(null);
      setProfileImagePreview(null);
    }
  }, [user]);

  const handleChange = (k, v) => setForm((p) => ({ ...p, [k]: v }));

  const handleSubmit = useCallback(async () => {
    setSubmitting(true);
    try {
      const requestBody = {
        ...user,
        ...form,
        gender:
          Object.keys(GENDER_MAPPING).find(
            (key) => GENDER_MAPPING[key] === form.gender
          ) ?? form.gender,
      };

      // remove password from request body if its empty or unchanged
      if (
        requestBody.password === "**********" ||
        requestBody.password === ""
      ) {
        delete requestBody.password;
      }

      // If user uploads a new profile image
      if (profileImageFile && profileImageFile instanceof File) {
        requestBody.profileImage = profileImageFile;
      } else {
        delete requestBody.profileImage; // removing this field since its a string (S3 url), whereas requestbody expects MultipartFile.[Refer backend controller code]
      }

      // If requestBody has no file it can be directly sent as JSON. But here it must be converted to FormData as the content-type is multipart/form-data
      var formData = new FormData();
      Object.keys(requestBody).forEach((key) => {
        formData.append(key, requestBody[key]);
      });

      console.log("Request Body:", requestBody);
      const { data: updated } = await api.put(`/users/${user.id}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      console.log(updated);
      setProfileImageFile(null);
      setProfileImagePreview(null);
      setEditing(null);
      toast.success("Profile updated ✅");
    } catch (err) {
      console.log(err);
      toast.error(err?.response?.data?.message || "Profile update failed");
    } finally {
      setSubmitting(false);
      setTimeout(() => {
        location.reload();
      }, 10000);
    }
  }, [form, user, setUser, profileImageFile]);

  const uploadPicture = async (file) => {
    if (!file) return;
    console.log("file:", file);

    // Create a preview URL for immediate display
    const previewUrl = URL.createObjectURL(file);

    setProfileImageFile(file);
    setProfileImagePreview(previewUrl);

    toast.success("Picture selected 📸");
  };

  const Field = ({
    id,
    label,
    type = "text",
    textarea,
    isDropdown,
    options,
  }) => {
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
      style: { direction: "ltr", unicodeBidi: "plaintext" },
      dir: "ltr",
    };

    return (
      <div className="flex flex-col gap-1">
        <Label htmlFor={id} className="text-sm font-medium">
          {label}
        </Label>

        <div className="relative">
          {textarea ? (
            <Textarea rows={4} {...sharedProps} />
          ) : isDropdown ? (
            <div className="relative">
              <select
                {...sharedProps}
                className={`${sharedProps.className} appearance-none pr-10 border`}
                disabled={false}
              >
                {options?.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              <ChevronDown className="absolute right-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400 pointer-events-none" />
            </div>
          ) : (
            <Input type={type} {...sharedProps} />
          )}

          {/* edit button - only show for non-dropdown fields */}
          {!isDropdown && (
            <button
              type="button"
              onClick={() => setEditing(id)}
              className={`absolute right-2 top-2 text-gray-400 hover:text-indigo-500 ${
                editingField === id ? "text-indigo-600" : ""
              }`}
            >
              <Edit3 size={16} />
            </button>
          )}
        </div>
      </div>
    );
  };

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
              src={profileImagePreview || user?.profileImage} // Use preview first, then fallback to user's image
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
            <Field
              id="gender"
              label="Gender"
              isDropdown={true}
              options={[
                { value: "male", label: "Male" },
                { value: "female", label: "Female" },
                { value: "prefer_not_to_say", label: "Prefer not to say" },
              ]}
            />
            <Field id="location" label="Location" />
            <Field id="phoneNumber" label="Phone number" />
            <Field id="bio" label="Bio" textarea />
          </div>

          <div className="flex justify-center mt-8">
            <Button type="submit" disabled={submitting}>
              {submitting ? "Saving..." : "Save changes"}
            </Button>
          </div>
        </form>
      </div>
    </section>
  );
};

export default Profile;
