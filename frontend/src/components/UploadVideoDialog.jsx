import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import api from "@/utils/api";
import { toast } from "react-toastify";
import {
  Dialog,
  DialogTrigger,
  DialogDescription,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";

/* ---------------------------- validation schema ----------------------------- */
const schema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().min(1, "Description is required"),
  category: z.string().min(1, "Category is required"),
  videoFile: z.any().refine((f) => {
    console.log("File validation:", f);
    if (!f || !f[0]) return false;

    const file = f[0];
    console.log("File details:", {
      name: file.name,
      type: file.type,
      size: file.size,
      isFile: file instanceof File,
    });
    return file instanceof File && file.size > 0;
  }, "Please select a valid video file"),
});

const UploadVideoDialog = ({ onSuccess, children }) => {
  const [open, setOpen] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm({ resolver: zodResolver(schema) });

  /* ---------------------------- submit handler ----------------------------- */
  const onSubmit = async (data) => {
    const formData = new FormData();
    formData.append("title", data.title);
    formData.append("description", data.description);
    formData.append("category", data.category);
    formData.append("videoFile", data.videoFile[0]);

    try {
      await api.post("/video/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      toast.success("Video uploaded ✅");
      reset();
      setOpen(false);
      onSuccess?.();
    } catch (err) {
      toast.error(err?.response?.data?.message || "Upload failed");
    }
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      {children ? (
        children({ open: () => setOpen(true) })
      ) : (
        <DialogTrigger asChild>
          <Button variant="secondary">Upload video</Button>
        </DialogTrigger>
      )}

      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle>Upload a new video</DialogTitle>
          <DialogDescription className="text-sm text-gray-600">
            All fields are required. Maximum file size: 150 MB
          </DialogDescription>
        </DialogHeader>

        <form
          id="upload-form"
          className="space-y-4"
          onSubmit={handleSubmit(onSubmit)}
        >
          {/* Title */}
          <div className="grid gap-1">
            <Label htmlFor="title">Title</Label>
            <Input id="title" {...register("title")} />
            {errors.title && (
              <p className="text-sm text-red-500">{errors.title.message}</p>
            )}
          </div>

          {/* Description */}
          <div className="grid gap-1">
            <Label htmlFor="description">Description</Label>
            <Textarea id="description" rows={3} {...register("description")} />
            {errors.description && (
              <p className="text-sm text-red-500">
                {errors.description.message}
              </p>
            )}
          </div>

          {/* Category */}
          <div className="grid gap-1">
            <Label htmlFor="category">Category</Label>
            <Input id="category" {...register("category")} />
            {errors.category && (
              <p className="text-sm text-red-500">{errors.category.message}</p>
            )}
          </div>

          {/* Video file */}
          <div className="grid gap-1">
            <Label htmlFor="videoFile">Video file</Label>
            <Input
              id="videoFile"
              type="file"
              accept="video/*"
              {...register("videoFile")}
            />
            {errors.videoFile && (
              <p className="text-sm text-red-500">{errors.videoFile.message}</p>
            )}
          </div>
        </form>

        <DialogFooter>
          <Button type="submit" form="upload-form" disabled={isSubmitting}>
            {isSubmitting ? "Uploading…" : "Upload"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default UploadVideoDialog;
