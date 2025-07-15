import { useEffect, useState, useCallback } from "react";
import api from "@/utils/api";
import useAuth from "@/context/AuthContext";
import VideoCard from "@/components/VideoCard";
import { PlusCircle } from "lucide-react"; // already in shadcn stack
import UploadVideoDialog from "@/components/UploadVideoDialog";
import { toast } from "react-toastify";

const Dashboard = () => {
  const { user } = useAuth();
  const [videos, setVideos] = useState(null); // null ⇒ loading
  const [error, setError] = useState("");

  const refetch = useCallback(async () => {
    if (!user?.id) return;
    try {
      const { data } = await api.get(`/videos?authorId=${user.id}`);
      console.log("videos uploaded by the user:", data);
      setVideos(data);
      setError("");
    } catch (e) {
      setError("Failed to load videos");
      toast.error("Cannot fetch videos");
      setVideos([]);
    }
  }, [user?.id]);

  useEffect(() => {
    refetch();
  }, [refetch]);

  return (
    <div className="container mx-auto px-6 py-10">
      <div className="mb-8 flex items-center justify-between">
        <h1 className="text-3xl font-bold">My Videos</h1>
      </div>

      {error && <p className="text-center text-red-500 my-4">{error}</p>}

      <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
        {/* when upload succeeds, refetch() runs → grid refreshes */}
        <UploadVideoDialog onSuccess={refetch}>
          {({ open } /* render-prop trigger */) => (
            <button
              onClick={open}
              className="group relative flex h-48 w-full items-center
                     justify-center rounded-xl border border-dashed
                     border-primary/50 transition hover:bg-primary/5"
            >
              <PlusCircle
                size={42}
                className="text-primary transition group-hover:scale-110"
              />
              <span className="absolute top-2 right-3 text-xs text-primary/70">
                Upload New Video
              </span>
            </button>
          )}
        </UploadVideoDialog>

        {(videos ?? Array.from({ length: 8 })).map((video, idx) => (
          <VideoCard key={video?.id || idx} video={video} />
        ))}
      </div>

      {videos && videos.length === 0 && (
        <p className="text-center text-gray-500 mt-12">
          You haven't uploaded any videos yet.
        </p>
      )}
    </div>
  );
};

export default Dashboard;
