import Hero from "@/components/Hero";
import { Link } from "react-router-dom";
import AboutComponent from "@/components/AboutComponent";
import { Upload, Play, Share2, Zap, Users, ArrowRight } from "lucide-react";

export default function Home() {
  const steps = [
    {
      number: "01",
      title: "Upload Your Video",
      description:
        "Drag and drop your video file. We support MP4, MOV, AVI, and more formats.",
      icon: <Upload className="w-8 h-8" />,
    },
    {
      number: "02",
      title: "Automatic Processing",
      description:
        "Our system automatically transcodes your video into multiple quality levels.",
      icon: <Zap className="w-8 h-8" />,
    },
    {
      number: "03",
      title: "Get Shareable Link",
      description:
        "Receive an instant shareable link that works on any device or platform.",
      icon: <Share2 className="w-8 h-8" />,
    },
  ];

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <Hero />

      {/* Features Preview Section */}
      <section className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              Why Choose VideoHub?
            </h2>
            <p className="text-xl text-gray-600 max-w-3xl mx-auto">
              Professional video hosting with enterprise-grade features for
              creators and businesses
            </p>

            <Link
              to="/about"
              onClick={() => window.scrollTo(0, 0)}
              className="mt-4 inline-flex items-center text-blue-600 hover:text-blue-700 font-medium transition-colors group"
            >
              Learn more about Video Hub
              <ArrowRight className="w-4 h-4 ml-2 group-hover:translate-x-1 transition-transform" />
            </Link>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            {/* Feature 1 */}
            <div className="text-center group">
              <div className="w-16 h-16 bg-gradient-to-br from-blue-500 to-purple-600 rounded-2xl flex items-center justify-center text-white mx-auto mb-6 group-hover:scale-110 transition-transform duration-300">
                <Upload className="w-8 h-8" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-3">
                One-Click Upload
              </h3>
              <p className="text-gray-600">
                Drag and drop your videos. We handle the rest with automatic
                transcoding and optimization.
              </p>
            </div>

            {/* Feature 2 */}
            <div className="text-center group">
              <div className="w-16 h-16 bg-gradient-to-br from-purple-500 to-pink-600 rounded-2xl flex items-center justify-center text-white mx-auto mb-6 group-hover:scale-110 transition-transform duration-300">
                <Play className="w-8 h-8" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-3">
                Adaptive Streaming
              </h3>
              <p className="text-gray-600">
                HLS technology with ABR ensures perfect playback on any device
                or connection speed.
              </p>
            </div>

            {/* Feature 3 */}
            <div className="text-center group">
              <div className="w-16 h-16 bg-gradient-to-tl from-pink-600 to-red-400 rounded-2xl flex items-center justify-center text-white mx-auto mb-6 group-hover:scale-110 transition-transform duration-300">
                <Users className="w-8 h-8" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-3">
                Global Reach
              </h3>
              <p className="text-gray-600">
                Worldwide CDN ensures fast loading and high availability for
                your audience globally.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* How It Works Section */}
      <section className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              How It Works
            </h2>
            <p className="text-xl text-gray-600 max-w-3xl mx-auto">
              Get your videos online in three simple steps
            </p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            {steps.map((step, index) => (
              <div key={index} className="text-center relative">
                <div className="w-20 h-20 bg-gradient-to-br from-blue-500 to-purple-600 rounded-full flex items-center justify-center text-white mx-auto mb-6">
                  {step.icon}
                </div>
                <div className="absolute -top-2 -left-1 w-8 h-8 bg-yellow-400 rounded-full flex items-center justify-center text-gray-900 text-sm font-bold">
                  {step.number}
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-3">
                  {step.title}
                </h3>
                <p className="text-gray-600">{step.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Common Section for Home and About */}
      <AboutComponent />
    </div>
  );
}
