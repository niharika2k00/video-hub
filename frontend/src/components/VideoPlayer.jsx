// src/components/VideoPlayer.jsx
import { useEffect, useRef } from "react";
import videojs from "video.js";
import "video.js/dist/video-js.css";
import "videojs-contrib-quality-levels";
import "videojs-hls-quality-selector";

const VideoPlayer = ({ src, poster, autoplay = false }) => {
  const videoRef = useRef(null);
  const playerRef = useRef(null);

  /* -------- first mount: create player ---------- */
  useEffect(() => {
    if (!videoRef.current || playerRef.current) return;

    /*
    Reference: https://www.npmjs.com/package/video.js?activeTab=readme
    There are two ways to initialize a Video.js player:

    [Method 1] Dynamically create the <video> element using `document.createElement()`, append it to the DOM, and immediately initialize Video.js with `videojs(videoElement)`. This works reliably because the element is guaranteed to exist when initialization happens.

    [Method 2] Declare the <video> tag inline in JSX. While cleaner and more declarative, this approach may fail because the DOM element might not be fully committed/rendered when `useEffect` runs. As a result, `videojs(videoRef.current)` may not find a ready element.

    ❗❗ Issue: In Method 2, `videojs(videoRef.current)` runs before the <video> tag is fully available in the DOM, leading to missing player UI or failed plugin setup, whereas Method 1 is working fine.

    ✅ To make Method 2 work reliably in React, we wrap the initialization in `requestAnimationFrame` or set a timeout to defer it until the browser has painted the DOM. This ensures that the video element exists and is ready for Video.js to hook into.
    */

    // Create the element
    /* const videoElement = document.getElementById("video-js");
    const videoElement = document.createElement("video");
    videoElement.classList.add(
      "video-js",
      "vjs-big-play-centered",
      "h-full",
      "w-full"
    );
    videoRef.current.appendChild(videoElement); */

    // Init video.js
    requestAnimationFrame(() => {
      playerRef.current = videojs(
        videoRef.current,
        {
          controls: true,
          responsive: true,
          fluid: true,
          aspectRatio: "16:9",
          autoplay,
          muted: autoplay,
          preload: "auto",
          playsinline: true,
          poster,
          sources: [{ src, type: "application/x-mpegURL" }], // "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
          playbackRates: [0.5, 1, 1.25, 1.5, 2],
        },
        () => {
          // plugin must be added after player is ready
          playerRef.current.hlsQualitySelector({
            displayCurrentQuality: true,
          });

          // Add error handling
          playerRef.current.on("error", () => {
            const err = playerRef.current.error();
            console.error("Video.js error →", err);
          });

          // Add ready event handler
          playerRef.current.ready(() => {
            console.log("Video.js READY, tech →", playerRef.current.techName_);
            playerRef.current.play().catch(() => {
              console.info(
                "Waiting for user gesture before playing with audio"
              );
            });
          });
        }
      );
    });
  }, []);

  /* -------- src / poster change ---------- */
  useEffect(() => {
    const player = playerRef.current;
    if (player) {
      player.src({ src, type: "application/x-mpegURL" });
      if (poster) player.poster(poster);
    }
  }, [src, poster]);

  /* -------- cleanup ---------- */
  useEffect(() => {
    const player = playerRef.current;
    return () => {
      if (player && !player.isDisposed()) {
        player.dispose();
        playerRef.current = null;
      }
    };
  }, []);

  return (
    <div
      data-vjs-player
      className="w-full max-w-4xl mx-auto aspect-video rounded-xl overflow-hidden shadow-lg"
    >
      <video
        crossOrigin="anonymous"
        className="video-js h-full w-full vjs-big-play-centered"
        ref={videoRef}
        style={{ height: "100%", width: "100%" }}
        title="Video Player"
      />
    </div>
  );
};

export default VideoPlayer;
