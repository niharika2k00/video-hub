import React from "react";
import {
  Upload,
  Play,
  Share2,
  Globe,
  Zap,
  ShieldCheck,
  CheckCircle,
} from "lucide-react";

const AboutComponent = () => {
  const features = [
    {
      icon: <Upload className="w-6 h-6" />,
      title: "Seamless Uploads",
      description:
        "Simple drag and drop interface for seamless video uploads with support for multiple formats including MP4, MOV, AVI, and more.",
    },
    {
      icon: <Zap className="w-6 h-6" />,
      title: "ABR Adaptive Streaming",
      description:
        "Delivers Adaptive Bitrate (ABR) streaming powered by HTTP Live Streaming (HLS) that automatically adjusts quality based on connection.",
    },
    {
      icon: <Play className="w-6 h-6" />,
      title: "HLS Live Streaming",
      description:
        "HTTP Live Streaming (HLS) technology ensures flawless playback across all devices and browsers with automatic quality switching.",
    },
    {
      icon: <Share2 className="w-6 h-6" />,
      title: "External Video Links",
      description:
        "Generate shareable links for your videos that can be embedded on any website or platform that supports HLS streaming.",
    },
    {
      icon: <Globe className="w-6 h-6" />,
      title: "Global CDN",
      description:
        "Worldwide content delivery network ensures fast loading times and high availability for your videos across the globe.",
    },
    {
      icon: <ShieldCheck className="w-6 h-6" />,
      title: "Secure & Private",
      description:
        "Advanced security features with optional password protection and private video sharing options.",
    },
  ];

  return (
    <>
      {/* Mission Section */}
      <section className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div>
              <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-6">
                Our Mission
              </h2>

              <p className="text-lg text-gray-600 mb-6">
                VideoHub was founded with a clear mission: to democratize video
                hosting and streaming using HLS with Adaptive Bitrate for
                creators, businesses, and individuals across the globe. Every
                uploaded video gets an instant shareable external link, allowing
                effortless embedding into websites, apps, and digital platforms.
              </p>
              <p className="text-lg text-gray-600 mb-8">
                Designed to remove barriers of cost, complexity, or technical
                overhead. VideoHub empowers creators and businesses with smooth,
                scalable video delivery and instant shareable links for
                embedding anywhere.
              </p>
              <div className="space-y-4">
                <div className="flex items-center">
                  <CheckCircle className="w-5 h-5 text-green-500 mr-3" />
                  <span className="text-gray-700">
                    Professional-grade streaming for all
                  </span>
                </div>
                <div className="flex items-center">
                  <CheckCircle className="w-5 h-5 text-green-500 mr-3" />
                  <span className="text-gray-700">
                    Instant sharing, effortless embedding
                  </span>
                </div>
                <div className="flex items-center">
                  <CheckCircle className="w-5 h-5 text-green-500 mr-3" />
                  <span className="text-gray-700">
                    Streamlined infrastructure, no complexity
                  </span>
                </div>
              </div>
            </div>
            <div className="relative">
              <div className="bg-gradient-to-br from-blue-100 to-purple-100 rounded-2xl p-8">
                <img
                  src="video-upload.jpg"
                  alt="Developer illustration"
                  className="w-full h-auto"
                />
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              Powerful Features
            </h2>
            <p className="text-xl text-gray-600 max-w-3xl mx-auto">
              Everything you need for professional video hosting and streaming
              in one platform
            </p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <div
                key={index}
                className="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-shadow"
              >
                <div className="w-12 h-12 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg flex items-center justify-center text-white mb-4">
                  {feature.icon}
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-3">
                  {feature.title}
                </h3>
                <p className="text-gray-600">{feature.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Technology Section */}
      <section className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div className="relative">
              <div className="bg-gradient-to-br from-indigo-100 to-purple-100 rounded-2xl p-8">
                <img
                  src="landing.jpg"
                  alt="Developer illustration"
                  // className="w-full h-140"
                  className="w-full h-auto"
                />
              </div>
            </div>
            <div>
              <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-6">
                Advanced Streaming Technology
              </h2>
              <div className="space-y-6">
                <div>
                  <h3 className="text-xl font-semibold text-gray-900 mb-3">
                    ABR (Adaptive Bitrate) Streaming
                  </h3>
                  <p className="text-gray-600">
                    Our platform automatically adjusts video quality based on
                    viewer's internet connection, ensuring smooth playback
                    without buffering. Multiple quality levels are created from
                    your uploaded video.
                  </p>
                </div>
                <div>
                  <h3 className="text-xl font-semibold text-gray-900 mb-3">
                    HLS (HTTP Live Streaming)
                  </h3>
                  <p className="text-gray-600">
                    Industry-standard HLS technology ensures compatibility with
                    all modern browsers and devices. Your videos will play
                    seamlessly on iOS, Android, desktop, and smart TVs.
                  </p>
                </div>
                <div>
                  <h3 className="text-xl font-semibold text-gray-900 mb-3">
                    External Embedding
                  </h3>
                  <p className="text-gray-600">
                    Generate shareable links for your videos that can be
                    embedded on any website or platform. Perfect for content
                    creators, educators, and businesses.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default AboutComponent;
