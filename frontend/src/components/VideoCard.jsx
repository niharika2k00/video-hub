import { useState } from "react";
import { Link } from "react-router-dom";
import { cn } from "@/lib/utils";
import Skeleton from "react-loading-skeleton";
import { Trash2 } from "lucide-react";
import ConfirmationDialog from "@/components/ui/confirmation-dialog";

export default function VideoCard({ video, className, onDelete }) {
  const [showDeleteDialog, setShowDeleteDialog] = useState(false);

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
    <div className={cn("group relative", className)}>
      <Link
        to={`/video/${id}`}
        className="block overflow-hidden rounded-xl shadow-lg transition-transform hover:-translate-y-1"
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

      {/* Delete Button */}
      <button
        onClick={(e) => {
          e.preventDefault();
          e.stopPropagation();
          setShowDeleteDialog(true);
        }}
        className="absolute top-2 right-3 p-1.5 bg-red-500 hover:bg-red-600 text-white rounded-md shadow-lg transition-colors duration-200 opacity-0 group-hover:opacity-100"
        title="Delete video"
      >
        <Trash2 size={18} />
      </button>

      {/* Confirmation Dialog */}
      {showDeleteDialog && (
        <ConfirmationDialog
          isDialogOpen={showDeleteDialog}
          onDialogStateChange={setShowDeleteDialog}
          title="Delete Video"
          description="Are you sure you want to delete this video? This action cannot be undone."
          confirmText="Delete"
          cancelText="Cancel"
          variant="destructive"
          onConfirm={() => onDelete(id)}
        />
      )}
    </div>
  );
}
