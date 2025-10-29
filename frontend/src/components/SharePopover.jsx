import {
  Popover,
  PopoverTrigger,
  PopoverContent,
} from "@/components/ui/popover";
import { Button } from "@/components/ui/button";
import { Share2, Copy, CopyCheck } from "lucide-react";
import { toast } from "react-toastify";
import { useState } from "react";
import { analytics } from "@/utils/analytics";

function SharePopover({ url, videoId, videoTitle }) {
  const [copied, setCopied] = useState(false);

  const copy = async () => {
    await navigator.clipboard.writeText(url);
    setCopied(true);
    analytics.trackSharePopupOpen(videoId, videoTitle);
    toast.success("Copied to clipboard");
    setTimeout(() => setCopied(false), 1500); // stop showing the check icon after 1.5 seconds
  };

  const handleOpenChange = (open) => {
    if (open) {
      analytics.trackSharePopupOpen(videoId, videoTitle);
    }
  };

  return (
    <Popover onOpenChange={handleOpenChange}>
      <PopoverTrigger asChild>
        <div className="flex items-center gap-2 px-3 py-1 bg-primary/20 rounded-full shadow-sm border border-gray-200 hover:bg-gray-200 transition cursor-pointer w-fit">
          <span className="text-sm font-medium text-gray-700">
            Get Embed Link
          </span>
          <Button size="icon" variant="ghost" aria-label="Share">
            <Share2 className="h-5 w-5" />
          </Button>
        </div>
      </PopoverTrigger>

      <PopoverContent align="end" className="w-80">
        <p className="text-sm font-medium mb-2">Share this video</p>
        <div className="flex items-center gap-2 rounded-md border bg-muted px-3 py-2">
          <span className="truncate text-xs" title={url}>
            {url}

            {/* Tailwind tooltip */}
            <span className="absolute bottom-full left-0 mb-2 px-2 py-1 bg-gray-800 text-white text-xs rounded shadow-lg opacity-0 group-hover:opacity-100 transition-opacity duration-200 pointer-events-none whitespace-nowrap z-50 max-w-xs">
              {url}
              {/* Arrow */}
              <span className="absolute top-full left-4 w-0 h-0 border-l-4 border-r-4 border-t-4 border-l-transparent border-r-transparent border-t-gray-800"></span>
            </span>
          </span>

          <button
            aria-label="Copy link"
            onClick={copy}
            className="ml-auto text-muted-foreground hover:text-foreground cursor-pointer focus-visible:outline-none"
          >
            {copied ? (
              <CopyCheck className="h-5 w-5 text-green-500" />
            ) : (
              <Copy className="h-5 w-5" />
            )}
          </button>
        </div>
      </PopoverContent>
    </Popover>
  );
}

export default SharePopover;
