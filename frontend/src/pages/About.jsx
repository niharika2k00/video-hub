import { Link } from "react-router-dom";
import AboutComponent from "@/components/AboutComponent";
import UploadVideoDialog from "@/components/UploadVideoDialog";
import { Upload, BarChart3, ArrowRight } from "lucide-react";

const About = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50">
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-r from-blue-500 via-purple-600 to-indigo-700 text-white">
        <div className="absolute inset-0 bg-black/20"></div>
        <div className="text-center relative max-w-7xl mx-auto px-4 py-16 pb-22 sm:px-6 sm:pb-24 md:pb-30 lg:pb-40 lg:px-8 ">
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold mb-6">
            About <span className="text-yellow-300">VideoHub</span>
          </h1>
          <p className="text-lg md:text-lg lg:text-2xl mb-8 max-w-3xl mx-auto text-blue-100">
            The ultimate platform for video hosting, streaming, and sharing with
            cutting-edge ABR technology and seamless HLS integration.
          </p>

          <div className="flex gap-6 justify-center">
            <UploadVideoDialog
              onSuccess={() => {
                console.log("Video uploaded successfully!");
                window.location.href = "/dashboard";
              }}
            >
              {({ open }) => (
                <button
                  onClick={open}
                  className="inline-flex items-center px-8 py-3 bg-yellow-300 text-gray-900 font-semibold rounded-lg hover:bg-yellow-400 transition-colors"
                >
                  <Upload className="w-5 h-5 mr-2" />
                  Start Uploading
                </button>
              )}
            </UploadVideoDialog>

            <Link
              to="/dashboard"
              className="inline-flex items-center px-8 py-3 border-2 border-white text-white font-semibold rounded-lg hover:bg-white hover:text-gray-900 transition-colors"
            >
              <BarChart3 className="w-5 h-5 mr-2" />
              View Dashboard
            </Link>
          </div>
        </div>

        {/* Decorative SVG */}
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

      {/* Common Section for Home and About */}
      <AboutComponent />

      {/* CTA Section */}
      <section className="py-20 bg-gradient-to-tl from-orange-200 via-pink-100 to-rose-200 text-white">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl md:text-3xl lg:text-4xl font-bold mb-6 text-gray-900">
            Ready to Get Started?
          </h2>
          <p className="text-lg md:text-lg lg:text-xl mb-8 text-gray-600 font-normal">
            Simple tools for uploading and streaming videos with adaptive
            playback and instant shareable links — all in one place. Start
            uploading and sharing your videos today.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link
              to="/dashboard"
              className="inline-flex items-center justify-center px-8 py-3 bg-gradient-to-r from-blue-600 to-purple-600 text-white font-semibold rounded-lg hover:from-blue-700 hover:to-purple-700 transition-all"
            >
              Get Started
              <ArrowRight className="w-5 h-5 ml-2" />
            </Link>
            <Link
              to="/contact"
              className="inline-flex justify-center items-center px-8 py-3 border-2 border-gray-300 text-gray-700 font-semibold rounded-lg hover:border-gray-400 hover:text-gray-900 transition-colors"
            >
              Contact Us
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
};

export default About;
