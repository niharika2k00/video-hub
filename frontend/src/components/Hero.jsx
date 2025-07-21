import { NavLink } from "react-router-dom";
import { Button } from "@/components/ui/button";

export default function Hero() {
  return (
    <section className="relative isolate overflow-hidden">
      <img
        className="absolute inset-0 -z-10 h-full w-full object-cover opacity-90"
        src="https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1600&q=80"
        // src="https://images.pexels.com/photos/7991374/pexels-photo-7991374.jpeg?auto=compress&cs=tinysrgb&w=1600&h=750&dpr=2"
        alt="Video thumbnails"
      />
      <div className="absolute inset-0 -z-10 bg-gradient-to-tr from-gray-900/80 to-primary/70 mix-blend-multiply" />
      <div className="container mx-auto px-6 py-32 max-w-2xl">
        <h1 className="text-5xl md:text-6xl font-extrabold text-white text-center leading-tight drop-shadow mb-6">
          Seamless <span className="text-secondary">Video</span> Upload&nbsp;
          <br className="hidden lg:block" /> &amp; Adaptive Streaming
        </h1>
        <p className="text-lg text-gray-200 mb-8">
          Upload once, stream everywhere — VideoHub automatically transcodes
          your video into multiple qualities so your viewers always enjoy the
          best experience.
        </p>
        <Button asChild size="lg" variant="secondary">
          <NavLink to="/dashboard">Get Started</NavLink>
        </Button>
      </div>
    </section>
  );
}
