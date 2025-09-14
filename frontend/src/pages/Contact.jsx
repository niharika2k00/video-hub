import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Mail, Phone, MapPin, Send, ArrowLeft, Loader2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import api from "@/utils/api";
import { toast } from "react-toastify";
import { analytics } from "@/utils/analytics";

const Contact = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    subject: "",
    message: "",
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [touched, setTouched] = useState({
    subject: false,
    message: false,
  });

  // Track Contact Us page visit
  useEffect(() => {
    analytics.trackContactPageVisit();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      const response = await api.post("/contact", formData);
      console.log(response);
      toast.success("Message sent successfully! We'll get back to you soon.");

      setFormData({
        // Reset form
        name: "",
        email: "",
        subject: "",
        message: "",
      });

      // Reset touched state
      setTouched({
        subject: false,
        message: false,
      });
    } catch (error) {
      console.error("Contact form submission error:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });

    // Mark field as touched when user starts typing
    if (!touched[e.target.name] && e.target.value.length > 0) {
      setTouched({
        ...touched,
        [e.target.name]: true,
      });
    }
  };

  const handleBlur = (e) => {
    setTouched({
      ...touched,
      [e.target.name]: true,
    });
  };

  const contactInfo = [
    {
      icon: <Mail className="w-6 h-6" />,
      title: "Email Us",
      description: "Get in touch with our support team",
      value: "support@videohub.com",
      link: "mailto:support@videohub.com",
    },
    {
      icon: <Phone className="w-6 h-6" />,
      title: "Call Us",
      description: "Speak with our team directly",
      value: "+91 8902912308",
      link: "tel:+918902912308",
    },
    {
      icon: <MapPin className="w-6 h-6" />,
      title: "Visit Us",
      description: "Our office location",
      value: "Remote",
      link: "#",
    },
  ];

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-r from-blue-600 via-purple-600 to-indigo-700 text-white py-16  pb-22 sm:pb-24 md:pb-30 lg:pb-40">
        <div className="absolute inset-0 bg-black/20"></div>
        <div className="text-center relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <Link
              to="/about"
              className="inline-flex items-center text-blue-100 hover:text-white mb-6 transition-colors"
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              Back to About
            </Link>
            <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold mb-6">
              Get in <span className="text-yellow-300">Touch</span>
            </h1>
            <p className="text-lg md:text-lg lg:text-2xl max-w-3xl text-blue-100 mx-auto my-auto">
              Have questions about VideoHub ? We're here to help you get the
              most out of our platform.
            </p>
          </div>
        </div>

        {/* Decorative SVG */}
        <div className="absolute bottom-0 left-0 right-0 -mb-px">
          <svg
            viewBox="0 0 1200 120"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M0 0L50 20C100 40 200 80 300 80C400 80 500 40 600 20C700 0 800 0 900 20C1000 40 1100 80 1150 100L1200 120V120H0V0Z"
              fill="#fff"
            />
          </svg>
        </div>
      </section>

      {/* Contact Form Section */}
      <section className="py-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12">
            {/* Contact Form */}
            <div>
              <h2 className="text-3xl font-bold text-gray-900 mb-6">
                Send us a Message
              </h2>
              <p className="text-lg text-gray-600 mb-8">
                Fill out the form below and we'll get back to you as soon as
                possible.
              </p>

              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="grid md:grid-cols-2 gap-6">
                  <div>
                    <label
                      htmlFor="name"
                      className="block text-sm font-medium text-gray-700 mb-2"
                    >
                      Name
                    </label>
                    <Input
                      type="text"
                      id="name"
                      name="name"
                      value={formData.name}
                      onChange={handleChange}
                      required
                      placeholder="Your name"
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="email"
                      className="block text-sm font-medium text-gray-700 mb-2"
                    >
                      Email
                    </label>
                    <Input
                      type="email"
                      id="email"
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      required
                      placeholder="your@email.com"
                    />
                  </div>
                </div>

                <div>
                  <label
                    htmlFor="subject"
                    className="block text-sm font-medium text-gray-700 mb-2"
                  >
                    Subject
                  </label>
                  <Input
                    type="text"
                    id="subject"
                    name="subject"
                    value={formData.subject}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    required
                    placeholder="What's this about ?"
                    maxLength={300}
                  />
                  <div className="flex justify-between items-center mt-1">
                    <span className="text-xs text-gray-500">
                      {touched.subject &&
                      formData.subject.length > 0 &&
                      formData.subject.length < 5 ? (
                        <span className="text-red-500">
                          Subject must be at least 5 characters
                        </span>
                      ) : (
                        <span className="text-green-500">
                          Subject looks good
                        </span>
                      )}
                    </span>
                    <span className="text-xs text-gray-400">
                      {formData.subject.length}/300
                    </span>
                  </div>
                </div>

                <div>
                  <label
                    htmlFor="message"
                    className="block text-sm font-medium text-gray-700 mb-2"
                  >
                    Message
                  </label>
                  <Textarea
                    id="message"
                    name="message"
                    value={formData.message}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    required
                    rows={6}
                    placeholder="Tell us more about your inquiry..."
                    maxLength={6000}
                  />
                  <div className="flex justify-between items-center mt-1">
                    <span className="text-xs text-gray-500">
                      {touched.message &&
                      formData.message.length > 0 &&
                      formData.message.length < 5 ? (
                        <span className="text-red-500">
                          Message must be at least 5 characters
                        </span>
                      ) : (
                        <span className="text-green-500">
                          Message looks good
                        </span>
                      )}
                    </span>
                    <span className="text-xs text-gray-400">
                      {formData.message.length}/6000
                    </span>
                  </div>
                </div>

                <Button
                  type="submit"
                  disabled={isSubmitting}
                  className="w-full bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {isSubmitting ? (
                    <>
                      <Loader2 className="w-5 h-5 mr-2 animate-spin" />
                      Sending...
                    </>
                  ) : (
                    <>
                      <Send className="w-5 h-5 mr-2" />
                      Send Message
                    </>
                  )}
                </Button>
              </form>
            </div>

            {/* Contact Information */}
            <div>
              <h2 className="text-3xl font-bold text-gray-900 mb-6">
                Contact Information
              </h2>
              <p className="text-lg text-gray-600 mb-8">
                Reach out to us through any of these channels. We're here to
                help!
              </p>

              <div className="space-y-6">
                {contactInfo.map((info, index) => (
                  <div
                    key={index}
                    className="flex items-start space-x-4 p-6 bg-gray-50 rounded-lg"
                  >
                    <div className="w-12 h-12 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg flex items-center justify-center text-white flex-shrink-0">
                      {info.icon}
                    </div>
                    <div>
                      <h3 className="text-lg font-semibold text-gray-900 mb-1">
                        {info.title}
                      </h3>
                      <p className="text-gray-600 mb-2">{info.description}</p>
                      <a
                        href={info.link}
                        className="text-blue-600 hover:text-blue-700 font-medium"
                      >
                        {info.value}
                      </a>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* FAQ Section */}
      <section className="py-20 bg-gray-50">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-4">
              Frequently Asked Questions
            </h2>
            <p className="text-lg text-gray-600">
              Quick answers to common questions about VideoHub
            </p>
          </div>

          <div className="space-y-6">
            <div className="bg-white rounded-lg p-6 shadow-sm">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                How do I upload a video?
              </h3>
              <p className="text-gray-600">
                Simply drag and drop your video file onto the upload area or
                click to browse. We support MP4, MOV, AVI, and other popular
                formats up to 10GB.
              </p>
            </div>

            <div className="bg-white rounded-lg p-6 shadow-sm">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                Can I embed videos on my website?
              </h3>
              <p className="text-gray-600">
                Yes! Every video gets a unique embed code that you can use on
                any website that supports HLS streaming. Perfect for content
                creators and businesses.
              </p>
            </div>

            <div className="bg-white rounded-lg p-6 shadow-sm">
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                What video quality do you support?
              </h3>
              <p className="text-gray-600">
                We support up to 4K resolution with automatic quality
                adaptation. Our ABR technology ensures the best viewing
                experience on any device.
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Contact;
