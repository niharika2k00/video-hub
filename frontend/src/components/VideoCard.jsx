import { Link } from "react-router-dom";
import { cn } from "@/lib/utils";
import Skeleton from "react-loading-skeleton";

export default function VideoCard({ video, className }) {
  if (!video) {
    return (
      <div className={cn("overflow-hidden rounded-xl shadow-lg", className)}>
        <Skeleton className="h-48 w-full" />
        <div className="p-4 space-y-2">
          <Skeleton className="h-4 w-3/4" />
          <Skeleton className="h-3 w-1/2" />
        </div>
      </div>
    );
  }

  const { id, title, category, thumbnailUrl } = video;

  return (
    <Link
      to={`/video/${id}`}
      className={cn(
        "group relative overflow-hidden rounded-xl shadow-lg transition-transform hover:-translate-y-1",
        className
      )}
    >
      <img
        src={thumbnailUrl}
        alt={title}
        className="h-48 w-full object-cover group-hover:scale-105 transition-transform duration-300"
      />
      <div className="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent" />
      <div className="absolute bottom-0 p-4 text-white">
        <h3 className="text-lg font-semibold line-clamp-1">{title}</h3>
        <span className="text-xs opacity-80">{category}</span>
      </div>
    </Link>
  );
}
