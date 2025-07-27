import { NavLink } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { ArrowRight, Sparkles } from "lucide-react";

export default function Hero() {
  return (
    <>
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-r from-blue-600 via-purple-600 to-indigo-700 text-white">
        {/* Background Pattern */}
        <div className="absolute inset-0 bg-black/20"></div>
        <div className="absolute inset-0 bg-gradient-to-br from-blue-600/30 via-purple-600/30 to-indigo-700/30"></div>

        {/* Floating Elements */}
        <div className="absolute top-20 left-10 w-20 h-20 bg-white/10 rounded-full blur-xl"></div>
        <div className="absolute top-40 right-20 w-32 h-32 bg-yellow-300/20 rounded-full blur-xl"></div>
        <div className="absolute bottom-20 left-1/4 w-16 h-16 bg-pink-400/20 rounded-full blur-xl"></div>
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24">
          <div className="text-center">
            {/* Main Heading */}
            <h1 className="text-4xl md:text-6xl lg:text-7xl font-bold mb-6 leading-tight">
              Seamless <span className="text-yellow-300">Video</span> Upload
              &amp;
              <br className="hidden lg:block" />
              <span className="text-pink-300">Adaptive Streaming</span>
            </h1>

            {/* Subtitle */}
            <p className="text-lg md:text-2xl mb-8 max-w-4xl mx-auto text-blue-100 leading-relaxed">
              Upload once, stream everywhere — VideoHub automatically transcodes
              your videos into multiple qualities with HLS technology for the
              best viewing experience across all devices.
            </p>

            {/* CTA Buttons */}
            <div className="flex flex-col sm:flex-row gap-4 justify-center mb-16">
              <Button
                asChild
                size="lg"
                className="bg-yellow-300 hover:bg-yellow-400 text-gray-900 font-semibold px-8 py-4 text-lg rounded-xl transition-all duration-300 transform hover:scale-105"
              >
                <NavLink to="/dashboard" className="flex items-center">
                  <Sparkles className="w-4 h-4 mr-2" />
                  Get Started
                  <ArrowRight className="w-5 h-5 ml-2" />
                </NavLink>
              </Button>
            </div>
          </div>
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
    </>
  );
}
