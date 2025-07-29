import { useEffect, useState, useCallback } from "react";
import api from "@/utils/api";
import useAuth from "@/context/AuthContext";
import VideoCard from "@/components/VideoCard";
import { PlusCircle, Upload } from "lucide-react";
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

  const handleVideoDelete = useCallback(async (videoId) => {
    try {
      await api.delete(`/videos/${videoId}`);
      toast.success("Video deleted successfully");

      // Remove the deleted video from the state
      setVideos((updatedVideoArray) =>
        updatedVideoArray.filter((item) => item.id !== videoId)
      );
    } catch (error) {
      console.error("Error deleting video:", error);
      toast.error("Failed to delete video");
    }
  }, []);

  useEffect(() => {
    refetch();
  }, [refetch]);

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50">
      {/* Header Section */}
      <section className="relative overflow-hidden bg-gradient-to-r from-blue-600 via-purple-600 to-indigo-700 text-white py-16  pb-22 sm:pb-24 md:pb-30 lg:pb-40 ">
        <div className="absolute inset-0 bg-black/20"></div>
        <div className="text-center relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold mb-6">
            My <span className="text-yellow-300">Videos</span>
          </h1>
          <p className="text-lg md:text-lg lg:text-2xl max-w-3xl text-blue-100 mx-auto my-auto">
            Manage and organize your video content with professional tools
          </p>
        </div>

        {/* Decorative Wave */}
        <div className="absolute bottom-0 left-0 right-0">
          <svg
            viewBox="0 0 1200 120"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M0 0L50 20C100 40 200 80 300 80C400 80 500 40 600 20C700 0 800 0 900 20C1000 40 1100 80 1150 100L1200 120V120H0V0Z"
              fill="rgb(248 250 252)"
            />
          </svg>
        </div>
      </section>

      {/* Main Content */}
      <section className="py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-8">
              <p className="text-red-600 text-center">{error}</p>
            </div>
          )}

          {/* Stats Section */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
            <div className="bg-white rounded-xl p-6 shadow-lg border border-gray-100">
              <div className="flex items-center">
                <div className="w-12 h-12 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg flex items-center justify-center text-white mr-4">
                  <Upload className="w-6 h-6" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Total Videos</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {videos ? videos.length : "..."}
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Videos Grid - adjust the gap and the grid-cols */}
          <div className="grid gap-8 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-3">
            {/* Upload Button */}
            <UploadVideoDialog onSuccess={refetch}>
              {({ open }) => (
                <button
                  onClick={open}
                  className="group relative flex h-48 w-full items-center justify-center rounded-xl border-2 border-dashed border-blue-300 bg-gradient-to-br from-orange-50 to-rose-100 hover:from-blue-100 hover:to-purple-100 transition-all duration-300 hover:scale-105 hover:shadow-lg"
                >
                  <div className="text-center">
                    <div className="w-16 h-16 bg-gradient-to-br from-blue-500 to-purple-600 rounded-2xl flex items-center justify-center text-white mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
                      <PlusCircle className="w-8 h-8" />
                    </div>
                    <p className="text-sm font-medium text-gray-700 mb-1">
                      Upload New Video
                    </p>
                    <p className="text-xs text-gray-500">
                      Click to get started
                    </p>
                  </div>
                </button>
              )}
            </UploadVideoDialog>

            {/* Video Cards */}
            {(videos ?? Array.from({ length: 8 })).map((video, idx) => (
              <VideoCard
                key={video?.id || idx}
                video={video}
                onDelete={handleVideoDelete}
              />
            ))}
          </div>

          {/* Empty State */}
          {videos && videos.length === 0 && (
            <div className="text-center py-16">
              <div className="w-24 h-24 bg-gradient-to-br from-blue-100 to-purple-100 rounded-full flex items-center justify-center mx-auto mb-6">
                <Upload className="w-12 h-12 text-blue-600" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-2">
                No videos yet
              </h3>
              <p className="text-gray-600 mb-6 max-w-md mx-auto">
                Start by uploading your first video. It's quick and easy!
              </p>
              <UploadVideoDialog onSuccess={refetch}>
                {({ open }) => (
                  <button
                    onClick={open}
                    className="inline-flex items-center px-6 py-3 bg-gradient-to-r from-blue-600 to-purple-600 text-white font-semibold rounded-lg hover:from-blue-700 hover:to-purple-700 transition-all"
                  >
                    <Upload className="w-5 h-5 mr-2" />
                    Upload Your First Video
                  </button>
                )}
              </UploadVideoDialog>
            </div>
          )}
        </div>
      </section>
    </div>
  );
};

export default Dashboard;
